/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(this.picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        if (isOutOfBound(x, y)) throw new IllegalArgumentException();
        // corner cases
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000.0;
        }
        double xGradient = calculateXGradient(x, y);
        double yGradient = calculateYGradient(x, y);
        return Math.pow(xGradient + yGradient, 0.5);
    }

    private double calculateXGradient(int x, int y) {
        if (isOutOfBound(x, y)) throw new IllegalArgumentException();
        Color leftColor = picture.get(x - 1, y);
        Color rightColor = picture.get(x + 1, y);

        int leftRed = leftColor.getRed();
        int leftBlue = leftColor.getBlue();
        int leftGreen = leftColor.getGreen();
        int rightRed = rightColor.getRed();
        int rightBlue = rightColor.getBlue();
        int rightGreen = rightColor.getGreen();

        return Math.pow(Math.abs(leftRed - rightRed), 2.0) +
                Math.pow(Math.abs(leftBlue - rightBlue), 2.0) +
                Math.pow(Math.abs(leftGreen - rightGreen), 2.0);
    }

    private double calculateYGradient(int x, int y) {
        if (isOutOfBound(x, y)) throw new IllegalArgumentException();
        Color bottomColor = picture.get(x, y - 1);
        Color topColor = picture.get(x, y + 1);

        int bottomRed = bottomColor.getRed();
        int bottomBlue = bottomColor.getBlue();
        int bottomGreen = bottomColor.getGreen();
        int topRed = topColor.getRed();
        int topBlue = topColor.getBlue();
        int topGreen = topColor.getGreen();

        return Math.pow(Math.abs(bottomRed - topRed), 2.0) +
                Math.pow(Math.abs(bottomBlue - topBlue), 2.0) +
                Math.pow(Math.abs(bottomGreen - topGreen), 2.0);
    }

    private boolean isOutOfBound(int x, int y) {
        return (x < 0 || x > width() - 1 || y < 0 || y > height() - 1);
    }

    public int[] findHorizontalSeam() {
        int[][] edgeTo = new int[width()][height()];
        double[][] distTo = new double[width()][height()];
        int[] seam = new int[width()];

        resetDistMatrix(distTo);
        for (int i = 0; i < height(); i++) {
            distTo[0][i] = energy(0, i);
            edgeTo[0][i] = -1;
        }

        // build up edgeTo and distTo matrix in topological order
        for (int i = 0; i < width() - 1; i++) {
            for (int j = 0; j < height(); j++) {
                relaxHorizontalEdges(i, j, edgeTo, distTo);
            }
        }

        // find the least accumulated energy pixel on the rightmost column
        double minEnergy = Double.POSITIVE_INFINITY;
        int minEnergyIndex = 0;
        for (int j = 0; j < height(); j++) {
            if (distTo[width() - 1][j] < minEnergy) {
                minEnergy = distTo[width() - 1][j];
                minEnergyIndex = j;
            }
        }

        // build the seam by moving backwards through the edgeTo matrix
        int trace = minEnergyIndex;
        int i = width() - 1;
        while (trace != -1) {
            seam[i] = trace;
            trace = edgeTo[i][trace];
            i--;
        }
        return seam;
    }

    private boolean isSeamInbound(int[] seam, boolean isVertical) {
        if (isVertical) {
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] < 0 || seam[i] > width() - 1) return false;
            }
        }
        else {
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] < 0 || seam[i] > height() - 1) return false;
            }
        }
        return true;
    }

    private boolean isValidSeam(int[] seam) {

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                return false;
            }
        }
        return true;
    }

    public int[] findVerticalSeam() {
        int[][] edgeTo = new int[width()][height()];
        double[][] distTo = new double[width()][height()];
        int[] seam = new int[height()];

        resetDistMatrix(distTo);
        for (int i = 0; i < width(); i++) {
            distTo[i][0] = energy(i, 0);
            edgeTo[i][0] = -1;
        }

        // build up edgeTo and distTo matrix
        for (int j = 0; j < height() - 1; j++) {
            for (int i = 0; i < width(); i++) {
                relaxVerticalEdges(i, j, edgeTo, distTo);
            }
        }

        // find the least accumulated energy pixel at the bottom
        double minEnergy = Double.POSITIVE_INFINITY;
        int minEnergyIndex = 0;
        for (int i = 0; i < width(); i++) {
            if (distTo[i][height() - 1] < minEnergy) {
                minEnergy = distTo[i][height() - 1];
                minEnergyIndex = i;
            }
        }

        // build the seam by moving backwards through the edgeTo matrix
        int trace = minEnergyIndex;
        int j = height() - 1;
        while (trace != -1) {
            seam[j] = trace;
            trace = edgeTo[trace][j];
            j--;
        }
        return seam;
    }

    private Iterable<Integer> adj(int i, int j, boolean isVertical) {
        List<Integer> neighbor = new ArrayList<>();
        if (isVertical) {
            for (int x = -1; x < 2; x++) {
                if (!isOutOfBound(i + x, j + 1)) neighbor.add(i + x);
            }
            return neighbor;
        }
        else {
            for (int x = -1; x < 2; x++) {
                if (!isOutOfBound(i + 1, j + x)) neighbor.add(j + x);
            }
            return neighbor;
        }
    }

    private void relaxVerticalEdges(int i, int j, int[][] edgeTo, double[][] distTo) {
        for (int x : adj(i, j, true))
            if (distTo[x][j + 1] > distTo[i][j] + energy(x, j + 1)) {
                distTo[x][j + 1] = distTo[i][j] + energy(x, j + 1);
                edgeTo[x][j + 1] = i;
            }
    }

    private void relaxHorizontalEdges(int i, int j, int[][] edgeTo, double[][] distTo) {
        for (int x : adj(i, j, false)) {
            if (distTo[i + 1][x] > distTo[i][j] + energy(i + 1, x)) {
                distTo[i + 1][x] = distTo[i][j] + energy(i + 1, x);
                edgeTo[i + 1][x] = j;
            }
        }
    }

    private void resetDistMatrix(double[][] distTo) {
        for (int i = 0; i < distTo.length; i++) {
            for (int j = 0; j < distTo[i].length; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    // swap the two for loops
    // rename ffor loop i and j to col and rows!!
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || height() <= 1 ||
                seam.length != width() || !isValidSeam(seam) ||
                !isSeamInbound(seam, false)) {
            throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height() - 1; j++) {
                if (j < seam[i]) {
                    newPicture.set(i, j, picture.get(i, j));
                }
                else {
                    newPicture.set(i, j, picture.get(i, j + 1));
                }
            }
        }
        this.picture = new Picture(newPicture);

    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null || width() <= 1 ||
                seam.length != height() || !isValidSeam(seam) ||
                !isSeamInbound(seam, true)) {
            throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(width() - 1, height());
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width() - 1; i++) {
                if (i < seam[j]) {
                    newPicture.set(i, j, picture.get(i, j));
                }
                else {
                    newPicture.set(i, j, picture.get(i + 1, j));
                }
            }
        }
        this.picture = newPicture;
    }

    public static void main(String[] args) {
        // Picture picture = new Picture("12x10.png");
        // StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(),
        //               picture.height());
        //
        // SeamCarver sc = new SeamCarver(picture);
        //
        // StdOut.printf("Printing energy calculated for each pixel.\n");
        //
        // for (int row = 0; row < sc.height(); row++) {
        //     for (int col = 0; col < sc.width(); col++)
        //         StdOut.printf("%9.0f ", sc.energy(col, row));
        //     StdOut.println();
    }
}

