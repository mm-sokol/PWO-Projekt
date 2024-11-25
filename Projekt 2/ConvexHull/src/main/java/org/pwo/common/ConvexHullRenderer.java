package org.pwo.common;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class ConvexHullRenderer extends JPanel {

    public ConvexHullRenderer(List<Point2D> points, int convexHullElements) {
        this._points = points;
        this._convexHullElements = convexHullElements;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



        for (int i = 0; i < _points.size(); i++) {

            g2d.setColor(Color.darkGray);
            if (i>0 && i<_convexHullElements) {
                Point2D end = _points.get(i);
                Point2D start = _points.get(i-1);

                g2d.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
            }

            int x = (int) _points.get(i).getX();
            int y = (int) _points.get(i).getY();
            g2d.setColor(Color.cyan);
            g2d.fillOval(x-5, y-5, 10, 10);

        }
    }

    private final List<Point2D> _points;
    private final int _convexHullElements;
}
