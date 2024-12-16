package org.pwo.convexhull.Geometry;

import java.awt.geom.Point2D;
import java.util.List;

public class PointScaler {

    private final double _canvasWidth;
    private final double _canvasHeight;

    private double _xOffset = 50;  // Horizontal offset
    private double _yOffset = 50;  // Vertical offset
    private double _scale = 1.0;  // scaling
    private double _centerX;
    private double _centerY;

    public PointScaler(double _canvasWidth, double _canvasHeight) {
        this._canvasWidth = _canvasWidth;
        this._canvasHeight = _canvasHeight;
    }

    public void calculateOffsetsAndScale(List<Point2D> points) {
        if (points.size()<2)
            return;

        double minX=Double.MAX_VALUE, minY=Double.MAX_VALUE, maxX=Double.MIN_VALUE, maxY=Double.MIN_VALUE;
        for (Point2D p : points) {
            minX = Math.min(p.getX(), minX);
            minY = Math.min(p.getY(), minY);
            maxX = Math.max(p.getX(), maxX);
            maxY = Math.max(p.getY(), maxY);
        }
        System.out.println("X: ["+minX+", "+maxX+"] x Y: ["+minY+", "+maxY+"]");

        double width = maxX - minX;
        double height = maxY - minY;

        _centerX = width / 2;
        _centerY = height / 2;

        _xOffset = _canvasWidth / 2;
        _yOffset = _canvasHeight / 2;

        double fillFactor = 0.8;

        // Calculate the scaling factor based on the largest dimension
        double scaleX = (_canvasWidth) / width;
        double scaleY = (_canvasHeight) / height;

        // Choose the smaller scaling factor to maintain the aspect ratio
        _scale = Math.min(scaleX, scaleY) * fillFactor;
    }

    public Point2D transformToScale(Point2D point) {

        System.out.println("Point:   ["+point.getX()+", "+point.getY()+"]");
        System.out.println("Offsets: ["+_xOffset+", "+_yOffset+"]");
        System.out.println("Scale:   "+_scale);

        Point2D tran = new Point2D.Double(
                (point.getX() - _centerX) * _scale + _xOffset,
                ((point.getY() - _centerY) * _scale) + _yOffset
        );
        System.out.println("Transformed: ["+tran.getX()+", "+tran.getY()+"]\n");

        return tran;
    }
}
