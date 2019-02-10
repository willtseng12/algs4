/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private boolean isSolvable;
    private final Stack<Board> solutionTrace = new Stack<>();

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode previousNode;
        private int moves;

        public SearchNode(Board board, SearchNode previousNode) {
            this.board = board;
            this.previousNode = previousNode;
            if (previousNode == null) {
                this.moves = 0;
            }
            else {
                this.moves = previousNode.moves + 1;
            }
        }

        @Override
        public int compareTo(SearchNode other) {
            return (this.board.manhattan() - other.board.manhattan()) +
                    (this.moves - other.moves);
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();
        SearchNode initialNode = new SearchNode(initial, null);
        SearchNode initialNodeTwin = new SearchNode(initial.twin(), null);
        pq.insert(initialNode);
        pqTwin.insert(initialNodeTwin);

        while (true) {

            SearchNode minNode = pq.delMin();
            SearchNode minNodeTwin = pqTwin.delMin();

            if (minNodeTwin.board.isGoal()) {
                this.isSolvable = false;
                break;
            }

            if (minNode.board.isGoal()) {
                this.isSolvable = true;
                createSolutionTrace(minNode);
                break;
            }

            for (Board neighbor : minNode.board.neighbors()) {
                if (minNode.previousNode == null) {
                    pq.insert(new SearchNode(neighbor, minNode));
                }
                else if (!neighbor.equals(minNode.previousNode.board)) {
                    pq.insert(new SearchNode(neighbor, minNode));
                }
            }

            for (Board neighbor : minNodeTwin.board.neighbors()) {
                if (minNodeTwin.previousNode == null) {
                    pqTwin.insert(new SearchNode(neighbor, minNodeTwin));
                }
                else if (!neighbor.equals(minNodeTwin.previousNode.board)) {
                    pqTwin.insert(new SearchNode(neighbor, minNodeTwin));
                }
            }
        }
    }

    private void createSolutionTrace(SearchNode node) {
        SearchNode currentNode = node;
        while (currentNode != null) {
            this.solutionTrace.push(currentNode.board);
            currentNode = currentNode.previousNode;
        }
    }

    public boolean isSolvable() {
        return this.isSolvable;
    }

    public int moves() {
        if (isSolvable()) {
            return solutionTrace.size() - 1;
        }
        else {
            return -1;
        }
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return solutionTrace;
    }

    public static void main(String[] args) {
        // In in = new In(args[0]);
        // int n = in.readInt();
        // int[][] blocks = new int[n][n];
        // for (int i = 0; i < n; i++)
        //     for (int j = 0; j < n; j++)
        //         blocks[i][j] = in.readInt();
        // Board initial = new Board(blocks);
        //
        // // solve the puzzle
        // Solver solver = new Solver(initial);
        //
        // // print solution to standard output
        // if (!solver.isSolvable())
        //     StdOut.println("No solution possible");
        // else {
        //     StdOut.println("Minimum number of moves = " + solver.moves());
        //     for (Board board : solver.solution())
        //         StdOut.println(board);
        // }
    }
}
