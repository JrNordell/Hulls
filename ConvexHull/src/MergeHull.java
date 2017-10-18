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

        hull = recursiveMergeHull(points);

        System.out.println("Start \n");
        for (Point2D point: hull) {
            System.out.println(point.getX());
        }
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

        int indexL = left.indexOf(rightmost);
        int indexR = right.indexOf(leftmost);

        lowerTangent = calcTangent(lowerTangent, indexL, indexR, left, right);
        upperTangent = calcTangent(upperTangent, indexR, indexL, right, left);


            int i = 0;
            while(!left.get(i).equals(upperTangent.getP2())){
                result.add(left.get(i));
                i++;
            }
            result.add(upperTangent.getP2());
            int j = right.indexOf(upperTangent.getP1());
            while(!right.get(j).equals(lowerTangent.getP2())){
                result.add(right.get(j));
                j = (j+1)%right.size();
            }
            result.add(lowerTangent.getP2());
            i = left.indexOf(lowerTangent.getP1());
            if(!result.contains(left.get(i))){
                while (i < left.size()) {
                    result.add(left.get(i));
                    i++;
                }
            }

        return result;
    }

    private Line2D calcTangent(Line2D line, int indexL, int indexR, List<Point2D> left, List<Point2D> right){
        boolean isComplete = false;
        while(!isComplete){
            int i = 0;
            while (i < 1) {
                i++;
                if (line.relativeCCW(left.get(mod(indexL-1, left.size())))<0){
                    indexL = (indexL - 1);
                    indexL = mod(indexL, left.size());
                    line.setLine(left.get(indexL), line.getP2());
                    i = 0;
                }else if(line.relativeCCW(left.get((indexL + 1)%left.size()))<0){
                    indexL = (indexL + 1)%left.size();
                    line.setLine(left.get(indexL), line.getP2());
                    i = 0;
                }
            }

            int j = 0;
            while (j < 1) {
                j++;
                if (line.relativeCCW(right.get(mod((indexR - 1), right.size())))<0){
                    indexR = (indexR - 1);
                    indexR = mod(indexR, right.size());
                    line.setLine(line.getP1(), right.get(indexR));
                    j = 0;
                }else if(line.relativeCCW(right.get((indexR+1)%right.size()))<0){
                    indexR = (indexR+1)%right.size();
                    line.setLine(line.getP1(), right.get(indexR));
                    j = 0;
                }
            }

            if(isTangent(left, line, indexL) && isTangent(right, line, indexR)){
                isComplete = true;
            }

        }
        return line;
    }

    private boolean isTangent(List<Point2D> list, Line2D tangent, int index){
        boolean result = true;
        if(tangent.relativeCCW(list.get(mod(index-1, list.size()))) < 0){
            result = false;
        }else if(tangent.relativeCCW(list.get(mod(index+1, list.size()))) < 0){
            result = false;
        }

        return result;
    }
    private int mod(int a, int b){
        int result;
        if(a >= 0){
            result = a % b;
        }else{
            result = b + a;
        }
        return result;
    }

    private List<Point2D> computeRightLeft(List<Point2D> left, List<Point2D> right){
        List<Point2D> result = new ArrayList<>();
        double x = left.get(0).getX();
        result.add(left.get(0));
        for (Point2D point: left) {
            if(point.getX() > x){
                result.set(0, point);
                x = point.getX();
            }
        }
        x = right.get(0).getX();
        result.add(right.get(0));
        for (Point2D point: right) {
            if(point.getX() < x){
                result.set(1, point);
                x = point.getX();
            }
        }

        return result;
    }


}
