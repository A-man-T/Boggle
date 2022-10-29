package assignment;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Boggle {
    static BoggleDictionary dict = new GameDictionary();
    static BoggleGame game = new GameManager();
    static HashSet<String> guessed = new HashSet<>();
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
                        int result = game.addWord(guess, a);
                        if (result == 0) {
                            System.out.println("Invalid, please try again.");
                        }
                        else {
                            guessed.add(guess);
                            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                            System.out.println("Good Guess! You got " + result + " points!");
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
            System.out.println();
            System.out.println("Unguessed Words:");
            game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_DICT);
            for (String m : game.getAllWords()) {
                if (!guessed.contains(m) && m.length() >= 4) System.out.print(m + ", ");
            }
        }
    }
}
