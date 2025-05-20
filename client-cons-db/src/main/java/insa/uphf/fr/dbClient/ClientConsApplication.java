package insa.uphf.fr.dbClient;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientConsApplication {
    public static void main(String[] args) {
        // Charge l'application Spring Boot
        SpringApplication application = new SpringApplication(ClientConsApplication.class);
        application.setBannerMode(Mode.OFF);
        application.run(args);
        
        // L'application reste active grâce aux Kafka listeners
    }
}