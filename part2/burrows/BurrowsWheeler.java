/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {
    private static final int CHAR_BITS = 8;
    private static final int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < s.length(); i++) {
            int index = (circularSuffixArray.index(i) + circularSuffixArray.length() - 1) %
                    circularSuffixArray.length();
            BinaryStdOut.write(s.charAt(index), CHAR_BITS);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] charArray = BinaryStdIn.readString().toCharArray();
        char[] sortedCharArray = charArray.clone();
        Arrays.sort(sortedCharArray);
        int[] next = createNextArray(charArray);
        for (int i = 0, charPos = first; i < charArray.length; i++, charPos = next[charPos]) {
            char originalChar = sortedCharArray[charPos];
            BinaryStdOut.write(originalChar);
        }
        BinaryStdOut.close();
    }

    private static int[] createNextArray(char[] charArray) {
        // If the jth original suffix (original string, shifted j characters to the left)
        // is the ith row in the sorted order, we define next[i] to be the row in the sorted
        // order where the (j + 1)st original suffix appears
        int[] next = new int[charArray.length];
        int[] cumArray = new int[R + 1];
        for (char c : charArray) cumArray[c + 1]++; // increment counter
        for (int i = 0; i < R; i++)
            cumArray[i + 1] += cumArray[i]; // calculate cumuative to get position
        for (int i = 0; i < charArray.length; i++) {
            char character = charArray[i]; // ith char in unsorted array
            next[cumArray[character]++] = i;
        }
        return next;
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) BurrowsWheeler.transform();
        if (args[0].equals("+")) BurrowsWheeler.inverseTransform();
    }
}
