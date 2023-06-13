import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> points;

    public PointSET()                               // construct an empty set of points
    {
        points = new SET<>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return points.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return points.size();
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException("Function Insert Error: Point2D is null!");
        }

        points.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException("Function Contains Error: Point2D is null!");
        }

        return points.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D point : points) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException("Function Range Error: RectHV is null!");
        }

        Queue<Point2D> queue = new Queue<>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                queue.enqueue(point);
            }
        }

        return queue;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException("Function Nearest Error: Point2D is null!");
        }

        Point2D nearest = null;
        for (Point2D point : points) {
            if (nearest == null) {
                nearest = point;
            } else if (p.distanceSquaredTo(point) < p.distanceSquaredTo(nearest)) {
                nearest = point;
            }
        }

        return nearest;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {

    }
}