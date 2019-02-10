/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {

    private String s;
    private int[] indices;

    private class StartIndexComparator implements Comparator<Integer> {

        public int compare(Integer first, Integer second) {
            int sLength = s.length();
            for (int i = 0; i < sLength; i++) {
                if (s.charAt((first + i) % sLength) < s.charAt((second + i) % sLength)) {
                    return -1;
                }
                if (s.charAt((first + i) % sLength) > s.charAt((second + i) % sLength)) {
                    return 1;
                }
            }
            return 0;
        }
    }


    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;
        this.indices = new int[s.length()];
        buildIndexLookup();
    }

    private void buildIndexLookup() {
        Integer[] startIndexArray = new Integer[s.length()];
        for (int i = 0; i < startIndexArray.length; i++) startIndexArray[i] = i;
        Arrays.sort(startIndexArray,
                    (Integer first, Integer second) -> { //lambda comparator function
                        int sLength = s.length();
                        for (int i = 0; i < sLength; i++) {
                            if (s.charAt((first + i) % sLength) < s
                                    .charAt((second + i) % sLength)) {
                                return -1;
                            }
                            if (s.charAt((first + i) % sLength) > s
                                    .charAt((second + i) % sLength)) {
                                return 1;
                            }
                        }
                        return 0;
                    }
        );
        for (int i = 0; i < startIndexArray.length; i++) indices[i] = startIndexArray[i];
    }

    public int length() {
        return s.length();
    }

    public int index(int i) {
        if ((i < 0) || (i > s.length() - 1)) throw new IllegalArgumentException();
        return indices[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("abracadabra");
    }
}
