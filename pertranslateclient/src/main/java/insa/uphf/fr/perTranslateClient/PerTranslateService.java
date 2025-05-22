package insa.uphf.fr.perTranslateClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Client Kafka "PerTranslateService" (Producer et Consumer) qui écoute les messages sur le topic OUT (réception de messages non traduits) et écrit sur le topic IN (envoie de messages traduits)
 */
@Service
public class PerTranslateService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final LibreTranslateClient translateClient;
    
    @Value("${application.topicin}")
    private String TOPICIN;
    
    @Autowired
    public PerTranslateService(KafkaTemplate<String, String> kafkaTemplate, LibreTranslateClient translateClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.translateClient = translateClient;
    }
    
    /****************** 
    Listener Kafka pour le topic OUT (messages non traduits envoyés par les clients)
    *******************/
    @KafkaListener(topics = "${application.topicout}")
    public void consume(String message) {
        try {
            String from = extract_FROM(message);
            String to = extract_TO(message);
            String msg = extract_MSG(message);
            
            // Traduction automatique vers le français
            String translated = translateClient.translateToFrench(msg);
            
            // Envoi du message traduit
            kafkaTemplate.send(TOPICIN, "FROM:" + from + "#TO:" + to + "#" + translated);
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement du message: " + e.getMessage());
        }
    }
    
    private String extract_TO(String message) {
        String[] segments = message.split("#");
        return segments[1].substring(3);
    }
    
    private String extract_FROM(String message) {
        String[] segments = message.split("#");
        return segments[0].substring(5);
    }
    
    private String extract_MSG(String message) {
        String[] segments = message.split("#");
        return segments[2];
    }
}