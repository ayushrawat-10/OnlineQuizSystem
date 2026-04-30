package dao;

import db.DBconn;
import model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the `questions` table.
 * Handles all DB operations related to questions.
 */
public class QuestionDAO {

    /**
     * Inserts a new question into the database.
     * 
     * @param question  The question text
     * @param answer    The correct answer
     */
    public void addQuestion(String question, String answer) {
        String sql = "INSERT INTO questions (question, answer) VALUES (?, ?)";

        try (Connection conn = DBconn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, question);
            pstmt.setString(2, answer);
            pstmt.executeUpdate();

            System.out.println("Question added successfully!");

        } catch (SQLException e) {
            System.out.println("Failed to add question.");
            e.printStackTrace();
        }
    }

    /**
     * Fetches all questions from the database.
     * 
     * @return List of Question objects
     */
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT id, question, answer FROM questions";

        try (Connection conn = DBconn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String questionText = rs.getString("question");
                String answer = rs.getString("answer");
                questions.add(new Question(id, questionText, answer));
            }

        } catch (SQLException e) {
            System.out.println("Failed to fetch questions.");
            e.printStackTrace();
        }

        return questions;
    }
}
