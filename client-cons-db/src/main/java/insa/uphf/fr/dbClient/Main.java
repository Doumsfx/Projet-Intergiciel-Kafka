package insa.uphf.fr.dbClient;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        try (var connection =  DB.connect()){
            System.out.println("Connected to the PostgreSQL database.");

            DB.insert_client("Michel");
            DB.insert_client("Jean");
            System.out.println(DB.is_connected("Michel"));

            DB.insert_log("Test log");
            DB.insert_log("Test log 2");
            
            System.out.println(DB.get_connected_clients());

            DB.delete_client("Michel");
            DB.is_connected("Michel");

            DB.disconnect();
            System.out.println("Disconnected from the PostgreSQL database.");



        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}