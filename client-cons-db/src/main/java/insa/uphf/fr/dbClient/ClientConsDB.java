package insa.uphf.fr.dbClient;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Client Kafka "ConsDB" (Producteur et Consommateur) qui écoute les messages sur les topic OUT (réception de messages non traduits), IN (réception de messages traduits) et TECHOUT (réception de requêtes techniques),
 * et qui écrit sur le topic TECHIN (envoie de réponses techniques).
 */
@Service
public class ClientConsDB {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Connection connection;

    @Value("${application.topicin}")
    private String TOPICIN;

    @Value("${application.topicout}")
    private String TOPICOUT;

    @Value("${application.topictechin}")
    private String TOPICTECHIN;

    @Value("${application.topictechout}")
    private String TOPICTECHOUT;

    @Autowired
    public ClientConsDB(KafkaTemplate<String, String> kafkaTemplate, Connection connection) {
        this.kafkaTemplate = kafkaTemplate;
        this.connection = connection;
        System.out.println("ClientConsDB initialized with connection: " + connection);
    }

    /****************** 
    Listener Kafka pour le topic OUT (messages non traduits envoyés par les clients)
    *******************/
    @KafkaListener(topics = "${application.topicout}")
    public void consumeOut(String message) {
        // Sauvegarder le message dans la base de données
        try {
            DB.insert_log(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /****************** 
    Listener Kafka pour le topic IN (messages traduits envoyés par le service de traduction)
    *******************/
    @KafkaListener(topics = "${application.topicin}")
    public void consumeIn(String message) {
        // Sauvegarder le message dans la base de données
        try {
            DB.insert_log(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /****************** 
    Listener Kafka pour le topic TECHOUT (requêtes techniques envoyées par les clients)
    *******************/
    @KafkaListener(topics = "${application.topictechout}")
    public void consumeTech(String message) {
        
        String [] segments=message.split(":");
        String commande = segments[0].trim();
        String client;
        
        // GET:ClientA
        // CONNECT:ClientA
        // DISCONNECT:ClientA
        // ISCONNECTED:ClientA#ClientB

        switch (commande) {
            case "GET":
                // Renvoyer la liste des clients connectés
                client = segments[1].trim();
                try {
                    String clientsConnectes = DB.get_connected_clients();
                    this.kafkaTemplate.send(TOPICTECHIN, "FROM:ClientConsDB#TO:" + client + "#" + clientsConnectes);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "CONNECT":
                // Sauvegarder le client dans la base de données
                client = segments[1].trim();
                try {
                    DB.insert_client(client);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "DISCONNECT":
                // Supprimer le client de la base de données
                client = segments[1].trim();
                try {
                    DB.delete_client(client);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "ISCONNECTED":
                // Vérifier si un client est connecté
                String clients = segments[1].trim();
                String [] clientsConnectes = clients.split("#");
                try {
                    if (DB.is_connected(clientsConnectes[1])) {
                        this.kafkaTemplate.send(TOPICTECHIN, "FROM:ClientConsDB#TO:" + clientsConnectes[0] + "#" + clientsConnectes[1] + " est connecté");
                    } else {
                        this.kafkaTemplate.send(TOPICTECHIN, "FROM:ClientConsDB#TO:" + clientsConnectes[0] + "#" + clientsConnectes[1] + " n'est pas connecté");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}