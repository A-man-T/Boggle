package assignment;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class Boggle {
    static BoggleDictionary dict = new GameDictionary();
    static BoggleGame game = new GameManager();
    static HashSet<String> guessed = new HashSet<>();
    public static void main(String[] args) throws IOException {
        dict.loadDictionary("words.txt"); // load the dictionary
        while (true) { // start the game
            Scanner scan = new Scanner(System.in);

            System.out.println("Press 's' to start the game. "); // starts game
            String s = scan.nextLine();

            if (s.equals("s")) {
                System.out.println("Enter the number of players: "); // game has started
                int players = scan.nextInt(); // numPlayers
                game.newGame(4, players, "cubes.txt", dict); // make the game
                //game.setGame(new char[][]{{'E', 'W', 'U', 'F'}, {'L', 'H', 'N', 'R'}, {'E', 'T', 'E', 'S'}, {'S', 'C', 'T', 'O'}});
                // above code used for testing
                char[][] modGrid = game.getBoard(); // modified grid used to highlight found words without changing original grid

                for (int a = 0; a < players; a++) { // each player's turn
                    while (true) {
                        System.out.println("Welcome Player " + (a + 1) + ", here is your board. Your score is: " + game.getScores()[a]);
                        for (int i = 0; i < game.getBoard().length; i++) { // prints grid
                            for (int j = 0; j < game.getBoard()[i].length; j++) {
                                modGrid[i][j] = Character.toUpperCase(game.getBoard()[i][j]);
                                System.out.print(modGrid[i][j] + " ");
                            }
                            System.out.println();
                        }
                        System.out.println("Enter 'e' to end the game or enter anything else to continue"); // end condition / continue
                        String x = scan.next();
                        scan.nextLine();
                        if (x.equals("e")) break;
                        else { // game goes on
                            System.out.println("Please enter your guess: ");
                            String guess = scan.next();
                            scan.nextLine(); // collects player's guess
                            int result = game.addWord(guess, a);
                            if (result == 0) { // invalid word
                                System.out.println("Invalid, please try again.");
                            } else {
                                guessed.add(guess); // adds player's guess to set of guessed words
                                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                                System.out.println("Good Guess! You got " + result + " points!"); // success message
                                for (Point p : game.getLastAddedWord()) {
                                    modGrid[p.x][p.y] = Character.toLowerCase(modGrid[p.x][p.y]); // highlights discovered word
                                }
                                for (int i = 0; i < game.getBoard().length; i++) {
                                    for (int j = 0; j < game.getBoard()[i].length; j++) {
                                        System.out.print(modGrid[i][j] + " ");
                                    }
                                    System.out.println();
                                }
                                System.out.println();
                                System.out.println("Type any character to continue: "); // continue to next turn
                                String str = scan.next();
                                scan.nextLine();
                                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                            }
                        }
                    }
                }
                System.out.println("Final Scores: "); // output final scores of each player
                for (int i = 0; i < game.getScores().length; i++) {
                    System.out.println("Player " + (i + 1) + ": " + game.getScores()[i]);
                }
                // we output both search types for testing purposes, to check if they print out the same words
                System.out.println();
                System.out.println("Unguessed Words:"); // unguessed words in board search
                game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_BOARD);
                for (String m : game.getAllWords()) {
                    if (!guessed.contains(m)) System.out.print(m + ", ");
                }
                System.out.println();
                // commented out for testing purposes
                /*
                System.out.println("Unguessed Words:"); // unguessed words in dict search
                game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_DICT);
                for (String m : game.getAllWords()) {
                    if (!guessed.contains(m)) System.out.print(m + ", ");
                }
                */
                System.out.println();
                System.out.println();
            }
            scan = new Scanner(System.in);

            System.out.println("Would you like to play again (y/n) ?"); // prompts user to play another game
            String b = scan.nextLine();
            if (b == "n") {
                break; // exit condition, everything stops
            }
        }
    }
}
