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
    private int[] scores;
    private GameDictionary dict;
    private HashSet<String> allWords;
    private boolean[][] visited;
    private ArrayList<Point> wordCoords;
    @Override
    public void newGame(int size, int numPlayers, String cubeFile, BoggleDictionary dict) throws IOException {
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
        if (word.length() < 4) {
            return 0;
        }
        if (guessed.containsKey(word)) {
            if (guessed.get(word).contains(player)) {
                return 0;
            }
            HashSet<Integer> temp = guessed.get(word);
            temp.add(player);
            guessed.put(word, temp);
            scores[player] += word.length()-3;
            return word.length()-3;
        }
        if (searchType == SearchTactic.SEARCH_DICT) {
            if (dict.contains(word)) {
                if (inGrid(word)) {
                    HashSet<Integer> temp = new HashSet<>();
                    temp.add(player);
                    guessed.put(word, temp);
                    scores[player] += word.length()-3;
                    return word.length()-3;
                }
            }
        }
        else if (searchType == SearchTactic.SEARCH_BOARD) {
            if (inGrid(word)) {
                if (dict.contains(word)) {
                    HashSet<Integer> temp = new HashSet<>();
                    temp.add(player);
                    guessed.put(word, temp);
                    scores[player-1] += word.length()-3;
                    return word.length()-3;
                }
            }
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
        return null;
    }

    @Override
    public void setSearchTactic(SearchTactic tactic) {
        searchType = tactic;
    }

    @Override
    public int[] getScores() {
        return scores;
    }

    private void randomInit(String cubeFile) throws IOException {
        ArrayList<Character> randChars = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(cubeFile));
        String temp = br.readLine();
        while (temp != null) {
            int randIdx = (int)(Math.random()*temp.length());
            randChars.add(temp.charAt(randIdx));
            temp = br.readLine();
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
}
