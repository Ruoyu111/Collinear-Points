import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private ArrayList<Point[]> res;
    private LineSegment[] lineSegments;
    private Point[] backup;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("Input is null.");
        Arrays.sort(points);
        backup = new Point[points.length];
        res = new ArrayList<Point[]>();
        if (points[0] == null)
            throw new IllegalArgumentException("Input contains null.");
        backup[0] = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Input contains null.");
            if (points[i].compareTo(points[i - 1]) == 0)
                throw new IllegalArgumentException("Input contains duplicate.");
            backup[i] = points[i];
        }

        // loop through points in backup array, and sort the points array
        for (Point p : backup) {
            Arrays.sort(points, p.slopeOrder());
            findSegments(points, p);
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return res.size();
    }

    // the line segments
    public LineSegment[] segments() {
        if (lineSegments == null) {
            lineSegments = new LineSegment[res.size()];
            int pos = 0;
            for (Point[] seg : res) {
                lineSegments[pos++] = new LineSegment(seg[0], seg[1]);
            }
        }
        return lineSegments;
    }

    // Helper Methods
    private void findSegments(Point[] points, Point p) {
        // start from position 1, since position 0 will be the point p itself
        int start = 1;
        double slop = p.slopeTo(points[1]);

        for (int i = 2; i < points.length; i++) {
            double tempSlop = p.slopeTo(points[i]);
            if (tempSlop != slop) {
                // check to see whether there have already 3 equal points
                if (i - start >= 3) {
                    Point[] ls = genSegment(points, p, start, i);
                    if (isUnique(res, ls)) {
                        res.add(ls);
                    }
                }
                // update
                start = i;
                slop = tempSlop;
            }
        }
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
    private boolean isUnique(ArrayList<Point[]> res, Point[] ls) {
        for (Point[] seg : res) {
            if (segEqual(seg, ls))
                return false;
        }
        return true;
    }

    private boolean segEqual(Point[] seg, Point[] ls) {
        if (seg[0].compareTo(ls[0]) == 0 && seg[1].compareTo(ls[1]) == 0)
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