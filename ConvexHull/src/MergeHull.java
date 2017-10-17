import java.awt.geom.Line2D;
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
        List<Point2D> leftRight = computeRightLeft(left, right);
        Point2D rightmost = leftRight.get(0);
        Point2D leftmost = leftRight.get(1);
        Line2D lowerTangent = new Line2D.Double(rightmost, leftmost);
        Line2D upperTangent = new Line2D.Double(leftmost, rightmost);

        boolean isComplete = false;
        int indexL = left.indexOf(rightmost);
        int indexR = right.indexOf(leftmost);
        while(!isComplete){
            int i = 0;
            while (i < 2) {
                i++;
                if (lowerTangent.relativeCCW(left.get(indexL - 1))<0){
                    indexL -= 1;
                    lowerTangent.setLine(left.get(indexL), lowerTangent.getP2());
                    i = 0;
                }else if(lowerTangent.relativeCCW(left.get(indexL + 1))<0){
                    indexL += 1;
                    lowerTangent.setLine(left.get(indexL), lowerTangent.getP2());
                    i = 0;
                }
            }

            int j = 0;
            while (j < 2) {
                j++;
                if (lowerTangent.relativeCCW(right.get(indexR-1))<0){
                    indexR -= 1;
                    lowerTangent.setLine(lowerTangent.getP1(), right.get(indexR-1));
                    j = 0;
                }else if(lowerTangent.relativeCCW(right.get(indexR+1))<0){
                    indexR += 1;
                    lowerTangent.setLine(lowerTangent.getP1(), right.get(indexR+1));
                    j = 0;
                }
            }

        }



        return result;
    }

    private List<Point2D> computeRightLeft(List<Point2D> left, List<Point2D> right){
        List<Point2D> result = new ArrayList<>();
        double x = 0;
        for (Point2D point: left) {
            if(point.getX() > x){
                result.set(0, point);
                x = point.getX();
            }
        }
        x = right.get(0).getX();
        result.set(1, right.get(0));
        for (Point2D point: right) {
            if(point.getX() < x){
                result.set(1, point);
                x = point.getX();
            }
        }
//        double y = left.get(0).getY();
//        result.set(2, right.get(0));
//        for (Point2D point: left) {
//            if(point.getY() < y){
//                result.set(2, point);
//                y = point.getY();
//            }
//        }
//        y = right.get(0).getY();
//        result.set(3, right.get(0));
//        for (Point2D point: right) {
//            if(point.getY() < y){
//                result.set(1, point);
//                y = point.getY();
//            }
//        }


        return result;
    }

    private
}
