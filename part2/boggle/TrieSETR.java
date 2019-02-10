/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class TrieSETR implements Iterable<String> {
    private static final int R = 26;        // extended ASCII

    private Node root;      // root of trie
    private int N;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    /**
     * Initializes an empty set of strings.
     */
    public TrieSETR() {
    }

    /**
     * Does the set contain the given key?
     *
     * @param key the key
     * @return <tt>true</tt> if the set contains <tt>key</tt> and
     * <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 65], key, d + 1);
    }

    /**
     * Adds the key to the set if it is not already present.
     *
     * @param key the key to add
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void add(String key) {
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) N++;
            x.isString = true;
        }
        else {
            char c = key.charAt(d);
            x.next[c - 65] = add(x.next[c - 65], key, d + 1);
        }
        return x;
    }

    /**
     * Returns the number of strings in the set.
     *
     * @return the number of strings in the set
     */
    public int size() {
        return N;
    }

    /**
     * Is the set empty?
     *
     * @return <tt>true</tt> if the set is empty, and <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns all of the keys in the set, as an iterator. To iterate over all of the keys in a set
     * named <tt>set</tt>, use the foreach notation: <tt>for (Key key : set)</tt>.
     *
     * @return an iterator to all of the keys in the set
     */
    public Iterator<String> iterator() {
        return keysWithPrefix("").iterator();
    }

    /**
     * Returns all of the keys in the set that start with <tt>prefix</tt>.
     *
     * @param prefix the prefix
     * @return all of the keys in the set that start with <tt>prefix</tt>, as an iterable
     */
    public Queue<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.isString) results.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Removes the key from the set if the key is present.
     *
     * @param key the key
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.isString) N--;
            x.isString = false;
        }
        else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d + 1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.isString) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }


    /**
     * Unit tests the <tt>TrieSETR</tt> data type.
     */
    public static void main(String[] args) {
        TrieSETR set = new TrieSETR();
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            set.add(key);
        }

        // print results
        if (set.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : set) {
                StdOut.println(key);
            }
            StdOut.println();
        }

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : set.keysWithPrefix("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shortening\"):");
        for (String s : set.keysWithPrefix("shortening"))
            StdOut.println(s);
        StdOut.println();
    }
}
