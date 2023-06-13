import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;
    private final ArrayList<LineSegment> segments;

    private Point minPoint(Point a, Point b){
        if(a.compareTo(b) < 0){
            return a;
        }

        return b;
    }
    private Point maxPoint(Point a, Point b){
        if(a.compareTo(b) > 0){
            return a;
        }

        return b;
    }

    private void Judge(int start, int end, int threshold, Point[] points, Point p) {
        if (end - start >= threshold) {
            int size = end - start;
            Point startpoint = p;
            Point endpoint = p;

            for (int n = 0; n < size; n++) {
                startpoint = minPoint(startpoint, points[start + n]);
                endpoint = maxPoint(endpoint, points[start + n]);
                StdOut.println(points[start + n]);
            }

            if (!isDuplication(startpoint, p)) {
                segments.add(new LineSegment(startpoint, endpoint));
            }
        }
    }

    private boolean isDuplication(Point startpoint, Point p) {
        return p.slopeTo(startpoint) != Double.NEGATIVE_INFINITY;
    }

    private void validate(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points is null!");
        }

        Point[] test = Arrays.copyOf(points, points.length);
        for (Point point : test) {
            if (point == null) {
                throw new IllegalArgumentException("point is null in points!");
            }
        }

        Arrays.sort(test);
        for (int i = 0; i < test.length - 1; i++) {
            if (test[i].slopeTo(test[i + 1]) == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("point is duplicate!");
            }
        }
    }

    public FastCollinearPoints(Point[] points) {         // finds all line segments containing 4 or more points
        validate(points);

        int len = points.length;
        segments = new ArrayList<>();
        Point[] candidate = Arrays.copyOf(points, len);
        Arrays.sort(candidate);
        StdOut.println(Arrays.toString(candidate));

        for (int i = 0; i < len; i++) {
            Point p = candidate[i];
            Point[] mockPoints = Arrays.copyOf(points, len);
            Arrays.sort(mockPoints, p.slopeOrder());
            StdOut.println(Arrays.toString(mockPoints));

            int start = 1;
            int threshold = 3; // 4 point = 3 line
            for (int j = 2; j < mockPoints.length + 1; j++) {
                if (j == mockPoints.length) {
                    Judge(start, j, threshold, mockPoints, p);
                } else if (p.slopeTo(mockPoints[start]) != p.slopeTo(mockPoints[j])) {
                    Judge(start, j, threshold, mockPoints, p);
                    start = j;
                }
            }
        }

        // List to Array
        lineSegments = new LineSegment[segments.size()];
        segments.toArray(lineSegments);
    }

    public int numberOfSegments() {    // the number of line segments
        return lineSegments.length;
    }

    public LineSegment[] segments() {    // the line segments
        return Arrays.copyOf(lineSegments, numberOfSegments());
    }
}
