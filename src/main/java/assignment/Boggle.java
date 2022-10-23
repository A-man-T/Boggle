package assignment;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Boggle {
    static BoggleDictionary dict = new GameDictionary();
    static BoggleGame game = new GameManager();
    public static void main(String[] args) throws IOException {
        dict.loadDictionary("words.txt");

        Scanner scan = new Scanner(System.in);

        System.out.println("Press 's' to start the game. ");
        String s = scan.nextLine();

        if (s.equals("s")) {
            System.out.println("Enter the number of players: ");
            int players = scan.nextInt();
            game.newGame(4, players, "cubes.txt", dict);
            char[][] modGrid = game.getBoard();

            for (int a = 0; a < players; a++) {
                while (true) {
                    System.out.println("Welcome Player " + (a+1) + ", here is your board. Your score is: " + game.getScores()[a]);
                    for (int i = 0; i < game.getBoard().length; i++) {
                        for (int j = 0; j < game.getBoard()[i].length; j++) {
                            modGrid[i][j] = Character.toUpperCase(game.getBoard()[i][j]);
                            System.out.print(modGrid[i][j] + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("Enter 'e' to end the game or enter anything else to continue");
                    String x = scan.next();
                    scan.nextLine();
                    if (x.equals("e")) break;
                    else {
                        System.out.println("Please enter your guess: ");
                        String guess = scan.next();
                        scan.nextLine();
                        int result = game.addWord(guess, a+1);
                        if (result == 0) {
                            System.out.println("You have already guessed that word, please try again.");
                        }
                        else if (result == 3) {
                            System.out.println("Your last guess is not a word, please try again.");
                        }
                        else if (result == 4) {
                            System.out.println("Your last guess was not found in the grid, please try again.");
                        }
                        else if (result == 1 || result == 2) {
                            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                            System.out.println("Good Guess!");
                            for (Point p : game.getLastAddedWord()) {
                                modGrid[p.x][p.y] = Character.toLowerCase(modGrid[p.x][p.y]);
                            }
                            for (int i = 0; i < game.getBoard().length; i++) {
                                for (int j = 0; j < game.getBoard()[i].length; j++) {
                                    System.out.print(modGrid[i][j] + " ");
                                }
                                System.out.println();
                            }
                            System.out.println();
                            System.out.println("Type any character to continue: ");
                            String str = scan.next();
                            scan.nextLine();
                            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        }
                    }
                }
            }
            System.out.println("Final Scores: ");
            for (int i = 0; i < game.getScores().length; i++) {
                System.out.println("Player " + (i+1) + ": " + game.getScores()[i]);
            }
        }
    }
}
