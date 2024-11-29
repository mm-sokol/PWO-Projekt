package org.pwo.parallel;

import org.pwo.utils.PointUtils;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;


public class GrahamScanTask extends RecursiveTask<List<Point2D>> {

    @Override
    protected List<Point2D> compute() {
        _points.sort(Comparator.comparingDouble(Point2D::getX));

        if (_points.size() > _minimumSize) {
            // split points into groups
            return ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .map(ForkJoinTask::join)
                    .reduce(new ArrayList<>(), this::merge);
        } else {
            // compute GrahamScan
            return scan();
        }
    }

    public GrahamScanTask(List<Point2D> points, int threshold) {
        this._minimumSize = threshold;
        this._points = points;

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
        hull.stream()
                .min(Comparator.comparingDouble(point -> -point.getY()))
                .ifPresent(minPoint -> {
                    // swap it with the first point
                    Point2D firstPoint = hull.getFirst();
                    hull.set(0, minPoint);
                    hull.set(hull.indexOf(minPoint), firstPoint);
                });


        // sort the other points based on polar angle between point and pivot
        hull.subList(1, hull.size()).sort(Comparator.comparingDouble(point -> PointUtils.polarAngle(point, hull.getFirst())));


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

    private List<Point2D> merge(List<Point2D> leftHull, List<Point2D> rightHull) {

        if (leftHull.isEmpty())
            return rightHull;
        if (rightHull.isEmpty())
            return leftHull;


        Point2D rightmostPointFromLeftHull = leftHull.stream()
                .max(Comparator.comparingDouble(Point2D::getX))
                .orElse(null);
        Point2D leftmostPointFromRightHull = rightHull.stream()
                .min(Comparator.comparingDouble((Point2D::getX)))
                .orElse(null);

        int upperTangentP1Index = leftHull.indexOf(rightmostPointFromLeftHull);
        int upperTangentP2Index = rightHull.indexOf(leftmostPointFromRightHull);
        boolean stop = false;
        while(!stop) {
            stop = true;
            while (PointUtils.isCounterClockwise(
                    rightHull.get(upperTangentP2Index),
                    leftHull.get(upperTangentP1Index),
                    leftHull.get((upperTangentP1Index + 1) % leftHull.size())
            )) {
                upperTangentP1Index = (upperTangentP1Index + 1) % leftHull.size();
                stop = false;
            }
            while (PointUtils.isClockwise(
                    leftHull.get(upperTangentP1Index),
                    rightHull.get(upperTangentP2Index),
                    rightHull.get((upperTangentP2Index + 1) % rightHull.size())
            )) {
                upperTangentP2Index = (upperTangentP2Index - 1 + rightHull.size()) % rightHull.size();
                stop= false;
            }
        }


        int lowerTangentP1Index = leftHull.indexOf(rightmostPointFromLeftHull);
        int lowerTangentP2Index = rightHull.indexOf(leftmostPointFromRightHull);
        stop = false;
        while(!stop) {
            stop = true;
            while (PointUtils.isClockwise(
                    rightHull.get(lowerTangentP2Index),
                    leftHull.get(lowerTangentP1Index),
                    leftHull.get((lowerTangentP1Index - 1 + leftHull.size()) % leftHull.size())
            )) {
                lowerTangentP1Index = (lowerTangentP1Index - 1 + leftHull.size()) % leftHull.size();
                stop = false;
            }
            while (PointUtils.isCounterClockwise(
                    leftHull.get(lowerTangentP1Index),
                    rightHull.get(lowerTangentP2Index),
                    rightHull.get((lowerTangentP2Index + 1) % rightHull.size())
            )) {
                lowerTangentP2Index = (lowerTangentP2Index + 1) % rightHull.size();
                stop= false;
            }
        }

        List<Point2D> result = new ArrayList<>();

        for (int i = upperTangentP1Index; i != lowerTangentP1Index ; i = (i+1) % leftHull.size()) {
            result.add(leftHull.get(i));
        }
        result.add(leftHull.get(lowerTangentP1Index));
        for (int i = lowerTangentP2Index; i != upperTangentP2Index ; i = (i+1) % rightHull.size()) {
            result.add(rightHull.get(i));
        }
        result.add(rightHull.get(upperTangentP2Index));

        return result;
    }

    private final int _minimumSize;
    private final List<Point2D> _points;
}
