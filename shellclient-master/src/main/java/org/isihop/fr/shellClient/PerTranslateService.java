package org.isihop.fr.shellClient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class PerTranslateService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RestTemplate restTemplate = new RestTemplate();
    private String apiUrl = "http://localhost:5000";

    @Value("${application.topicin}")
    private String TOPICIN;

    @Autowired
    public PerTranslateService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${application.topicout}")
    public void consume(String message) {
        String from = extract_FROM(message);
        String to = extract_TO(message);
        String msg = extract_MSG(message);

        String sourceLang = detectLang(msg);
        String translated = translateInFrench(msg, sourceLang);
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

    private String detectLang(String message) {
        DetectRequest request = new DetectRequest(message);
        String detectUrl = apiUrl + "/detect";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<DetectRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<DetectResponse> response = restTemplate.postForEntity(detectUrl, entity, DetectResponse.class);

        DetectResponse body = response.getBody();

        return body.getLanguage();
    }

    private String translateInFrench(String message, String sourceLang) {
        TranslateRequest request = new TranslateRequest(message, sourceLang, "fr", 1);
        String translateUrl = apiUrl + "/translate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<TranslateRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<TranslateResponse> response = restTemplate.postForEntity(translateUrl, entity, TranslateResponse.class);

        TranslateResponse body = response.getBody();

        return body.getTranslatedText();

    }
}
