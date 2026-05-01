package db;

import java.sql.Connection;
import java.sql.Statement;

public class SetupDB {

    public static void main(String[] args) {

        try {
            Connection conn = DBconn.getConnection();
            Statement stmt = conn.createStatement();

            // 1️⃣ Create questions table
            String createQuestionsTable = 
                "CREATE TABLE IF NOT EXISTS questions (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "question VARCHAR(255), " +
                "answer VARCHAR(100)" +
                ")";

            stmt.executeUpdate(createQuestionsTable);

            // 2️⃣ Create scores table
            String createScoresTable = 
                "CREATE TABLE IF NOT EXISTS scores (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user VARCHAR(100), " +
                "marks INT, " +
                "attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

            stmt.executeUpdate(createScoresTable);

            // 3️⃣ Insert sample questions
            String insertQ1 = 
                "INSERT INTO questions (question, answer) VALUES " +
                "('What is the capital of India?', 'Delhi')";

            String insertQ2 = 
                "INSERT INTO questions (question, answer) VALUES " +
                "('What is the capital of Haryana?', 'Chandigarh')";

            String insertQ3 = 
                "INSERT INTO questions (question, answer) VALUES " +
                "('What is the capital of Uttar Pradesh?', 'Lucknow')";

            String insertQ4 = 
                "INSERT INTO questions (question, answer) VALUES " +
                "('What is the capital of Bihar?', 'Patna')";
                
            String insertQ5 = 
                "INSERT INTO questions (question, answer) VALUES " +
                "('What is the capital of West Bengal?', 'Kolkata')";
                
            String insertQ6 = 
                "INSERT INTO questions (question, answer) VALUES " +
                "('What is the capital of Maharashtra?', 'Mumbai')";

            String insertQ7 = 
                "INSERT INTO questions (question, answer) VALUES " +
                "('What is the capital of Tamil Nadu?', 'Chennai')";
                
            String insertQ8 = 
                "INSERT INTO questions (question, answer) VALUES " +
                "('What is the capital of Telangana?', 'Hyderabad')";
                
            String insertQ9 = 
                "INSERT INTO questions (question, answer) VALUES " +
                "('What is the capital of Andhra Pradesh?', 'Amaravati')";
                
            String insertQ10 = 
                "INSERT INTO questions (question, answer) VALUES " +
                "('What is the capital of Karnataka?', 'Bengaluru')";

            stmt.executeUpdate(insertQ1);
            stmt.executeUpdate(insertQ2);
            stmt.executeUpdate(insertQ3);
            stmt.executeUpdate(insertQ4);
            stmt.executeUpdate(insertQ5);
            stmt.executeUpdate(insertQ6);
            stmt.executeUpdate(insertQ7);
            stmt.executeUpdate(insertQ8);
            stmt.executeUpdate(insertQ9);
            stmt.executeUpdate(insertQ10);

            System.out.println("Tables created and sample data inserted successfully!");

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}