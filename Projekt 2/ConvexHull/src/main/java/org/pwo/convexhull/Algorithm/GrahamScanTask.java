package org.pwo.convexhull.Algorithm;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import java.util.concurrent.RecursiveTask;


public class GrahamScanTask extends RecursiveTask<List<Point2D>> {

    private final int _minimumSize;
    private final List<Point2D> _points;

    @Override
    public List<Point2D> compute() {
        if (_points.size() <= _minimumSize) {
            return GrahamScan.scan(this._points);
        } else {
            // Divide the points into 2 groups based on the x coordiantes
            int middle = this._points.size() / 2;

            GrahamScanTask left = new GrahamScanTask(
                    _minimumSize,
                    new ArrayList<Point2D>(this._points.subList(0, middle))
            );
            GrahamScanTask right = new GrahamScanTask(
                    _minimumSize,
                    new ArrayList<Point2D>(this._points.subList(middle, this._points.size()))
            );

            left.fork();

            List<Point2D> leftHull = left.join();
            List<Point2D> rightHull = right.invoke();
            return GrahamScan.merge(leftHull, rightHull);
        }
    }

    public GrahamScanTask(int _minimumSize, List<Point2D> _points) {
        this._minimumSize = _minimumSize;
        this._points = _points;
    }

}
