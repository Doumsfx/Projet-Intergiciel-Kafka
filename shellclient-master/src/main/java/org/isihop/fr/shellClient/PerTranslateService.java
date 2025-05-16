package org.isihop.fr.shellClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PerTranslateService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${application.topicin}")
    private String TOPICIN;

    @Autowired
    public PerTranslateService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${application.topicout}")
    public void listenAndTranslate(String message) {
        String translated = translate(message);
        kafkaTemplate.send(TOPICIN, translated);
    }

    @KafkaListener(topics = "${application.topicout}")
    public void consume(String message) {
        String from = extract_FROM(message);
        String to = extract_TO(message);
        String msg = extract_MSG(message);

        String translated = translate(msg);
        kafkaTemplate.send(TOPICIN, "FROM:"+from+"#TO:"+to+"#"+translated);
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

    private String translate(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}
