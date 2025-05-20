package insa.uphf.fr.dbClient;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PreDestroy;

@Configuration
public class DatabaseConnexion {

    private Connection connection;

    @Bean
    public Connection databaseConnexion() throws SQLException {
        // Connexion à la base de données
        connection = DB.connect();
        if (connection != null) {
            System.out.println("Connected to the PostgreSQL database.");
            System.out.println(connection);
        } else {
            System.err.println("Failed to establish database connection.");
        }
        return connection;
    }
    
    @PreDestroy
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Closing database connection...");
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}