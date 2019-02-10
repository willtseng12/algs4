/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private int size;

    private class Node {
        private final Point2D p;
        private final boolean vertical;
        private Node leftBottom = null;
        private Node rightTop = null;

        public Node(Point2D p, boolean vertical) {
            this.p = p;
            this.vertical = vertical;
        }
    }

    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * @return boolean true if the set is empty else false
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @return number of points in the set
     */
    public int size() {
        return this.size;
    }

    /**
     * add the point to the set (if it is not already in the set)
     *
     * @param p a new point
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        this.root = insert(this.root, p, true);
    }

    /**
     * recursive helper function that inserts a new point
     *
     * @param x          a node
     * @param p          a new point
     * @param isVertical boolean whether the given node is a vertical node
     * @return the node with new node added in it's subtree
     */
    private Node insert(Node x, Point2D p, boolean isVertical) {
        if (x == null) {
            this.size++;
            return new Node(p, isVertical);
        }

        if (p.equals(x.p)) {
            return x;
        }

        int cmp = x.vertical ? xCompare(p, x.p) : yCompare(p, x.p);

        if (cmp < 0) {
            x.leftBottom = insert(x.leftBottom, p, !x.vertical);
        }
        else {
            x.rightTop = insert(x.rightTop, p, !x.vertical);
        }
        return x;
    }

    /**
     * Compare 2 point by their x coordinates
     *
     * @param p1 first point
     * @param p2 second point
     * @return 1 if p1 > p2, -1 if p1 < p2, else 0
     */
    private int xCompare(Point2D p1, Point2D p2) {
        if (p1.x() < p2.x()) return -1;
        if (p1.x() > p2.x()) return 1;
        return 0;
    }

    /**
     * Compare 2 point by their y coordinates
     *
     * @param p1 first point
     * @param p2 second point
     * @return 1 if p1 > p2, -1 if p1 < p2, else 0
     */
    private int yCompare(Point2D p1, Point2D p2) {
        if (p1.y() < p2.y()) return -1;
        if (p1.y() > p2.y()) return 1;
        return 0;
    }

    /**
     * Check whether the set contains the given point
     *
     * @param p point to search for
     * @return boolean true if the set contain point p
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Node x = this.root;
        int cmp;

        while (x != null) {

            if (x.p.equals(p)) return true;

            cmp = x.vertical ? xCompare(p, x.p) : yCompare(p, x.p);

            if (cmp < 0) {
                x = x.leftBottom;
            }
            else {
                x = x.rightTop;
            }
        }
        return false;
    }

    /**
     * draw the complete KdTree
     */
    public void draw() {
        draw(this.root, new RectHV(0, 0, 1, 1));
    }

    /**
     * recursive helper function to draw the KdTree
     *
     * @param x    a node
     * @param rect a RectHV object the provided node is partitioning
     */
    private void draw(Node x, RectHV rect) {
        if (x != null) {
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);

            // draw the point
            x.p.draw();

            if (x.vertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(x.p.x(), rect.ymax(), x.p.x(), rect.ymin());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), x.p.y(), rect.xmax(), x.p.y());
            }
            RectHV leftBottomRect = subsetRect(x, rect, true);
            RectHV rightTopRect = subsetRect(x, rect, false);
            draw(x.leftBottom, leftBottomRect);
            draw(x.rightTop, rightTopRect);
        }
    }

    /**
     * cut a rectangle either horizontally or vertically depending on the whether the provided node
     * is a vertical node or not
     *
     * @param x            a node
     * @param rect         a RectHV object the provided node is partitioning
     * @param isLeftBottom boolean whether to return the left/bottom rectangle partitionn
     * @return a partitioned rectangle
     */
    private RectHV subsetRect(Node x, RectHV rect, boolean isLeftBottom) {
        if (isLeftBottom) {
            if (x.vertical) {
                return new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax());
            }
            else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y());
            }
        }
        else {
            if (x.vertical) {
                return new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
            else {
                return new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax());
            }
        }
    }

    /**
     * Find all points that are inside the given rectangle (or on the boundary)
     *
     * @param rect a RectHV Object
     * @return Iterable of all point within provided rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> inRange = new ArrayList<>();
        range(this.root, new RectHV(0, 0, 1, 1), rect, inRange);
        return inRange;

    }

    /**
     * recursive helper function that updates the list of points located in the provided rectangle
     *
     * @param x       a node
     * @param box     an outer rectangle containg the inner rectangle (unit rectangle for this
     *                assignment)
     * @param rect    the inner rectangle within "box" of which the points that lies wihtin it we
     *                are trying to find
     * @param inRange a list of point that lies wihtin "rect" (Empty list to begin with)
     */
    private void range(Node x, RectHV box,
                       RectHV rect, List<Point2D> inRange) {
        if (x != null) {
            if (rect.intersects(box)) {
                if (rect.contains(x.p)) inRange.add(x.p);

                RectHV leftBottom = subsetRect(x, box, true);
                RectHV rightTop = subsetRect(x, box, false);

                range(x.leftBottom, leftBottom, rect, inRange);
                range(x.rightTop, rightTop, rect, inRange);
            }
        }
    }

    /**
     * Find a nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p a query point
     * @return a nearest neighboring point
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node x = this.root;
        Point2D closest = nearest(x, p, new RectHV(0, 0, 1, 1), null);
        return closest;
    }

    /**
     * recursive helper function that find and return the closest neighboring point to the given
     * query point
     *
     * @param x         a node
     * @param p         the querying point
     * @param box       an outer rectangle containg the inner rectangle (unit rectangle for this *
     *                  assignment)
     * @param candidate the closest neigboring point found so far (null to begin with)
     * @return
     */
    private Point2D nearest(Node x, Point2D p, RectHV box, Point2D candidate) {
        if (x == null) return candidate;

        Point2D possibleCandidate = candidate;

        double nearestDist = possibleCandidate == null ? Double.MAX_VALUE :
                             p.distanceSquaredTo(possibleCandidate);

        // some point on the rectangle is closer to the query point
        // than the closest candidate from the previous un-partitioned
        // rectangle
        if (nearestDist > box.distanceSquaredTo(p)) {
            double dist = p.distanceSquaredTo(x.p);

            if (dist < nearestDist) possibleCandidate = x.p;

            RectHV leftBottom = subsetRect(x, box, true);
            RectHV rightTop = subsetRect(x, box, false);
            int cmp = (x.vertical) ? xCompare(p, x.p) : yCompare(p, x.p);

            // search the side of the partition the query point lies in first
            if (cmp < 0) {
                possibleCandidate = nearest(x.leftBottom, p, leftBottom, possibleCandidate);
                possibleCandidate = nearest(x.rightTop, p, rightTop, possibleCandidate);
            }
            else {
                possibleCandidate = nearest(x.rightTop, p, rightTop, possibleCandidate);
                possibleCandidate = nearest(x.leftBottom, p, leftBottom, possibleCandidate);
            }
        }
        return possibleCandidate;
    }

    public static void main(String[] args) {
        // KdTree kdtree = new KdTree();
        // kdtree.insert(new Point2D(0.2, 0.4));
        // kdtree.insert(new Point2D(0.5, 0.3));
        // kdtree.insert(new Point2D(0.7, 0.15));
        // kdtree.insert(new Point2D(0.1, 0.6));
        // kdtree.insert(new Point2D(0.4, 0.1));
        // kdtree.insert(new Point2D(0.8, 0.8));
        // kdtree.insert(new Point2D(0.88, 0.7));
        // kdtree.insert(new Point2D(0.2, 0.3));
        // kdtree.insert(new Point2D(0.2, 0.5));
        // System.out.println(kdtree.size);
        // System.out.println(kdtree.contains(new Point2D(2, 3)));
        // System.out.println(kdtree.contains(new Point2D(4, 5)));
        // System.out.println(kdtree.contains(new Point2D(1, 6)));
        // System.out.println(kdtree.contains(new Point2D(5, 4)));
        // for (Point2D p : kdtree.range(new RectHV(0, 0, 1, 1)))
        //     System.out.println(p);
        // kdtree.draw();
        // System.out.println(kdtree.size);
        // System.out.println(kdtree.nearest(new Point2D(0.8, 0.75)));
    }
}
