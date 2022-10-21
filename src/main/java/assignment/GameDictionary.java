package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class GameDictionary implements BoggleDictionary{
    TrieNode root;

    @Override
    public void loadDictionary(String filename) throws IOException {
        root = new TrieNode();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String temp = br.readLine();
            while(temp != null) {
                root.insert(temp, root);
                temp = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Invalid filepath");
        }

    }

    @Override
    public boolean isPrefix(String prefix) {
        return root.isPrefix(prefix, root);
    }

    @Override
    public boolean contains(String word) {
        return root.find(word, root);
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }
}
