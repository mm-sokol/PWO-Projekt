package org.pwo.utils;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

class PointUtilsTest {

    private double _line2D(double x, double a, double b) {
        return a*x + b;
    }

    @Test
    void counterClockwise() {

        // TODO: change random point coordinates to complete the test
        Point2D a = new Point2D.Double(12, 90);
        Point2D b = new Point2D.Double(29., 102.);

        Point2D collinearPoint = new Point2D.Double(1, 2);
        Point2D counterClockwisePoint = new Point2D.Double(1, 2);
        Point2D clockwisePoint = new Point2D.Double(1, 2);

        assert PointUtils.counterClockwise(a, b, collinearPoint) == 0;
        assert PointUtils.counterClockwise(a, b, counterClockwisePoint) < 0;
        assert PointUtils.counterClockwise(a, b, clockwisePoint) > 0;

    }

    @Test
    void isCounterClockwise() {
    }

    @Test
    void isClockwise() {
    }

    @Test
    void polarAngle() {
    }

    @Test
    void readList() {
    }
}