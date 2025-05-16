package insa.uphf.fr.dbClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClientConsDB {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${application.topicin}")
    private String TOPICIN;

    @Value("${application.topicout}")
    private String TOPICOUT;

    @Value("${application.topictechin}")
    private String TOPICTECHIN;

    @Value("${application.topictechout}")
    private String TOPICTECHOUT;

    

    @Autowired
    public ClientConsDB(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    /****************** 
    Listener Kafka
    *******************/
    @KafkaListener(topics = "${application.topicout}")
    public void consume(String message) {
        // TODO Sauvegarder le message dans la base de données
        

    }


    /****************** 
    Listener Kafka pour le TECH
    *******************/
    @KafkaListener(topics = "${application.topictechout}")
    public void consumeTech(String message) {
        
        String [] segments=message.split(":");
        String commande = segments[0].trim();
        
        // GET:ClientA
        // CONNECT:ClientA
        // DISCONNECT:ClientA
        // ISCONNECTED:ClientA#ClientB

        switch (commande) {
            case "GET":
                // TODO Renvoyer la liste des clients connectés


                break;
            case "CONNECT":
                // TODO Sauvegarder le client dans la base de données

                break;
            case "DISCONNECT":
                // TODO Supprimer le client de la base de données

                break;
            case "ISCONNECTED":
                // TODO Vérifier si un client est connecté


                break;
            default:
                break;
        }

    }














    private String extract_TO(String message) 
    {
        String [] segments=message.split("#");
        return segments[1].substring(3);
    }
    
    private String extract_FROM(String message) 
    {
        String [] segments=message.split("#");
        return segments[0].substring(5);
    }
    
    private String extract_MSG(String message) 
    {
        String [] segments=message.split("#");
        return segments[2];
    }

}
