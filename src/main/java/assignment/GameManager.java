package assignment;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class GameManager implements BoggleGame{

    @Override
    public void newGame(int size, int numPlayers, String cubeFile, BoggleDictionary dict) throws IOException {

    }

    @Override
    public char[][] getBoard() {
        return new char[0][];
    }

    @Override
    public int addWord(String word, int player) {
        return 0;
    }

    @Override
    public List<Point> getLastAddedWord() {
        return null;
    }

    @Override
    public void setGame(char[][] board) {

    }

    @Override
    public Collection<String> getAllWords() {
        return null;
    }

    @Override
    public void setSearchTactic(SearchTactic tactic) {

    }

    @Override
    public int[] getScores() {
        return new int[0];
    }
}
