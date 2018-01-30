import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> res;
    private Point[] backup;
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Input is null.");
        Arrays.sort(points);
        if (points[0] == null) throw new IllegalArgumentException("Input contains null.");
        backup[0] = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("Input contains null.");
            if (points[i].compareTo(points[i - 1]) == 0) throw new IllegalArgumentException("Input contains duplicate.");
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
        return (LineSegment[]) res.toArray();
    }
    
    // Helper Methods
    private void findSegments(Point[] points, Point p) {
        int start = 1;
        double slop = p.slopeTo(points[1]);
        
        for (int i = 2; i < points.length; i++) {
            double tempSlop = p.slopeTo(points[i]);
            if (tempSlop != slop) {
                // check to see whether there have already 3 equal points
                if (i - start >= 3) {
                    LineSegment ls = genSegment(points, p, start, i);
                    res.add(ls);
                }
                // update
                start = i;
                slop = p.slopeTo(points[i]);
            }
        }
    }
    
    private LineSegment genSegment(Point[] points, Point p, int start, int end) {
        ArrayList<Point> temp = new ArrayList<>();
        temp.add(p);
        for (int i = start; i < end; i++) {
            temp.add(points[i]);
        }
        temp.sort(null);
        return new LineSegment(temp.get(0), temp.get(temp.size() - 1));
    }
}