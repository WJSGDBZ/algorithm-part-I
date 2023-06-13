import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class KdTree {
    private Node Root;
    private int size;

    private class Node {
        Point2D node;
        Node left;
        Node right;
        boolean XCoordinate;

        public Node(Point2D node, boolean XCoordinate) {
            this.node = node;
            this.XCoordinate = XCoordinate;
        }
    }

    public KdTree()                               // construct an empty set of points
    {
        Root = null;
        size = 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return Root == null;
    }

    public int size()                         // number of points in the set
    {
        return size;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException("Function Insert Error: Point2D is null!");
        }
        if(contains(p)) return;

        if (Root == null) {
            Root = new Node(p, true);
        } else {
            Node it = Root;
            while (it != null) {
                if (it.XCoordinate) it = put(it, p, false, Point2D.X_ORDER);
                else it = put(it, p, true, Point2D.Y_ORDER);
            }
        }

        size++;
    }

    private Node put(Node it, Point2D p, boolean XCoordinate, Comparator<Point2D> compare) {
        if (compare.compare(p, it.node) < 0) {
            if (it.left != null) return it.left;
            else {
                it.left = new Node(p, XCoordinate);
            }
        } else {
            if (it.right != null) return it.right;
            else {
                it.right = new Node(p, XCoordinate);
            }
        }

        return null;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException("Function Contains Error: Point2D is null!");
        }

        if (Root != null) {
            Node it = Root;
            while (it != null) {
                if (it.node.equals(p)) return true;
                if (it.XCoordinate) it = find(it, p, Point2D.X_ORDER);
                else it = find(it, p, Point2D.Y_ORDER);
            }
        }

        return false;
    }

    private Node find(Node it, Point2D p, Comparator<Point2D> compare) {
        if (compare.compare(p, it.node) < 0) {
            if (it.left != null) return it.left;
        } else {
            if (it.right != null) return it.right;
        }

        return null;
    }

    public void draw()                         // draw all points to standard draw
    {
        preorderDraw(Root, new RectHV(0, 0, 1, 1));
    }

    private void preorderDraw(Node root, RectHV rect) {
        if (root == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        root.node.draw();
        if (root.XCoordinate) {
            RectHV leftSpace = new RectHV(rect.xmin(), rect.ymin(), root.node.x(), rect.ymax());
            RectHV rightSpace = new RectHV(root.node.x(), rect.ymin(), rect.xmax(), rect.ymax());

            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(leftSpace.xmax(), leftSpace.ymax(), rightSpace.xmin(), rightSpace.ymin());

            preorderDraw(root.left, leftSpace);
            preorderDraw(root.right, rightSpace);
        } else {
            RectHV topSpace = new RectHV(rect.xmin(), root.node.y(), rect.xmax(), rect.ymax());
            RectHV bottomSpace = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), root.node.y());

            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(topSpace.xmin(), topSpace.ymin(), bottomSpace.xmax(), bottomSpace.ymax());

            preorderDraw(root.left, bottomSpace);
            preorderDraw(root.right, topSpace);
        }
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException("Function Range Error: RectHV is null!");
        }

        Queue<Point2D> queue = new Queue<>();
        findRange2D(Root, rect, queue, new RectHV(0, 0, 1, 1));

        return queue;
    }

    private void findRange2D(Node root, RectHV rect, Queue<Point2D> queue, RectHV halfSpace) {
        if (root == null) return;
        if (rect.contains(root.node)) queue.enqueue(root.node);

        if (root.XCoordinate) {
            RectHV leftSpace = new RectHV(halfSpace.xmin(), halfSpace.ymin(), root.node.x(), halfSpace.ymax());
            RectHV rightSpace = new RectHV(root.node.x(), halfSpace.ymin(), halfSpace.xmax(), halfSpace.ymax());

            if (rect.intersects(leftSpace)) {
                findRange2D(root.left, rect, queue, leftSpace);
            }
            if (rect.intersects(rightSpace)) {
                findRange2D(root.right, rect, queue, rightSpace);
            }
        } else {
            RectHV topSpace = new RectHV(halfSpace.xmin(), root.node.y(), halfSpace.xmax(), halfSpace.ymax());
            RectHV bottomSpace = new RectHV(halfSpace.xmin(), halfSpace.ymin(), halfSpace.xmax(), root.node.y());

            if (rect.intersects(bottomSpace)) {
                findRange2D(root.left, rect, queue, bottomSpace);
            }
            if (rect.intersects(topSpace)) {
                findRange2D(root.right, rect, queue, topSpace);
            }
        }
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException("Function Nearest Error: Point2D is null!");
        }
        if(Root == null){
            return null;
        }

        return findNearest2D(Root, p, Root.node, new RectHV(0, 0, 1, 1));
    }

    private Point2D findNearest2D(Node root, Point2D p, Point2D nearest, RectHV rect) {
        if (root == null) return nearest;
        if (root.node.equals(p)) return root.node;
        nearest = closest(p, root.node, nearest);

        if (root.XCoordinate) {
            RectHV leftSpace = new RectHV(rect.xmin(), rect.ymin(), root.node.x(), rect.ymax());
            RectHV rightSpace = new RectHV(root.node.x(), rect.ymin(), rect.xmax(), rect.ymax());

            if (p.x() < root.node.x()) {
                nearest = closest(p, findNearest2D(root.left, p, nearest, leftSpace), nearest);
                double dx = rightSpace.xmin() - p.x();
                if (p.distanceSquaredTo(nearest) > dx*dx) {
                    nearest = closest(p, findNearest2D(root.right, p, nearest, rightSpace), nearest);
                }
            } else {
                nearest = closest(p, findNearest2D(root.right, p, nearest, rightSpace), nearest);
                double dx = p.x() - leftSpace.xmax();
                if (p.distanceSquaredTo(nearest) > dx*dx) {
                    nearest = closest(p, findNearest2D(root.left, p, nearest, leftSpace), nearest);
                }
            }
        } else {
            RectHV topSpace = new RectHV(rect.xmin(), root.node.y(), rect.xmax(), rect.ymax());
            RectHV bottomSpace = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), root.node.y());

            if (p.y() < root.node.y()) {
                nearest = closest(p, findNearest2D(root.left, p, nearest, bottomSpace), nearest);
                double dy = topSpace.ymin() - p.y();
                if (p.distanceSquaredTo(nearest) > dy*dy) {
                    nearest = closest(p, findNearest2D(root.right, p, nearest, topSpace), nearest);
                }
            } else {
                nearest = closest(p, findNearest2D(root.right, p, nearest, topSpace), nearest);
                double dy = p.y() - bottomSpace.ymax();
                if (p.distanceSquaredTo(nearest) > dy*dy) {
                    nearest = closest(p, findNearest2D(root.left, p, nearest, bottomSpace), nearest);
                }
            }
        }

        return nearest;
    }

    private Point2D closest(Point2D p, Point2D a, Point2D b){
        if(p.distanceSquaredTo(a) < p.distanceSquaredTo(b)) {
            return a;
        }

        return b;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
    }
}
