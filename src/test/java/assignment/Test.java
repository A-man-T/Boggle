package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class Test {

    @org.junit.jupiter.api.Test
    void invalidDictionary() throws IOException{
        BoggleDictionary dict = new GameDictionary();
        dict.loadDictionary("1.txt");
        BufferedReader br = new BufferedReader(new FileReader("1.txt"));
        String temp = br.readLine();
        while (temp != null) {
            if (!dict.contains(temp)) System.out.println(temp);
            assertTrue(dict.contains(temp));
            temp = br.readLine();
        }
    }


    @org.junit.jupiter.api.Test
    void loadDictionary() throws IOException {
        BoggleDictionary dict = new GameDictionary();
        dict.loadDictionary("dicttest.txt");
        BufferedReader br = new BufferedReader(new FileReader("dicttest.txt"));
        String temp = br.readLine();
        while (temp != null) {
            if (!dict.contains(temp)) System.out.println(temp);
            assertTrue(dict.contains(temp));
            temp = br.readLine();
        }
    }

    @org.junit.jupiter.api.Test
    void iterator() throws IOException {
        BoggleDictionary dict = new GameDictionary();
        dict.loadDictionary("words.txt");
        BufferedReader br = new BufferedReader(new FileReader("words.txt"));
        String temp = br.readLine();
        HashSet<String> words = new HashSet<>();

        while (temp != null) {
            words.add(temp);
            temp = br.readLine();
        }
        for (String s : dict) {
            assertTrue(words.contains(s));
            words.remove(s);
        }
        assertEquals(0, words.size());
    }

    @org.junit.jupiter.api.Test
    void prefixTest() throws IOException{
        BoggleDictionary dict = new GameDictionary();
        dict.loadDictionary("dicttest.txt");
        BufferedReader br = new BufferedReader(new FileReader("dicttest.txt"));
        String temp = br.readLine();
        while (temp != null) {
            for (int i = 0; i < temp.length(); i++) {
                assertTrue(dict.isPrefix(temp.substring(0, i + 1)));
            }
            temp = br.readLine();
        }
    }

    @org.junit.jupiter.api.Test
    void testGame() throws IOException {
        BoggleDictionary dict = new GameDictionary();
        BoggleGame game = new GameManager();
        dict.loadDictionary("testgridtest.tx");
        game.newGame(4, 1, "testgrid.txt", dict);
        assertEquals(game.addWord("aaaa", 0), 1);
        assertEquals(game.getScores()[0], 1);
        assertEquals(game.addWord("aaaaa", 0), 2);
        assertEquals(game.getScores()[0], 3);
        assertEquals(game.addWord("aaaaaa", 0), 3);
        assertEquals(game.getScores()[0], 6);
        assertEquals(game.addWord("aaaaaa", 0), 0);
        assertEquals(game.getScores()[0], 6);
    }

    @org.junit.jupiter.api.Test
    void randomInitTest() throws IOException {
        BoggleDictionary dict = new GameDictionary();
        BoggleGame game = new GameManager();
        dict.loadDictionary("words.txt");
        game.newGame(4, 1, "cubes.txt", dict);
        HashSet<Character> chars = new HashSet<>();
        BufferedReader br = new BufferedReader(new FileReader("cubes.txt"));
        String temp = br.readLine();
        while (temp != null) {
            for (char c : temp.toCharArray()) {
                chars.add(Character.toLowerCase(c));
            }
            temp = br.readLine();
        }
        for (char[] c : game.getBoard()) {
            for (char d : c) {
                assertTrue(chars.contains(Character.toLowerCase(d)));
            }
        }
    }


}