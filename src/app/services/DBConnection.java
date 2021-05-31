package app.services;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static  Connection connection;

    private DBConnection() {
        String url= "jdbc:mysql://localhost:3300/";
        String dbName = "maskDetection";
        String username = "root";
        String password = "";
        try {
            connection = DriverManager.getConnection(url + dbName, username, password);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Connection getDBConnection() {
        if ( connection == null ) {
            new DBConnection();
        }
        return connection;
    }
}
