package dao;

import db.DBconn;
import model.Score;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for scores table
 */
public class ScoreDAO {

    /**
     * Save score into database
     */
    public void saveScore(Score score) {

        String sql = "INSERT INTO scores (user, marks) VALUES (?, ?)";

        try (
                Connection conn = DBconn.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, score.getUserName());
            pstmt.setInt(2, score.getScore());
            pstmt.executeUpdate();

            System.out.println("Score saved successfully!");

        } catch (SQLException e) {

            System.out.println("Failed to save score.");
            e.printStackTrace();
        }
    }

    /**
     * Fetch all scores from database, ordered by highest marks first.
     *
     * @return List of Score objects
     */
    public List<Score> getAllScores() {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT user, marks FROM scores ORDER BY marks DESC";

        try (
                Connection conn = DBconn.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String user = rs.getString("user");
                int marks = rs.getInt("marks");
                // String attemptedAt = rs.getString("attempted_at");
                scores.add(new Score(user, marks));
            }

        } catch (SQLException e) {
            System.out.println("Failed to fetch scores.");
            e.printStackTrace();
        }

        return scores;
    }
}