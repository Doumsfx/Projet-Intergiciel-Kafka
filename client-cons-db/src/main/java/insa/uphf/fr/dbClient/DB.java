package insa.uphf.fr.dbClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DB {

    private static Connection connection;

    public static Connection connect() throws SQLException {

        try {
            // Get database credentials from DatabaseConfig class
            var jdbcUrl = DatabaseConfig.getDbUrl();
            var user = DatabaseConfig.getDbUsername();
            var password = DatabaseConfig.getDbPassword();

            // Open a connection
            connection = DriverManager.getConnection(jdbcUrl, user, password);

            return connection;

        } catch (SQLException  e) {
            connection = null;
            System.err.println(e.getMessage());
            return null;
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
        }
    }



    public static void delete_client(String client) throws SQLException {
        
        try {
            String sql = "DELETE FROM Client WHERE nom = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            // Client name
            pstmt.setString(1, client);
            // Delete
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
    }


    public static boolean is_connected(String client) throws SQLException {
        try {
            String sql = "SELECT * FROM Client WHERE nom = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            // Client name
            pstmt.setString(1, client);
            // Execute query
            var rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    
    public static String get_connected_clients() throws SQLException {
        StringBuilder clients = new StringBuilder();
        try {
            String sql = "SELECT nom FROM Client";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            // Execute query
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                clients.append(rs.getString("nom")).append(",");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return clients.toString();
    }

    /**
     * Close the database connection
     */
    public static void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }


}