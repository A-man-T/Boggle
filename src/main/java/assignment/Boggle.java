package assignment;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Boggle {
    static BoggleDictionary dict = new GameDictionary();
    static BoggleGame game = new GameManager();
    public static void main(String[] args) throws IOException {
        dict.loadDictionary("cubes.txt");

        Scanner scan = new Scanner(System.in);

        System.out.println("Press 's' to start the game. ");
        String s = scan.nextLine();

        if (s.equals("s")) {
            System.out.println("Enter the number of players: ");
            int players = scan.nextInt();
            game.newGame(4, players, "words.txt", dict);

            boolean inGame = true;
            while (inGame) {
                for (int i = 0; i < game.getBoard().length; i++) {
                    for (int j = 0; j < game.getBoard()[i].length; j++) {
                        System.out.print(game.getBoard()[j][i] + " ");
                    }
                    System.out.println();
                }
                System.out.println("Enter 'e' to end the game. ");
                inGame = !(scan.nextLine().equals("e"));
            }
        }
    }
}
