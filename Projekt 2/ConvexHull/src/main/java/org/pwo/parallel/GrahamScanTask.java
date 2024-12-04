package org.pwo.parallel;

import org.pwo.utils.PointUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;


public class GrahamScanTask extends RecursiveTask<List<Point2D>> {

    private static final Object _lock = new Object();

    @Override
    public List<Point2D> compute() {

        if (_points.size() < 2) {
            return _points;
        } else if (_points.size() < 3) {
            if (_points.getFirst().getY()>_points.getLast().getY()) {
                Point2D first = _points.getFirst();
                _points.add(0, _points.getLast());
                _points.add(1, first);
            }
            return _points;
//        } else if (_points.size() == 3) {
//            _points.sort(Comparator.comparingDouble(p -> -p.getY()));
//            var first = _points.getFirst();
//            _points.subList(1, _points.size()).sort(Comparator.comparingDouble(p -> PointUtils.polarAngle(p, first)));
//            return _points;

        } else if (_points.size() > _minimumSize) {
            // split points into groups
//           return ForkJoinTask.invokeAll(createSubtasks())
//                   .stream()
//                   .map(ForkJoinTask::join)
//                   .reduce(new ArrayList<>(), this::merge);

            int middle = this._points.size() / 2;
//                System.out.println("Sublist left:");
//                printPointList(this._points.subList(0, middle));

            GrahamScanTask left = new GrahamScanTask(
                    new ArrayList<>(this._points.subList(0, middle)),
                    _minimumSize
            );

//            System.out.println("Sublist right:");
//            printPointList(this._points.subList(middle, this._points.size()));

            GrahamScanTask right = new GrahamScanTask(
                    new ArrayList<>(this._points.subList(middle, this._points.size())),
                    _minimumSize
            );
            left.fork();
//            right.invoke();
            List<Point2D> leftHull;
            List<Point2D> rightHull;
            final List<Point2D> merge;


            System.out.println("Merging");
            leftHull = left.join();
            printPointList(leftHull);
            rightHull = right.invoke();
            System.out.println("--------------------------------");
            printPointList(rightHull);
            System.out.println("Done merging;");
            merge = merge(leftHull, rightHull);
            printPointList(merge);
            System.out.println("<<-----------------------------");


            return merge;

        } else {
            // compute GrahamScan
            return scan();
        }
    }

    public GrahamScanTask(List<Point2D> points, int threshold) {
        this._minimumSize = threshold;
        this._points = points;
        this._points.sort(Comparator.comparingDouble(Point2D::getY));
        this._points.sort(Comparator.comparingDouble(Point2D::getX));
    }

    private Collection<GrahamScanTask> createSubtasks() {
        List<GrahamScanTask> tasks = new ArrayList<>();

        int middle = this._points.size() / 2;

        tasks.add(new GrahamScanTask(
                new ArrayList<>(this._points.subList(0, middle)),
                _minimumSize
        ));

        tasks.add(new GrahamScanTask(
                new ArrayList<>(this._points.subList(middle, this._points.size())),
                _minimumSize
        ));
        return tasks;
    }

    private List<Point2D> scan() {
        List<Point2D> hull = new java.util.ArrayList<>(this._points.stream()
                .distinct()
                .toList());

        // get the lowest point in Y direction
        hull = hull.stream().distinct()
                .sorted(Comparator.comparingDouble(p->-p.getY()))
                .collect(Collectors.toList());
//                .ifPresent(minPoint -> {
//                    // swap it with the first point
//                    Point2D firstPoint = hull.getFirst();
//                    hull.set(0, minPoint);
//                    hull.set(hull.indexOf(minPoint), firstPoint);
//                });

        Point2D first = hull.getFirst();

        // sort the other points based on polar angle between point and pivot
        hull.subList(1, hull.size()).sort(Comparator.comparingDouble(point -> PointUtils.polarAngle(point, first)));


        int vecEnd = 1;
        for (int i = 2; i < hull.size(); i++) {

            // while hull[i] is collinear or to the right of vector [hull[vecEnd-1], hull[vecEnd]]
            while (PointUtils.counterClockwise(hull.get(vecEnd-1), hull.get(vecEnd), hull.get(i))<=0) {

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

    public List<Point2D> merge(List<Point2D> leftHull, List<Point2D> rightHull) {
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

    private int nextIndex(int index, int size) {
        return (index + 1) % size;
    }

    private int previousIndex(int index, int size) {
        return (index - 1 + size) % size;
    }

    private int findRightmostPoint(List<Point2D> points) {
        int rightmost = 0;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX() > points.get(rightmost).getX()) {
                rightmost = i;
            }
        }
        return rightmost;
    }

    private int findLeftmostPoint(List<Point2D> points) {
        int leftmost = 0;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX() < points.get(leftmost).getX()) {
                leftmost = i;
            }
        }
        return leftmost;
    }

    private void printPointList(List<Point2D> points) {
        points.forEach(p->System.out.println(p.getX()+", "+p.getY()));
    }

    public record Tuple<A, B>(A first, B second) {}

    private Tuple<Integer,Integer> findLowerTangent(List<Point2D> leftHull, List<Point2D> rightHull) {
        int leftIndex = findRightmostPoint(leftHull);
        int rightIndex = findLeftmostPoint(rightHull);

        boolean stop = false;
        while (!stop) {
            stop = true;
            while (PointUtils.isCounterClockwise(
                    rightHull.get(rightIndex),
                    leftHull.get(leftIndex),
                    leftHull.get(previousIndex(leftIndex, leftHull.size())))) {
                leftIndex = previousIndex(leftIndex, leftHull.size());
                stop = false;
            }
            while (PointUtils.isClockwise(
                    leftHull.get(leftIndex),
                    rightHull.get(rightIndex),
                    rightHull.get(nextIndex(rightIndex, rightHull.size())))) {
                rightIndex = nextIndex(rightIndex, rightHull.size());
                stop = false;
            }
        }

        return new Tuple<>(leftIndex, rightIndex);
    }

    private Tuple<Integer, Integer> findUpperTangent(List<Point2D> leftHull, List<Point2D> rightHull) {
        int leftIndex = findRightmostPoint(leftHull);
        int rightIndex = findLeftmostPoint(rightHull);

        boolean stop = false;
        while(!stop) {
            stop = true;
            while (PointUtils.isClockwise(
                    rightHull.get(rightIndex),
                    leftHull.get(leftIndex),
                    leftHull.get(nextIndex(leftIndex, leftHull.size()))
            )) {
                leftIndex = nextIndex(leftIndex, leftHull.size());
                stop = false;
            }
            while (PointUtils.isCounterClockwise(
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

    private final int _minimumSize;
    private final List<Point2D> _points;
}
