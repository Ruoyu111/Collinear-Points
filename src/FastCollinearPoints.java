import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private final LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // verify input
        // 1. null check
        if (points == null)
            throw new IllegalArgumentException("Input is null.");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Input contains null.");
        }

        // copy input parameter to avoid direct modify
        Point[] localPoints = points.clone();

        // sort local points to avoid mutate input
        Arrays.sort(localPoints);

        // 2. duplicate check
        if (localPoints.length > 1) {
            for (int m = 1; m < localPoints.length; m++) {
                if (localPoints[m].compareTo(localPoints[m - 1]) == 0)
                    throw new IllegalArgumentException("Input contains duplicate.");
            }
        }

        ArrayList<Point[]> res = new ArrayList<Point[]>();

        if (localPoints.length > 3) {
            for (int i = 0; i < localPoints.length - 1; i++) {
                Arrays.sort(localPoints, i + 1, localPoints.length, localPoints[i].slopeOrder());
                findSegments(localPoints, i, res);
            }
        }

        segments = new LineSegment[res.size()];
        int pos = 0;
        for (Point[] seg : res) {
            segments[pos++] = new LineSegment(seg[0], seg[1]);
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }

    // Helper Methods
    private void findSegments(Point[] points, int i, ArrayList<Point[]> res) {
        Point p = points[i];
        double slop = p.slopeTo(points[i + 1]);
        int start = i + 1;

        for (int index = i + 2; index < points.length; index++) {
            double tempSlop = p.slopeTo(points[index]);
            if (!collinearSlop(tempSlop, slop)) {
                // check to see whether there have already 3 equal points
                if (index - start >= 3) {
                    Point[] ls = genSegment(points, p, start, index);
                    if (isUnique(ls, res)) {
                        res.add(ls);
                    }
                }
                // update
                start = index;
                slop = tempSlop;
            }
        }
        if (points.length - start >= 3) {
            Point[] ls = genSegment(points, p, start, points.length);
            if (isUnique(ls, res)) {
                res.add(ls);
            }
        }
    }

    private boolean collinearSlop(double tempSlop, double slop) {
        if (Double.compare(slop, tempSlop) == 0)
            return true;
        return false;
    }

    private Point[] genSegment(Point[] points, Point p, int start, int end) {
        ArrayList<Point> temp = new ArrayList<>();
        temp.add(p);
        for (int i = start; i < end; i++) {
            temp.add(points[i]);
        }
        temp.sort(null);
        return new Point[] { temp.get(0), temp.get(temp.size() - 1) };
    }

    // check duplicate
    private boolean isUnique(Point[] ls, ArrayList<Point[]> res) {
        for (Point[] seg : res) {
            if (segEqual(seg, ls))
                return false;
        }
        return true;
    }

    private boolean segEqual(Point[] seg, Point[] ls) {
        double slopSeg = seg[0].slopeTo(seg[1]);
        double slopLs = ls[0].slopeTo(ls[1]);
        if (Double.compare(slopSeg, slopLs) != 0)
            return false;
        if ((seg[0].compareTo(ls[0]) == 0) || (seg[1].compareTo(ls[1]) == 0))
            return true;
        if (Double.compare(slopSeg, seg[0].slopeTo(ls[0])) == 0)
            return true;
        return false;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}