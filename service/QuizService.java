package service;

import dao.QuestionDAO;
import model.Question;

import java.util.List;
import java.util.Scanner;

/**
 * Service layer that contains the quiz business logic.
 * Fetches questions, prompts the user, and calculates the score.
 */
public class QuizService {

    private final QuestionDAO questionDAO;

    public QuizService() {
        this.questionDAO = new QuestionDAO();
    }

    /**
     * Runs the interactive quiz in the console.
     * Fetches all questions, prompts for answers, and returns the final score.
     *
     * @return final score achieved by the user
     */
    public int startQuiz() {
        List<Question> questions = questionDAO.getAllQuestions();

        if (questions.isEmpty()) {
            System.out.println("No questions found in the database. Please run SetupDB first.");
            return 0;
        }

        Scanner scanner = new Scanner(System.in);
        int score = 0;

        System.out.println("\n========== ONLINE QUIZ ==========");
        System.out.println("Total Questions: " + questions.size());
        System.out.println("=================================\n");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);

            System.out.println("Q" + (i + 1) + ": " + q.getQuestion());
            System.out.print("Your Answer: ");
            String userAnswer = scanner.nextLine().trim();

            // Case-insensitive comparison
            if (userAnswer.equalsIgnoreCase(q.getAnswer())) {
                System.out.println("✓ Correct!\n");
                score++;
            } else {
                System.out.println("✗ Wrong! Correct answer: " + q.getAnswer() + "\n");
            }
        }

        System.out.println("=================================");
        System.out.println("Quiz Over! Your Score: " + score + "/" + questions.size());
        System.out.println("=================================\n");

        // Note: Do NOT close scanner here — it closes System.in globally.
        // The caller (Main) should close it after saving the score.
        return score;
    }
}
