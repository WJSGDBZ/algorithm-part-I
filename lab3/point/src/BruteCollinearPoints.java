import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

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

    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        validate(points);

        int len = points.length;
        ArrayList<LineSegment> segments = new ArrayList<>();
        Point[] mockPoints = Arrays.copyOf(points, len);
        Arrays.sort(mockPoints);

        for (int p = 0; p < len; p++) {
            for (int q = p + 1; q < len; q++) {
                for (int r = q + 1; r < len; r++) {
                    for (int s = r + 1; s < len; s++) {
                        if (mockPoints[p].slopeTo(mockPoints[q]) == mockPoints[p].slopeTo(mockPoints[r])
                                && mockPoints[p].slopeTo(mockPoints[q]) == mockPoints[p].slopeTo(mockPoints[s])) {
                            Point[] subpoint = {mockPoints[p], mockPoints[q], mockPoints[r], mockPoints[s]};

                            segments.add(new LineSegment(subpoint[0], subpoint[3]));
                        }
                    }
                }
            }
        }

        lineSegments = new LineSegment[segments.size()];
        segments.toArray(lineSegments);
    }

    public int numberOfSegments() {                  // the number of line segments
        return lineSegments.length;
    }

    public LineSegment[] segments() {   // the line segments
        return Arrays.copyOf(lineSegments, numberOfSegments());
    }
}
