package app;

import dao.ScoreDAO;
import model.Score;
import service.QuizService;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Online Quiz System.
 *
 * Flow:
 *   1. Ask the user their name
 *   2. Run the interactive quiz via QuizService
 *   3. Save the final score to DB via ScoreDAO
 *   4. Display all past scores (leaderboard)
 *
 * To run this properly, make sure:
 *   - MySQL is running
 *   - `quiz_db` database exists (run SetupDB.java once to create tables + seed data)
 *   - lib/mysql-connector-j-*.jar is on the classpath
 */
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ScoreDAO scoreDAO = new ScoreDAO();

        // 1. Ask for player name
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine().trim();

        // 2. Start the quiz and get the score
        QuizService quizService = new QuizService();
        int finalScore = quizService.startQuiz();

        // 3. Save score to database
        Score score = new Score(playerName, finalScore);
        scoreDAO.saveScore(score);
        System.out.println("Your score has been saved to the database!");

        // 4. Show leaderboard (all past scores, ordered by highest marks)
        System.out.println("\n========== LEADERBOARD ==========");
        List<Score> allScores = scoreDAO.getAllScores();
        if (allScores.isEmpty()) {
            System.out.println("No scores recorded yet.");
        } else {
            int rank = 1;
            for (Score s : allScores) {
                System.out.println("#" + rank + " " + s);
                rank++;
            }
        }
        System.out.println("=================================\n");

        scanner.close();
    }
}
