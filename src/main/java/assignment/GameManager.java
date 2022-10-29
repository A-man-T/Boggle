package assignment;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

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
        grid = new char[size][size];
        this.dict = (GameDictionary) dict;
        searchType = BoggleGame.SEARCH_DEFAULT;
        randomInit(cubeFile);
        scores = new int[numPlayers];
        Arrays.fill(scores, 0);
    }

    @Override
    public char[][] getBoard() {
        return grid;
    }

    @Override
    public int addWord(String word, int player) {
        if (player < 0 || player >= scores.length) {
            System.out.println("Invalid player number");
            return 0;
        }
        if (word.length() < 4) {
            return 0;
        }
        if (guessed.containsKey(word)) {
            if (guessed.get(word.toLowerCase()).contains(player)) {
                return 0;
            }
            HashSet<Integer> temp = guessed.get(word.toLowerCase());
            temp.add(player);
            guessed.put(word.toLowerCase(), temp);
            scores[player] += word.length()-3;
            return word.length()-3;
        }
        if (dict.contains(word) && inGrid(word)) {
            HashSet<Integer> temp = new HashSet<>();
            temp.add(player);
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
    public void setGame(char[][] board) {
        this.grid = Arrays.copyOf(board, board.length);
    }

    @Override
    public Collection<String> getAllWords() {
        if (dict == null) {
            System.err.println("Dictionary not initialized.");
            return allWords;
        }
        allWords = new HashSet<>();
        if (searchType == SearchTactic.SEARCH_BOARD) {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    String s = "";
                    visited = new boolean[grid.length][grid[0].length];
                    ff2(s, i, j);
                }
            }
        }
        else if (searchType == SearchTactic.SEARCH_DICT) {
            for (String s : dict) {
                if (inGrid(s)) {
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
    public int[] getScores() {
        if (scores.length == 0) {
            System.err.println("Invalid number of players, scores has not been instantiated");
        }
        return scores;
    }

    private void randomInit(String cubeFile) throws IOException {
        ArrayList<Character> randChars = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(cubeFile));
        String temp = br.readLine();
        if (temp.length() != 6) {
            System.out.println("Invalid dictionary / cubes are not 6 sides");
            return;
        }
        int count = 0;
        while (temp != null) {
            int randIdx = (int)(Math.random()*temp.length());
            randChars.add(temp.charAt(randIdx));
            temp = br.readLine();
            count++;
        }
        if (count != grid.length*grid.length) {
            System.out.println("Invalid number of cubes");
            return;
        }
        Collections.shuffle(randChars);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = randChars.remove(randChars.size()-1);
            }
        }
    }

    private boolean inGrid(String s) {

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                visited = new boolean[grid.length][grid[0].length];
                wordCoords = new ArrayList<>();
                if (ff(s, i, j, 0, wordCoords)) return true;
            }
        }
        return false;
    }


    private boolean ff(String s, int i, int j, int n, ArrayList<Point> a) {
        if (n == s.length()) return true;
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || visited[i][j]) return false;
        if (s.charAt(n) != grid[i][j] + 32) return false;
        visited[i][j] = true;
        a.add(new Point(i, j));
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (ff(s, i + di, j + dj, n+1, a)) return true;
            }
        }
        return false;
    }

    private void ff2(String s, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || visited[i][j]) return;
        s += Character.toLowerCase(grid[i][j]);
        if (!dict.isPrefix(s)) return;
        if (dict.contains(s) && s.length() >= 4) allWords.add(s);
        visited[i][j] = true;
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                ff2(s, i + di, j + dj);
            }
        }
    }
}
