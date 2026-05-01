package model;

public class Score {

    private String userName;
    private int score;
    private String attemptedAt; // stores timestamp from DB (optional, can be null)

    // Constructor used when saving (no timestamp yet)
    public Score(String userName, int score) {
        this.userName = userName;
        this.score = score;
        this.attemptedAt = null;
    }

    // Constructor used when reading from DB (includes timestamp)
    public Score(String userName, int score, String attemptedAt) {
        this.userName = userName;
        this.score = score;
        this.attemptedAt = attemptedAt;
    }

    // Getter for userName
    public String getUserName() {
        return userName;
    }

    // Setter for userName
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter for score
    public int getScore() {
        return score;
    }

    // Setter for score
    public void setScore(int score) {
        this.score = score;
    }

    // Getter for attemptedAt
    public String getAttemptedAt() {
        return attemptedAt;
    }

    // Display score details
    @Override
    public String toString() {
        if (attemptedAt != null) {
            return "Player: " + userName + " | Score: " + score + " | Attempted At: " + attemptedAt;
        }
        return "Player: " + userName + " | Score: " + score;
    }
}