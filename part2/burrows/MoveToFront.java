/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int CHAR_BITS = 8;
    private static int R = 256;
    private static int[] rank = new int[R]; // index is ascii char value is position
    private static char[] charSeq = new char[R]; // index is postion within sequence, value is char

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        initialize();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int position = rank[c];
            BinaryStdOut.write(position, CHAR_BITS);
            moveToFront(c);
        }
        BinaryStdOut.close();
    }

    private static void moveToFront(char c) {
        int position = rank[c];
        for (int i = position; i > 0; i--) {
            charSeq[i] = charSeq[i - 1];
            rank[charSeq[i]] = i;
        }
        charSeq[0] = c;
        rank[c] = 0;
    }

    private static void initialize() {
        for (int i = 0; i < R; i++) {
            charSeq[i] = (char) i;
            rank[i] = i;
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        initialize();
        while (!BinaryStdIn.isEmpty()) {
            int encoding = BinaryStdIn.readInt(CHAR_BITS);
            char c = charSeq[encoding];
            BinaryStdOut.write(c, CHAR_BITS);
            moveToFront(c);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) MoveToFront.encode();
        if (args[0].equals("+")) MoveToFront.decode();
    }
}
