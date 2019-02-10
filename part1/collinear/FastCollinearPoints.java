/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    private final LineSegment[] lineSegmentsArray;

    public FastCollinearPoints(Point[] points) {

        checkNull(points);
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDuplicate(sortedPoints);

        List<LineSegment> lineSegmentsList = new ArrayList<>();

        int arrayLength = points.length;

        for (int i = 0; i < arrayLength; i++) {
            Point refPoint = sortedPoints[i];
            Comparator<Point> slopeComparator = refPoint.slopeOrder();

            Point[] slopeSortedArray = sortedPoints.clone();
            Arrays.sort(slopeSortedArray, slopeComparator);

            int j = 1;
            while (j < arrayLength) {
                double refSlope = refPoint.slopeTo(slopeSortedArray[j]);
                List<Point> candidateArray = new ArrayList<>();

                // look for 3 + collinear candidates aside from reference point
                do {
                    candidateArray.add(slopeSortedArray[j++]);
                } while (j < arrayLength &&
                        refPoint.slopeTo(slopeSortedArray[j]) == refSlope);

                int candidateSize = candidateArray.size();
                Point origincandidate = candidateArray.get(0);
                Point endPoint = candidateArray.get(candidateSize - 1);

                // Check if the first point among candidate points is
                // smaller than the reference point. If it is, then the
                // reference point lies between the first and last point of the
                // candidate segment array, so dont add a segment with
                // the reference point as an end point. This works
                // because Array.sort() for object type is stable.

                if (candidateSize >= 3 && refPoint.compareTo(origincandidate) < 0) {
                    lineSegmentsList.add(new LineSegment(refPoint, endPoint));
                }
            }
        }
        this.lineSegmentsArray = lineSegmentsList.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {
        return this.lineSegmentsArray.length;
    }

    public LineSegment[] segments() {
        return lineSegmentsArray.clone();
    }

    /**
     * Precondition: Assumes that the input array is sorted
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
