package app;

import dao.QuestionDAO;
import service.QuizService;

/**
 * Entry point for the Online Quiz System.
 *
 * Current flow (Phase 1 - Questions only):
 *   1. Add a couple of test questions via QuestionDAO
 *   2. Start the interactive quiz via QuizService
 *
 * To run this properly, make sure:
 *   - MySQL is running
 *   - `quizdb` database exists (run SetupDB.java once to create tables + seed data)
 *   - lib/mysql-connector-j-*.jar is on the classpath
 */
public class Main {

    public static void main(String[] args) {

        // --- OPTIONAL: Add a fresh question programmatically ---
        // Uncomment these lines to test the addQuestion() method:
        //
        // QuestionDAO dao = new QuestionDAO();
        // dao.addQuestion("What is the capital of Rajasthan?", "Jaipur");
        // dao.addQuestion("What is the largest planet in the solar system?", "Jupiter");

        // --- Start the Quiz ---
        QuizService quizService = new QuizService();
        quizService.startQuiz();
    }
}
