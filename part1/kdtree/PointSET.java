/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> tree;

    public PointSET() {
        this.tree = new TreeSet<>();
    }

    /**
     * @return boolean true if the set is empty else false
     */
    public boolean isEmpty() {
        return this.tree.isEmpty();
    }

    /**
     * @return number of points in the set
     */
    public int size() {
        return this.tree.size();
    }

    /**
     * add the point to the set (if it is not already in the set)
     *
     * @param p a new point
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        this.tree.add(p);
    }

    /**
     * Check whether the set contains the given point
     *
     * @param p point to search for
     * @return boolean true if the set contain point p
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return this.tree.contains(p);
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        for (Point2D p : this.tree) {
            p.draw();
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
        SET<Point2D> intersected = new SET<Point2D>();
        for (Point2D s : this.tree) {
            if (rect.contains(s)) intersected.add(s);
        }
        return intersected;
    }

    /**
     * Find a nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p a query point
     * @return a nearest neighboring point
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (this.tree.isEmpty()) return null;
        Point2D nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Point2D s : this.tree) {
            double d = s.distanceSquaredTo(p);
            if (d < nearestDistance) {
                nearestDistance = d;
                nearest = s;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

    }
}
