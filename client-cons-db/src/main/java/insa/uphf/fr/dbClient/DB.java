package insa.uphf.fr.dbClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

/*
 * Fonctions d'appel à la base de données (connexion, déconnexion et requêtes)
 */
@Component
public class DB {

    private static Connection connection;

    public static Connection connect() throws SQLException {
        try {
            // Récupération des identifiants de la base de données
            var jdbcUrl = DatabaseConfig.getDbUrl();
            var user = DatabaseConfig.getDbUsername();
            var password = DatabaseConfig.getDbPassword();

            // Ouverture de la connexion
            connection = DriverManager.getConnection(jdbcUrl, user, password);

            return connection;

        } catch (SQLException  e) {
            connection = null;
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public static void insert_client(String client) throws SQLException {
        try {
            String sql = "INSERT INTO Client(nom, heure) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            // Message
            pstmt.setString(1, client);
            
            // Timestamp
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            pstmt.setTimestamp(2, timestamp);
            // Insert
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public static void insert_log(String message) throws SQLException {
        try {
            String sql = "INSERT INTO Logs(contenu, heure) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            // Message
            pstmt.setString(1, message);
            
            // Timestamp
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            pstmt.setTimestamp(2, timestamp);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public static void delete_client(String client) throws SQLException {
        try {
            String sql = "DELETE FROM Client WHERE nom = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            // Nom du client
            pstmt.setString(1, client);
            // Exécution de la requête
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public static boolean is_connected(String client) throws SQLException {
        try {
            String sql = "SELECT * FROM Client WHERE nom = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            // Nom du client
            pstmt.setString(1, client);
            // Exécution de la requête
            var rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }
    
    public static String get_connected_clients() throws SQLException {
        StringBuilder clients = new StringBuilder();
        try {
            String sql = "SELECT nom FROM Client";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            // Exécution de la requête
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                clients.append(rs.getString("nom")).append(", ");
            }
            
            if (clients.length() > 2) {
                return clients.toString().substring(0, clients.length() - 2);
            }
            return "";
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public static void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            // Fermeture de la connexion
            connection.close();
        }
    }
}