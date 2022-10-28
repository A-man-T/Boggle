package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

public class GameDictionary implements BoggleDictionary{
    TrieNode root;
    int numwords;

    @Override
    public void loadDictionary(String filename) throws IOException {
        root = new TrieNode();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String temp = br.readLine();
        while(temp != null) {
            root.insert(temp, root);
            numwords++;
            temp = br.readLine();
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
        return new TrieIterator<>(root, numwords);
    }
}

class TrieIterator<String> implements Iterator<String> {
    TrieNode current;
    Stack<TrieNode> path;
    int numwords;
    char lastChar;
    StringBuilder next;
    public TrieIterator(TrieNode root, int numwords) {
        current = root;
        path = new Stack<>();
        path.push(current);
        this.numwords = numwords;
        lastChar = 'a';
        next = new StringBuilder();
    }

    @Override
    public boolean hasNext() {
        return numwords > 0;
    }

    @Override
    public String next() {
        if (!hasNext()) return null;
        numwords--;
        TrieNode root = path.peek();
        if (!isLeaf(root)) {
            traverseDown(root, -1);
            return (String) next.toString();
        }
        do {
            path.pop();
            lastChar = next.charAt(next.length() - 1);
            next.deleteCharAt(next.length()- 1);
            root = path.peek();
        } while (getChild(root, lastChar-'a') == -1);
        traverseDown(root,lastChar-'a');
        return (String) next.toString();
    }
    private int getChild(TrieNode root, int start) {
        if (start + 1 >= 26) return -1;
        for (int i = start+1; i < 26; i++) {
            if (root.children[i] != null) {
                return i;
            }
        }
        return -1;
    }
    private void traverseDown(TrieNode root, int start){
        do {
            int x = getChild(root, start);
            root = root.children[x];
            next.append((char)(x + 'a'));
            path.push(root);
            start = -1;
        } while(!root.end);
    }

    private boolean isLeaf(TrieNode node) {
        for (int i = 0; i < node.children.length; i++) {
            if (node.children[i] != null) {
                return false;
            }
        }
        return true;
    }


}
