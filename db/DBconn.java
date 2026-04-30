package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconn {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_db";
    private static final String USER = "root";
    private static final String PASSWORD = "ayush";

    // Method to get connection
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Optional for newer JDBC, but safe to include
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected Successfully!");

        } catch (Exception e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }
        return conn;
    }
}
