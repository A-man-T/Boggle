package assignment;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class GameManager implements BoggleGame{
    private char[][] grid;
    private SearchTactic searchType;
    private HashMap<String, HashSet<Integer>> guessed = new HashMap<>();
    private int[] scores = new int[0];
    private GameDictionary dict;
    private HashSet<String> allWords = new HashSet<>();
    private boolean[][] visited;
    private ArrayList<Point> wordCoords;
    @Override
    public void newGame(int size, int numPlayers, String cubeFile, BoggleDictionary dict) throws IOException {
        if (size <= 0) {
            System.out.println("Invalid input dimension");
            return;
        }
        if (numPlayers <= 0) {
            System.out.println("Invalid number of players.");
            return;
        }
        grid = new char[size][size]; // init grid
        this.dict = (GameDictionary) dict;
        searchType = BoggleGame.SEARCH_DEFAULT;
        randomInit(cubeFile); // populate grid
        scores = new int[numPlayers];
        Arrays.fill(scores, 0);
    }

    @Override
    public char[][] getBoard() {
        return grid;
    }

    @Override
    public int addWord(String word, int player) {
        if (player < 0 || player >= scores.length) { // invalid player number
            System.out.println("Invalid player number");
            return 0;
        }
        for (char c : word.toCharArray()) {
            if (!Character.isLetter(c)) {
                System.err.println("Please ensure your word is made up of letters");
                return 0;
            }
        }
        if (word.length() < 4) { // word too short
            return 0;
        }
        if (guessed.containsKey(word.toLowerCase())) { // word has been guessed
            if (guessed.get(word.toLowerCase()).contains(player)) {
                return 0; // word has already been guessed by this player
            }
            HashSet<Integer> temp = guessed.get(word.toLowerCase());
            temp.add(player);
            guessed.put(word.toLowerCase(), temp);
            scores[player] += word.length()-3; // word has already been guessed but NOT by this player, it is added to score
            return word.length()-3;
        }
        if (dict.contains(word) && inGrid(word.toLowerCase())) { // word has not been guessed
            HashSet<Integer> temp = new HashSet<>();
            temp.add(player); // added to this player's score
            guessed.put(word.toLowerCase(), temp);
            scores[player] += word.length()-3;
            return word.length()-3;
        }
        return 0;
    }

    @Override
    public List<Point> getLastAddedWord() {
        return wordCoords;
    }

    @Override
    public void setGame(char[][] board) { // sets each char of current board to the new board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                grid[i][j] = board[i][j];
            }
        }
    }

    @Override
    public Collection<String> getAllWords() {
        if (dict == null) {
            System.err.println("Dictionary not initialized.");
            return allWords;
        }
        allWords = new HashSet<>(); // output collection of choice: HashSet
        if (searchType == SearchTactic.SEARCH_BOARD) { // board search
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    String s = "";
                    visited = new boolean[grid.length][grid[0].length];
                    ff2(s, i, j); // flood fill through the board (dfs) find all valid permutations of adjacent characters
                }
            }
        }
        else if (searchType == SearchTactic.SEARCH_DICT) { // dict search
            for (String s : dict) { // iterate through dict
                if (inGrid(s) && s.length() >= 4) { // check if it exists in the grid and is of the right length
                    allWords.add(s);
                }
            }
        }
        return allWords;
    }

    @Override
    public void setSearchTactic(SearchTactic tactic) {
        searchType = tactic;
    }

    @Override
    public int[] getScores() { // returns score array
        if (scores.length == 0) {
            System.err.println("Invalid number of players, scores has not been instantiated");
        }
        return scores;
    }

    private void randomInit(String cubeFile) throws IOException { // populates grid randomly from cubefile
        ArrayList<Character> randChars = new ArrayList<>(); // adds a random char from each cube
        BufferedReader br = new BufferedReader(new FileReader(cubeFile));
        String temp = br.readLine();
        if (temp.length() != 6) { // cubes do not have 6 sides
            System.out.println("Invalid dictionary / cubes are not 6 sides");
            return;
        }
        int count = 0;
        while (temp != null) {
            int randIdx = (int)(Math.random()*temp.length()); // randomly choose a face of the cube
            randChars.add(temp.charAt(randIdx)); // add chosen char
            temp = br.readLine();
            count++;
        }
        if (count != grid.length*grid.length) { // checks if number of cubes is not square
            System.out.println("Invalid number of cubes");
            return;
        }
        Collections.shuffle(randChars); // randomize order of characters in the array
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = randChars.remove(randChars.size()-1); // populate grid with random characters
            }
        } // this method has 2 elements of randomness: randomly choosing a letter from each cube, and randomizing the order of those latters
    }

    private boolean inGrid(String s) { // checks if a given word is present on the board

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                visited = new boolean[grid.length][grid[0].length]; // maintain visited tiles
                wordCoords = new ArrayList<>(); // point array of found word
                if (ff(s, i, j, 0, wordCoords)) { // flood fill search
                    return true;
                }
            }
        }
        return false;
    }


    private boolean ff(String s, int i, int j, int n, ArrayList<Point> a) { //. flood fill search for a specific word
        if (n == s.length()) return true; // exit condition: word is found
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || visited[i][j]) return false; // idx out of bounds / already checked this tile
        if (s.charAt(n) != Character.toLowerCase(grid[i][j])) return false; // character mismatch, no need to search further
        visited[i][j] = true; // mark current tile as visited
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                // search all 8 adjacent tiles for the NEXT char
                if (ff(s, i + di, j + dj, n+1, a)){
                    a.add(new Point(i, j)); // add the current char to the point[]
                    return true;
                }
            }
        }
        visited[i][j] = false; // reset visited for next path pass, so words aren't skipped
        return false;
    }

    private void ff2(String s, int i, int j) { // find all valid permutations of chars in the grid
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || visited[i][j]) return; // idx out of bounds / tile alr visited
        s += Character.toLowerCase(grid[i][j]); // add current character to string
        if (!dict.isPrefix(s)) { // prune permutations based on whether current generated string is a valid prefix
            return;
        }
        visited[i][j] = true; // mark current tile as visited
        if (dict.contains(s) && s.length() >= 4) allWords.add(s); // valid word found
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                // search all 8 adjacent tiles
                ff2(s, i + di, j + dj);
            }
        }
        s = "" + s.charAt(s.length() - 1); // remove last char if nothing was satisfied
        visited[i][j] = false; // unmark last tile as visited, so words are not skipped.
    }
}
