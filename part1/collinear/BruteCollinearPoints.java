/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        checkNull(points);
        List<LineSegment> lineSegmentsList = new ArrayList<>();
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDuplicate(sortedPoints);
        for (int i = 0; i < sortedPoints.length - 3; i++) {

            for (int j = i + 1; j < sortedPoints.length - 2; j++) {
                double firstSlope = sortedPoints[i].slopeTo(sortedPoints[j]);

                for (int k = j + 1; k < sortedPoints.length - 1; k++) {
                    double secondSlope = sortedPoints[i].slopeTo(sortedPoints[k]);

                    if (firstSlope != secondSlope) continue;
                    for (int m = k + 1; m < sortedPoints.length; m++) {
                        double thirdSlope = sortedPoints[i].slopeTo(sortedPoints[m]);

                        if (firstSlope == thirdSlope) {
                            lineSegmentsList.add(new LineSegment(sortedPoints[i],
                                                                 sortedPoints[m]));
                        }
                    }
                }
            }
        }
        this.lineSegments = lineSegmentsList.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {
        return lineSegments.length;
    }

    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    /**
     * Assumes that the input array is sorted
     *
     * @throws IllegalArgumentException if input is null, contains null, or contains duplicate
     *                                  points
     */
    private void checkDuplicate(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void checkNull(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
        }
    }


    public static void main(String[] args) {
    }
}

