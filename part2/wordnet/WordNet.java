/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {
    private final HashMap<String, Bag<Integer>> words;
    private final List<String> synsetList;
    private final Digraph dg;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        this.words = new HashMap<String, Bag<Integer>>();
        synsetList = new ArrayList<>();
        this.dg = new Digraph(parseSynsets(synsets));
        parseHypernyms(hypernyms);

        // throw an error if not a DAG
        DirectedCycle dc = new DirectedCycle(this.dg);
        if (dc.hasCycle()) throw new IllegalArgumentException("Graph must not have a cycle");

        // check for more than one root
        boolean foundRoot = false;
        for (int i = 0; i < synsetList.size(); i++) {
            if (this.dg.outdegree(i) == 0) {
                if (foundRoot) {
                    throw new IllegalArgumentException("Found multiple roots");
                }
                else {
                    foundRoot = true;
                }
            }
        }

    }

    private int parseSynsets(String synsets) {
        In synsetsIn = new In(synsets);
        int synsetsNum = 0;

        while (!synsetsIn.isEmpty()) {
            String line = synsetsIn.readLine();
            String[] fields = line.split(",");
            String[] nouns = fields[1].split(" ");
            int synsetId = Integer.parseInt(fields[0]);
            this.synsetList.add(fields[1]);

            for (String noun : nouns) {
                if (!this.words.containsKey(noun)) {
                    this.words.put(noun, new Bag<Integer>());
                }
                this.words.get(noun).add(synsetId);
            }
            synsetsNum++;
        }
        return synsetsNum;
    }

    private void parseHypernyms(String hypernyms) {
        In hypernymsIn = new In(hypernyms);
        while (!hypernymsIn.isEmpty()) {
            String line = hypernymsIn.readLine();
            String[] fields = line.split(",");

            // first field is synsetId string
            // hypernyms starts on second field
            int synsetId = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                this.dg.addEdge(synsetId, Integer.parseInt(fields[i]));
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.words.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return this.words.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null ||
                !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP sap = new SAP(this.dg);
        Iterable<Integer> synsetsA = this.words.get(nounA);
        Iterable<Integer> synsetsB = this.words.get(nounB);
        return sap.length(synsetsA, synsetsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null ||
                !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP sap = new SAP(this.dg);
        Iterable<Integer> synsetsA = this.words.get(nounA);
        Iterable<Integer> synsetsB = this.words.get(nounB);
        System.out.println(this.words.get(nounA).size());
        int shortestAncestorId = sap.ancestor(synsetsA, synsetsB);
        // System.out.println(shortestAncestorId);
        return this.synsetList.get(shortestAncestorId);
    }

    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet net = new WordNet(synsets, hypernyms);
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int length = net.distance(v, w);
            String ancestor = net.sap(v, w);
            StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
        }

    }
}
