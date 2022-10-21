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
        if (guessed.containsKey(word)) {
            if (guessed.get(word).contains(player)) {
                return 0;
            }
            HashSet<Integer> temp = guessed.get(word);
            temp.add(player);
            guessed.put(word, temp);
            scores[player] += word.length()-3;
            return 1;
        }
        if (searchType == SearchTactic.SEARCH_DICT) {
            if (dict.contains(word)) {
                if (inGrid(word)) {
                    HashSet<Integer> temp = new HashSet<>();
                    temp.add(player);
                    guessed.put(word, temp);
                    scores[player] += word.length()-3;
                    return 2;
                }
            }
        }
        else if (searchType == SearchTactic.SEARCH_BOARD) {
            if (inGrid(word)) {
                if (dict.contains(word)) {
                    HashSet<Integer> temp = new HashSet<>();
                    temp.add(player);
                    guessed.put(word, temp);
                    scores[player] += word.length()-3;
                    return 2;
                }
            }
        }
        return -1;
    }

    @Override
    public List<Point> getLastAddedWord() {
        return null;
    }

    @Override
    public void setGame(char[][] board) {
        this.grid = Arrays.copyOf(board, board.length);
    }

    @Override
    public Collection<String> getAllWords() {
        for ()
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
        int i = 0;
        int j = 0;
        while (ff(s, 0, new Point(j, i))) {
            i++;
            if (i == grid.length-1) {
                i = 0;
                j++;
            }
        }
        return !((4*j)+i == 16);
    }

    private boolean ff(String s, int n, Point pos) {
        if (pos.x < 0 || pos.x >= grid.length || pos.y < 0 || pos.y >= grid.length) {
            return true;
        }
        if (s.charAt(n)== grid[pos.y][pos.x] && n == s.length()-1) return false;
        if (s.charAt(n) == grid[pos.y][pos.x]) {
            ff (s, n+1, new Point(pos.x+1, pos.y));
            ff (s, n+1, new Point(pos.x-1, pos.y));
            ff (s, n+1, new Point(pos.x+1, pos.y+1));
            ff (s, n+1, new Point(pos.x+1, pos.y-1));
            ff (s, n+1, new Point(pos.x, pos.y+1));
            ff (s, n+1, new Point(pos.x, pos.y-1));
            ff (s, n+1, new Point(pos.x-1, pos.y-1));
            ff (s, n+1, new Point(pos.x-1, pos.y+1));
        }
        return true;
    }
}
