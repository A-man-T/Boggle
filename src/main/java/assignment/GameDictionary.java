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
            root.insert(temp, root); // loads every word from the dictionary file into the trie.
            numwords++;
            temp = br.readLine();
        }
        if (numwords == 0) {
            System.err.println("Empty dictionary provided"); // lets user know if provided dict is empty
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
        path = new Stack<>(); // contains path traversed to far
        path.push(current); // adds root
        this.numwords = numwords; // total words in the trie
        lastChar = 'a';
        next = new StringBuilder(); // maintains the characters from the path
    }

    @Override
    public boolean hasNext() {
        return numwords > 0; // while the trie still has words
    }

    @Override
    public String next() {
        if (!hasNext()) return null;
        numwords--;
        TrieNode root = path.peek(); // root is the pointer to current node, can be thought of as curr
        if (!isLeaf(root)) { // as long as you're not at the bottom of the trie, continue
            traverseDown(root, -1);
            return (String) next.toString();
        }
        do {
            path.pop(); // go up one level
            lastChar = next.charAt(next.length() - 1); // set last character to the character of the node that was just popped
            next.deleteCharAt(next.length()- 1); // remove that character from the char stack
            root = path.peek(); // update current node pointer
        } while (getChild(root, lastChar-'a') == -1);
        traverseDown(root,lastChar-'a'); // look at the next child
        return (String) next.toString();
    }
    private int getChild(TrieNode root, int start) { // returns the first child of a given node from a starting index to the end of the child array
        if (start + 1 >= 26) return -1;
        for (int i = start+1; i < 26; i++) {
            if (root.children[i] != null) {
                return i;
            }
        }
        return -1;
    }
    private void traverseDown(TrieNode root, int start){ // go down the trie until a word end is found
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
