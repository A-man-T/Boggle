package assignment;

import java.util.ArrayList;
import java.util.Arrays;

public class TrieNode {
    public TrieNode[] children;
    public boolean end;
    public char val;

    // TrieNode constructor, creates TrieNode
    public TrieNode() {
        end = false;
        children = new TrieNode[26];
        Arrays.fill(children, null);
    }

    // adds a word into the trie
    public void insert(String s, TrieNode root) {
        s = s.toLowerCase();
        TrieNode traverse = root;
        for (int i = 0; i < s.length(); i++) {
            int pos = s.charAt(i)-'a';
            if(traverse.children[pos] ==  null) {
                traverse.children[pos] = new TrieNode();
            }
            traverse = traverse.children[pos];
        }
        traverse.end = true;
    }

    // finds word in a trie
    public boolean find(String s, TrieNode root) {
        s = s.toLowerCase();
        TrieNode traverse = root;
        for (int i = 0; i < s.length(); i++) {
            int pos = s.charAt(i)-'a';
            if (traverse.children[pos] == null) {
                return false;
            }
            traverse = traverse.children[pos];
        }
        return traverse.end;
    }

    // checks if a substring is a prefix of another word in the trie
    public boolean isPrefix(String s, TrieNode root) {
        if (s.length() == 0) {
            return true;
        }
        s = s.toLowerCase();
        TrieNode traverse = root;
        for (int i = 0; i < s.length(); i++) {
            int pos = s.charAt(i)-'a';
            if (traverse.children[pos] == null) {
                return false;
            }
            traverse = traverse.children[pos];
        }
        return true;
    }
}
