package model;

/**
 * Represents a single quiz question fetched from the database.
 */
public class Question {

    private int id;
    private String question;
    private String answer;

    // Constructor
    public Question(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "Q" + id + ": " + question;
    }
}
