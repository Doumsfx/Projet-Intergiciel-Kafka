package insa.uphf.fr.dbClient;

import java.sql.SQLException;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan
public class ClientConsApplication {
    public static void main(String[] args){
        // Connexion à la base de données
        try (var connection =  DB.connect()){
            System.out.println("Connected to the PostgreSQL database.");
            System.out.println(connection);

            // Charge l'application Spring Boot
            SpringApplication application = new SpringApplication(ClientConsApplication.class);
            application.setBannerMode(Mode.OFF);
            application.run(args);


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
