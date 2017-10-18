import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public class QuickHull implements ConvexHullFinder{

    public List<Point2D> computeHull(List<Point2D> points) {
        List<Point2D> hull = new ArrayList<>();
        List<Point2D> top = new ArrayList<>();
        List<Point2D> bottom = new ArrayList<>();
        Point2D leftMost = points.get(0);
        Point2D rightMost = points.get(0);


        for (Point2D point: points) {
            if(point.getX()< leftMost.getX()){
                leftMost = point;
            }else if(point.getX() > rightMost.getX()){
                rightMost = point;
            }
        }
        Line2D l = new Line2D.Double(leftMost, rightMost);
        Line2D lB = new Line2D.Double(rightMost, leftMost);
        for (Point2D point: points) {
            if(l.relativeCCW(point) > 0){
                top.add(point);
            }else if(l.relativeCCW(point) < 0){
                bottom.add(point);
            }
        }
        hull.add(leftMost);
        if (!top.isEmpty()) {
            hull.addAll(recursiveQuickHull(l, top));
        }
        hull.add(rightMost);
        if(!bottom.isEmpty()) {
            hull.addAll(recursiveQuickHull(lB, bottom));
        }
        System.out.println("Start \n");
        for (Point2D point: hull) {
            System.out.println(point.getX());
        }
        return hull;
    }



    private List<Point2D> recursiveQuickHull(Line2D lineAB, List<Point2D>pointsAB){
        List<Point2D> result = new ArrayList<>();
        Point2D hullPoint = pointsAB.get(0);
        double temp;
        double dist = 0;
        for (Point2D point: pointsAB) {
                temp = lineAB.ptLineDist(point);
                if(temp > dist){
                    dist = temp;
                    hullPoint = point;
                }
            }
        Line2D left = new Line2D.Double(lineAB.getP1(), hullPoint);
        Line2D right = new Line2D.Double(hullPoint, lineAB.getP2());
        List<Point2D> leftL = new ArrayList<>();
        List<Point2D> rightL = new ArrayList<>();
        for (Point2D point: pointsAB) {
            if(left.relativeCCW(point) > 0){
                leftL.add(point);
            }else if(right.relativeCCW(point) > 0){
                rightL.add(point);
            }
        }
        if(!leftL.isEmpty()) {
            result.addAll(recursiveQuickHull(left, leftL));
            result.add(left.getP2());
        }
        else{
            result.add(left.getP2());
        }
        if(!rightL.isEmpty()) {
            result.addAll(recursiveQuickHull(right, rightL));
        }
        return result;
    }
}
