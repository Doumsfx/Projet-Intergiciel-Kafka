package insa.uphf.fr.perTranslateClient;

/**
 *
 * @author tondeur-h
 */
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.group-id}")
    private String groupid;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        // Générer un group-id dynamique
        String uuid=UUID.randomUUID().toString();
        String dynamicGroupId = groupid+"-"+uuid;
        
        factory.getContainerProperties().setGroupId(dynamicGroupId);

        System.out.println("Le groupId de ce client est : "+groupid+"-"+uuid);
        
        return factory;
    }
}
