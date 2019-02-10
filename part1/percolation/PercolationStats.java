/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] percolateRatios;
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.percolateRatios = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int randRow = StdRandom.uniform(1, n + 1);
                int randCol = StdRandom.uniform(1, n + 1);
                if (!perc.isOpen(randRow, randCol)) {
                    perc.open(randRow, randCol);
                }
            }
            percolateRatios[i] = perc.numberOfOpenSites() / Math.pow(n, 2);
        }
        mean = StdStats.mean(percolateRatios);
        stddev = StdStats.stddev(percolateRatios);
        confidenceLo = mean - (1.96 * stddev) / Math.sqrt(trials);
        confidenceHi = mean + (1.96 * stddev) / Math.sqrt(trials);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidenceLo;
    }

    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        int gridSize = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(gridSize, trials);
        System.out.printf("mean                     = %f\n", percStats.mean());
        System.out.printf("stddev                   = %f\n", percStats.stddev());
        System.out.printf("95%% confidence Interval  = %f, %f\n",
                          percStats.confidenceLo(), percStats.confidenceHi());
    }
}
