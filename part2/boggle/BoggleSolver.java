/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BoggleSolver {

    private final TrieSETR dictionary = new TrieSETR();
    private final HashSet<String> validWord = new HashSet<>();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (int i = 0; i < dictionary.length; i++) {
            this.dictionary.add(dictionary[i]);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        List<String> validWordClone = new ArrayList<>();
        int n = board.cols() * board.rows();
        for (int i = 0; i < n; i++) {
            // System.out.println(i);
            boolean[] marked = new boolean[n];
            dfs(board, i, "", marked);
        }

        for (String word : this.validWord) {
            validWordClone.add(word);
        }
        this.validWord.clear();
        return validWordClone;
    }

    private void dfs(BoggleBoard board, int square, String cur, boolean[] marked) {
        int row = square / board.cols();
        int col = square % board.cols();

        marked[square] = true;
        char letter = board.getLetter(row, col);
        String temp = cur + letter;
        if (letter == 'Q') {
            temp += "U";
        }

        // System.out.println(temp);

        if (temp.length() >= 3) {
            if (this.dictionary.contains(temp)) {
                this.validWord.add(temp);
            }
            else {
                // stop searching if no words in dictionary with current prefix
                if (!this.dictionary.keysWithPrefix(temp).iterator().hasNext()) {
                    return;
                }
            }
        }

        for (int neighbor : adj(board, square)) {
            if (!marked[neighbor]) {
                dfs(board, neighbor, temp, marked.clone());
            }
        }
    }
    // private void dfs(BoggleBoard board, int v, String curr, int d, boolean[] marked) {
    //     int cols = board.cols();
    //     int rows = board.rows();
    //     marked[v] = true;
    //     String temp = curr + board.getLetter(v / cols, v % cols);
    //
    //     // if the letter is Q, add a U to the last letter
    //     if (board.getLetter(v / cols, v % cols) == 'Q') {
    //         temp = temp + "U";
    //     }
    //
    //     if (d >= 2) {
    //         if (dictionary.contains(temp))
    //             this.validWord.add(temp);
    //         else {
    //             if (!dictionary.keysWithPrefix(temp).iterator().hasNext())
    //                 return;
    //         }
    //     }
    //
    //     // Bag<Integer> adjs = adj(board, v);
    //     for (int i : adj(board, v)) {
    //         if (!marked[i]) {
    //             if (board.getLetter(v / cols, v % cols) == 'Q') {
    //                 dfs(board, i, temp, d + 2, marked.clone());
    //             }
    //             else {
    //                 dfs(board, i, temp, d + 1, marked.clone());
    //             }
    //         }
    //     }
    // }

    private int indicesToInt(BoggleBoard board, int row, int col) {
        int colsPerRow = board.cols();
        return row * colsPerRow + col;
    }

    private Iterable<Integer> adj(BoggleBoard board, int square) {
        int rows = board.rows();
        int cols = board.cols();
        int rowNum = square / cols;
        int colNum = square % cols;
        Bag<Integer> neighbors = new Bag<>();
        if (rowNum - 1 >= 0) neighbors.add(indicesToInt(board, rowNum - 1, colNum)); // top
        if (rowNum + 1 < rows) neighbors.add(indicesToInt(board, rowNum + 1, colNum)); // bottom
        if (colNum - 1 >= 0) neighbors.add(indicesToInt(board, rowNum, colNum - 1)); // left
        if (colNum + 1 < cols) neighbors.add(indicesToInt(board, rowNum, colNum + 1)); // right
        if (rowNum - 1 >= 0 && colNum - 1 >= 0) {
            neighbors.add(indicesToInt(board, rowNum - 1, colNum - 1)); // top left
        }
        if (rowNum - 1 >= 0 && colNum + 1 < cols) {
            neighbors.add(indicesToInt(board, rowNum - 1, colNum + 1)); // top right
        }
        if (rowNum + 1 < rows && colNum - 1 >= 0) {
            neighbors.add(indicesToInt(board, rowNum + 1, colNum - 1)); // bottom left
        }
        if (rowNum + 1 < rows && colNum + 1 < cols) {
            neighbors.add(indicesToInt(board, rowNum + 1, colNum + 1)); // bottom right
        }
        return neighbors;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (this.dictionary.contains(word)) {
            int length = word.length();
            return score(length);
        }
        else {
            return 0;
        }
    }

    private int score(int length) {
        if (length <= 2) return 0;
        if (length <= 4) return 1;
        if (length <= 5) return 2;
        if (length <= 6) return 3;
        if (length <= 7) return 5;
        return 11;

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
