package app;

import dao.ScoreDAO;
import model.Score;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ScoreDAO dao = new ScoreDAO();
        char choice;

        do {
            // User input
            System.out.print("Enter User Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Marks: ");
            int marks = sc.nextInt();

            sc.nextLine();

            // Create Score object
            Score s1 = new Score(name, marks);

            // Save score
            dao.saveScore(s1);

            // Ask user to continue
            System.out.print("Add another score? (y/n): ");
            choice = sc.next().charAt(0);
            sc.nextLine();

        } while (choice == 'y' || choice == 'Y');
        System.out.println("Program Ended.");
        sc.close();
    }
}