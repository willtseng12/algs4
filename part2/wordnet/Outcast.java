/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[] distances = new int[nouns.length];
        for (int i = 0; i < nouns.length - 1; i++) {
            for (int j = i; j < nouns.length; j++) {
                int distance = this.wordNet.distance(nouns[i], nouns[j]);
                distances[i] += distance;
                distances[j] += distance;
            }
        }
        int maxId = 0;
        int maxDistance = 0;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] > maxDistance) {
                maxDistance = distances[i];
                maxId = i;
            }
        }
        return nouns[maxId];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
