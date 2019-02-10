/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int SPACE = 0;
    private final int[][] blocks;
    private final int manhattan;

    public Board(int[][] blocks) {

        if (blocks == null) throw new IllegalArgumentException();

        this.blocks = new int[blocks.length][blocks.length];
        int totalDistanceOff = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                this.blocks[i][j] = blocks[i][j];

                // calculate the manhattan distance
                int number = this.blocks[i][j];
                if (number != SPACE) {
                    int[] indices = expectedIndices(number);
                    int xDistance = Math.abs(indices[0] - i);
                    int yDistance = Math.abs(indices[1] - j);
                    totalDistanceOff += xDistance + yDistance;
                }
            }
        }
        this.manhattan = totalDistanceOff;
    }

    /**
     * return n of a n-by-n block
     *
     * @return dimension of a n-by-n block
     */
    public int dimension() {
        return blocks.length;
    }

    /**
     * calculate the number of blocks out of place
     *
     * @return number of blocks out of place
     */
    public int hamming() {
        int numOutOfPlace = 0;
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks.length; j++) {

                if (this.blocks[i][j] != SPACE &&
                        this.blocks[i][j] != expectedNum(i, j)) numOutOfPlace++;
            }
        }
        return numOutOfPlace; // last row is suppose to be blank anyways
    }

    /**
     * calculate the expected number at the block if the board is completed
     *
     * @param i row index
     * @param j column index
     * @return expected number if board is in order
     */
    private int expectedNum(int i, int j) {
        return (dimension() * i + j + 1);
    }

    /**
     * calculates the expected row and column index of the number  if the board is completed
     *
     * @param num a nono-zero number that belongs to a n-by-n board
     * @return int array of length 2 (row index and column index)
     */
    private int[] expectedIndices(int num) {
        int[] indices = new int[2];
        indices[0] = (num - 1) / dimension();
        indices[1] = num - 1 - indices[0] * dimension();
        return indices;
    }

    public int manhattan() {
        // int totalDistanceOff = 0;
        // for (int i = 0; i < this.blocks.length; i++) {
        //     for (int j = 0; j < this.blocks.length; j++) {
        //         int number = this.blocks[i][j];
        //         if (number != SPACE) {
        //             int[] indices = expectedIndices(number);
        //             int xDistance = Math.abs(indices[0] - i);
        //             int yDistance = Math.abs(indices[1] - j);
        //             totalDistanceOff += xDistance + yDistance;
        //         }
        //     }
        // }
        // return totalDistanceOff;
        return this.manhattan;
    }

    /**
     * @return boolean whether the board is completed
     */
    public boolean isGoal() {
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks.length; j++) {
                // System.out.println((!(i == dimension() - 1 && j == dimension() - 1)));
                if ((!(i == dimension() - 1 && j == dimension() - 1)) &&
                        expectedNum(i, j) != this.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return a new board with two non-zero block values switched
     */
    public Board twin() {
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks.length; j++) {
                if (this.blocks[i][j] != SPACE && this.blocks[i + 1][j] != SPACE) {
                    return new Board(swap(i, j, i + 1, j));
                }
            }
        }
        throw new RuntimeException();
    }

    /**
     * create a new array of blocks with two non-zero block value switched
     *
     * @param x1 x-index of first block
     * @param y1 y-index of first block
     * @param x2 x-index of second block
     * @param y2 y-index of second block
     * @return new block array with the item in the two indices switched
     */
    private int[][] swap(int x1, int y1, int x2, int y2) {
        int[][] twinBlocks = copy(this.blocks);
        int tmp = twinBlocks[x1][y1];
        twinBlocks[x1][y1] = twinBlocks[x2][y2];
        twinBlocks[x2][y2] = tmp;
        return twinBlocks;
    }

    private int[][] copy(int[][] originalBlocks) {
        int[][] blockCopy = new int[dimension()][dimension()];
        for (int i = 0; i < originalBlocks.length; i++) {
            for (int j = 0; j < originalBlocks.length; j++) {
                blockCopy[i][j] = originalBlocks[i][j];
            }
        }
        return blockCopy;
    }

    /**
     * @param y another Object
     * @return boolean whether this class instance equals exactly Object y
     */
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        if ((this.blocks.length != ((Board) y).blocks.length)) return false;

        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks.length; j++) {
                if (((Board) y).blocks[i][j] != this.blocks[i][j]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        List<Board> neighborBoards = new ArrayList<>();
        int[] blankPosition = findBlank();
        int blankRow = blankPosition[0];
        int blankCol = blankPosition[1];

        if (blankRow + 1 < this.blocks.length) { // swap bottom
            neighborBoards.add(new Board(swap(blankRow, blankCol, blankRow + 1, blankCol)));
        }

        if (blankCol + 1 < this.blocks.length) { // swap right
            neighborBoards.add(new Board(swap(blankRow, blankCol, blankRow, blankCol + 1)));
        }

        if (blankRow - 1 >= 0) { // swap top
            neighborBoards.add(new Board(swap(blankRow, blankCol, blankRow - 1, blankCol)));
        }

        if (blankCol - 1 >= 0) { // swap left
            neighborBoards.add(new Board(swap(blankRow, blankCol, blankRow, blankCol - 1)));
        }

        return neighborBoards;
    }

    private int[] findBlank() {
        int[] blankIndices = new int[2];
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks.length; j++) {
                if (this.blocks[i][j] == 0) {
                    blankIndices[0] = i;
                    blankIndices[1] = j;
                    return blankIndices;
                }
            }
        }
        throw new RuntimeException();
    }


    public String toString() {
        StringBuilder s = new StringBuilder();
        int n = dimension();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", this.blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        // int[][] new_ = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        // Board board = new Board(new_);
        // System.out.println(board.toString());
        // System.out.println("\n\n\n");
        // System.out.println(board);
        // System.out.println(board.isGoal());
        // System.out.println(board.hamming());
        // System.out.println(board.manhattan());
        // for (Board n : board.neighbors()) System.out.println(n.toString());
    }
}
