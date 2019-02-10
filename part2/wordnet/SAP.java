/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return findShortestAncestor(v, w, "length");
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return findShortestAncestor(v, w, "ancestor");

    }

    private int findShortestAncestor(int v, int w, String result) {
        BreadthFirstDirectedPaths vBfdp = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths wBfdp = new BreadthFirstDirectedPaths(this.G, w);
        int shortestDist = Integer.MAX_VALUE;
        int shortestAnc = 0;
        for (int i = 0; i < this.G.V(); i++) {
            if (vBfdp.hasPathTo(i) && wBfdp.hasPathTo(i)) {
                int potentialShortestDist = vBfdp.distTo(i) + wBfdp.distTo(i);
                if (potentialShortestDist < shortestDist) {
                    shortestDist = potentialShortestDist;
                    shortestAnc = i;
                }
            }
        }
        if (result.equals("length")) {
            return (shortestDist != Integer.MAX_VALUE) ? shortestDist : -1;
        }
        else {
            return (shortestDist != Integer.MAX_VALUE) ? shortestAnc : -1;
        }
    }

    private boolean hasNullInput(Iterable<Integer> vertex) {
        for (Integer v : vertex) {
            if (v == null) return true;
        }
        return false;
    }

    private int findShortestAncestor(Iterable<Integer> v, Iterable<Integer> w,
                                     String result) {
        if (hasNullInput(v) || hasNullInput(w)) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths vBfdp = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths wBfdp = new BreadthFirstDirectedPaths(this.G, w);
        int shortestDist = Integer.MAX_VALUE;
        int shortestAnc = 0;
        for (int i = 0; i < this.G.V(); i++) {
            if (vBfdp.hasPathTo(i) && wBfdp.hasPathTo(i)) {
                int potentialShortestDist = vBfdp.distTo(i) + wBfdp.distTo(i);
                if (potentialShortestDist < shortestDist) {
                    shortestDist = potentialShortestDist;
                    shortestAnc = i;
                }
            }
        }
        if (result.equals("length")) {
            return (shortestDist != Integer.MAX_VALUE) ? shortestDist : -1;
        }
        else {
            return (shortestDist != Integer.MAX_VALUE) ? shortestAnc : -1;
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        return findShortestAncestor(v, w, "length");
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        return findShortestAncestor(v, w, "ancestor");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
