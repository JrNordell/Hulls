import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MergeHull implements ConvexHullFinder {


    public List<Point2D> computeHull(List<Point2D> points) {
        //Array list of hull points in ccw order
        List<Point2D> hull = new ArrayList<>();
        //Using Collections.Sort on the point list to find the leftmost and rightmost and maintain O(nlog(n))
        points.sort(new Comparator<Point2D>() {
            //custom compare method to compare two points
            public int compare(Point2D o1, Point2D o2) {
                return Double.compare(o1.getX(), o2.getX());
            }});
        //return hull to gui class
        return hull;
    }

    //recursive method for splitting and calling helper method findTangent
    private List<Point2D> recursiveMergeHull(List<Point2D> points){
        //Three lists to hold the left and right hulls for finding the tangent
        //And then the resulting hull to return
        List<Point2D> result;
        List<Point2D> left;
        List<Point2D> right;

        //Conditional for the base case if there is only 1 point in the list
        if(points.size() > 1){
            //recursively calls this method to continue splitting up the list
            left = recursiveMergeHull(points.subList(0,points.size()/2));
            right = recursiveMergeHull(points.subList(points.size()/2, points.size()));

            //Calls the findTangent method to do the work of combining and come out with a resulting hull
            result = findTangent(left, right);

        }else{
            //create the base case hull of a single point to begin the recursion back to the top
            result = points;
        }
        //return the hull
        return result;
    }

    //method for doing the bulk of the work for combining the left and right hulls every time
    private List<Point2D> findTangent(List<Point2D> left, List<Point2D> right){
        List<Point2D> result = new ArrayList<>();






        return result;
    }
}
