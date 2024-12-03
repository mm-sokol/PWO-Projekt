package org.pwo.utils;

import org.junit.jupiter.api.Test;
import java.awt.geom.Point2D;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PointUtilsTest {

    private Random _random;
    private double _eps;

    public PointUtilsTest() {
        _random = new Random();
        _eps = Double.MIN_VALUE * 10;
    }

    private Point2D _randomPointOn2DLine(Point2D a, Point2D b) {
        double slope =  (b.getY() - a.getY()) / (b.getX() - a.getX());
        double y_intercept = a.getY() - slope * a.getX();

        double x = _random.nextDouble();
        double y = slope*x + y_intercept;
        return new Point2D.Double(x, y);
    }

    private Point2D _perpendicularUnitVector(Point2D a, Point2D b) {
        // Calculate direction vector of the line
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();

        // Calculate the perpendicular vector pointing to the right
        double perpX = dy;       // Swap components
        double perpY = -dx;      // Negate the second component

        // Normalize the perpendicular vector
        double length = Math.sqrt(perpX * perpX + perpY * perpY);
        perpX /= length;
        perpY /= length;

        // Compute the new point to the right of the line
        return new Point2D.Double(perpX, perpY);
    }

    @Test
    void counterClockwise() {

        // TODO: change random point coordinates to complete the test
        Point2D a = new Point2D.Double(_random.nextDouble(), _random.nextDouble());
        Point2D b = new Point2D.Double(_random.nextDouble(), _random.nextDouble());

        for (int i = 0; i < 25; i++) {

            Point2D perp = _perpendicularUnitVector(a, b);
            Point2D counterClockwisePoint = new Point2D.Double(
                    b.getX() + perp.getX() * Math.abs(_random.nextDouble()) + _eps,
                    b.getY() + perp.getY() * Math.abs(_random.nextDouble()) + _eps
            );
            Point2D clockwisePoint = new Point2D.Double(
                    b.getX() - perp.getX() * Math.abs(_random.nextDouble()) - _eps,
                    b.getY() - perp.getY() * Math.abs(_random.nextDouble()) - _eps
            );
            Point2D collinearPoint = _randomPointOn2DLine(a, b);

            assert PointUtils.counterClockwise(a, b, counterClockwisePoint) < 0;
            assert PointUtils.counterClockwise(a, b, clockwisePoint) > 0;

            assertEquals(0, PointUtils.counterClockwise(a, b, collinearPoint), 1e-10, "Values are not equal within tolerance");
        }

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