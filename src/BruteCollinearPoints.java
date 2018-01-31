import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private ArrayList<Point[]> res;
    private LineSegment[] lineSegments;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        Point[] temp = new Point[4];
        for (int i = 0; i < points.length - 3; i++) {
            temp[0] = points[i];
            for (int j = 1; j < points.length - 2; j++) {
                temp[1] = points[j];
                for (int p = 2; p < points.length - 1; p++) {
                    temp[2] = points[p];
                    for (int k = 3; k < points.length; k++) {
                        temp[3] = points[k];
                        if (collinear(temp)) {
                            Point[] segment = getSeg(temp);
                            if (isUnique(res, segment)) {
                                res.add(segment);
                            }
                        }
                    }
                }
            }
            
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
    
    // Helper functions
    
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
    
    private Point[] getSeg(Point[] temp) {
        Arrays.sort(temp);
        return new Point[] { temp[0], temp[3] };
    }

    private boolean collinear(Point[] temp) {
        double slop1 = temp[0].slopeTo(temp[1]);
        double slop2 = temp[0].slopeTo(temp[2]);
        double slop3 = temp[0].slopeTo(temp[3]);
        if (slop1 == slop2 && slop1 == slop3) return true;
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
