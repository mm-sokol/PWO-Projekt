package org.pwo.convexhull.Algorithm;

import org.pwo.convexhull.Geometry.PointOperations;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GrahamScan {

    public record Tuple<A, B>(A first, B second) {}
    
    public static List<Point2D> scan(List<Point2D> points) {
        List<Point2D> hull = new java.util.ArrayList<>(points.stream()
                .distinct()
                .toList());

        // get the lowest point in Y direction
        hull = hull.stream().distinct()
                .sorted(Comparator.comparingDouble(p->-p.getY()))
                .collect(Collectors.toList());

        Point2D first = hull.getFirst();

        // sort the other points based on polar angle between point and pivot
        hull.subList(1, hull.size()).sort(Comparator.comparingDouble(point -> PointOperations.polarAngle(point, first)));


        int vecEnd = 1;
        for (int i = 2; i < hull.size(); i++) {

            // while hull[i] is collinear or to the right of vector [hull[vecEnd-1], hull[vecEnd]]
            while (PointOperations.counterClockwise(hull.get(vecEnd-1), hull.get(vecEnd), hull.get(i))<=0) {

                if (vecEnd>1) { // check different vector
                    vecEnd--;
                } else if (vecEnd==1) { // there's nothing we can do if vecEnd==1
                    break;
                } else { // check the next point
                    i++;
                }
            }
            vecEnd++;


            Point2D point = hull.get(i);
            hull.set(i, hull.get(vecEnd));
            hull.set(vecEnd, point);
        }
        return hull.subList(0, vecEnd+1);
    }

    public static List<Point2D> merge(List<Point2D> leftHull, List<Point2D> rightHull) {
        if (leftHull.isEmpty())
            return rightHull;
        if (rightHull.isEmpty())
            return leftHull;

//        System.out.println("Left hull -----------------------------------");
//        printPointList(leftHull);
//        System.out.println("Right hull -----------------------------------");
//        printPointList(rightHull);

        Tuple<Integer, Integer> upperTangent = findUpperTangent(leftHull, rightHull);
        int upperTangentP1 = upperTangent.first;
        int upperTangentP2 = upperTangent.second;
//        System.out.println("Upper tangent: P1("+leftHull.get(upperTangentP1)+") -> ("+rightHull.get(upperTangentP2)+")");

        Tuple<Integer, Integer> lowerTangent = findLowerTangent(leftHull, rightHull);
        int lowerTangentP1 = lowerTangent.first;
        int lowerTangentP2 = lowerTangent.second;
//        System.out.println("Lower tangent: P1("+leftHull.get(lowerTangentP1)+") -> ("+rightHull.get(lowerTangentP2)+")");

        List<Point2D> result = new ArrayList<>();
        // Add points from left hull
        for (int i = upperTangentP1; i != nextIndex(lowerTangentP1, leftHull.size()); i = nextIndex(i, leftHull.size())) {
            result.add(leftHull.get(i));
        }
        // Add points from right hull
        for (int i = lowerTangentP2; i != nextIndex(upperTangentP2, rightHull.size()); i = nextIndex(i, rightHull.size())) {
            result.add(rightHull.get(i));
        }

        return result;
    }



    public static Tuple<Integer, Integer> findUpperTangent(List<Point2D> leftHull, List<Point2D> rightHull) {
        int leftIndex = findRightmostPoint(leftHull);
        int rightIndex = findLeftmostPoint(rightHull);

        boolean stop = false;
        while(!stop) {
            stop = true;
            while (PointOperations.isClockwise(
                    rightHull.get(rightIndex),
                    leftHull.get(leftIndex),
                    leftHull.get(nextIndex(leftIndex, leftHull.size()))
            )) {
                leftIndex = nextIndex(leftIndex, leftHull.size());
                stop = false;
            }
            while (PointOperations.isCounterClockwise(
                    leftHull.get(leftIndex),
                    rightHull.get(rightIndex),
                    rightHull.get(previousIndex(rightIndex, rightHull.size()))
            )) {
                rightIndex = previousIndex(rightIndex, rightHull.size());
                stop= false;
            }
        }
        return new Tuple<>(leftIndex, rightIndex);
    }

    public static Tuple<Integer,Integer> findLowerTangent(List<Point2D> leftHull, List<Point2D> rightHull) {
        int leftIndex = findRightmostPoint(leftHull);
        int rightIndex = findLeftmostPoint(rightHull);

        boolean stop = false;
        while (!stop) {
            stop = true;
            while (PointOperations.isCounterClockwise(
                    rightHull.get(rightIndex),
                    leftHull.get(leftIndex),
                    leftHull.get(previousIndex(leftIndex, leftHull.size())))) {
                leftIndex = previousIndex(leftIndex, leftHull.size());
                stop = false;
            }
            while (PointOperations.isClockwise(
                    leftHull.get(leftIndex),
                    rightHull.get(rightIndex),
                    rightHull.get(nextIndex(rightIndex, rightHull.size())))) {
                rightIndex = nextIndex(rightIndex, rightHull.size());
                stop = false;
            }
        }

        return new Tuple<>(leftIndex, rightIndex);
    }



    private static int findRightmostPoint(List<Point2D> points) {
        int rightmost = 0;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX() > points.get(rightmost).getX()) {
                rightmost = i;
            }
        }
        return rightmost;
    }

    private static int findLeftmostPoint(List<Point2D> points) {
        int leftmost = 0;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX() < points.get(leftmost).getX()) {
                leftmost = i;
            }
        }
        return leftmost;
    }



    private static int nextIndex(int index, int size) {
        return (index + 1) % size;
    }

    private static int previousIndex(int index, int size) {
        return (index - 1 + size) % size;
    }
}
