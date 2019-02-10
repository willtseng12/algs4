/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] status;
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF auxGrid;
    private int size;
    private int openSite;
    private final int virtualTop;
    private final int virtualBottom;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.size = n;
        this.openSite = 0;
        this.virtualTop = 0;
        this.virtualBottom = (n * n) + 1;
        status = new boolean[(n * n) + 2];
        status[0] = true;
        status[n * n + 1] = true;
        grid = new WeightedQuickUnionUF((n * n) + 2);
        auxGrid = new WeightedQuickUnionUF((n * n) + 1);
    }

    public void open(int row, int col) {
        checkInbound(row, col);
        int boxNum = xyTo1D(row, col);
        status[boxNum] = true;
        openSite++;
        if (row > 1 && isOpen(row - 1, col)) { // connect top neighbor
            grid.union(boxNum, xyTo1D(row - 1, col));
            auxGrid.union(boxNum, xyTo1D(row - 1, col));
        }
        if (row < size && isOpen(row + 1, col)) { // connect botttom neighbor
            grid.union(boxNum, xyTo1D(row + 1, col));
            auxGrid.union(boxNum, xyTo1D(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) { // connect left neighbor
            grid.union(boxNum, xyTo1D(row, col - 1));
            auxGrid.union(boxNum, xyTo1D(row, col - 1));
        }
        if (col < size && isOpen(row, col + 1)) { // connect right neighbor
            grid.union(boxNum, xyTo1D(row, col + 1));
            auxGrid.union(boxNum, xyTo1D(row, col + 1));
        }

        if (row == 1) { // connect top row blocks with virtual top
            grid.union(boxNum, virtualTop);
            auxGrid.union(boxNum, virtualTop);
        }
        if (row == size) { // connect bottom row blocks with virtual bottom
            grid.union(boxNum, virtualBottom);
        }
    }

    public boolean isOpen(int row, int col) {
        checkInbound(row, col);
        return status[xyTo1D(row, col)];
    }

    public boolean isFull(int row, int col) {
        checkInbound(row, col);
        int boxNum = xyTo1D(row, col);
        return grid.connected(boxNum, virtualTop) && auxGrid.connected(virtualTop, boxNum);
    }

    public int numberOfOpenSites() {
        return openSite;
    }

    public boolean percolates() {
        return grid.connected(virtualTop, virtualBottom);
    }

    private void checkInbound(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException();
        }
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * size + col;
    }

    public static void main(String[] args) {
    }
}
