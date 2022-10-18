package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GameDictionary implements BoggleDictionary{
    private HashSet<String> words = new HashSet<>();
    private ArrayList<String> words2 = new ArrayList<>();

    @Override
    public void loadDictionary(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String temp = br.readLine();
        while (temp!= null) {
            words.add(temp);
            words2.add(temp);
            temp = br.readLine();
        }
    }

    @Override
    public boolean isPrefix(String prefix) {
        for (int i = 0; i < words2.size(); i++) {
            if (prefix.equals(words2.get(i).substring(0, prefix.length()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(String word) {
        return words.contains(word);
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }
}
