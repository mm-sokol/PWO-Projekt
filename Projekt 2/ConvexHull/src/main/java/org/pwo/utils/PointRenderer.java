package org.pwo.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PointRenderer extends JFrame {

    public static class ProblemPanel extends JPanel {
        public ProblemPanel(List<Point2D> points, Color pointColor, List<Point2D> lines, Color linesColor, int pointSize) {
            this._points = points;
            this._pointColor = pointColor;
            this._lines = lines;
            this._linesColor = linesColor;
            this._pointSize = pointSize;
            calculateOffset();
        }

        public ProblemPanel(List<Point2D> points, Color pointColor, int pointSize) {
            this._points = points;
            this._pointColor = pointColor;
            this._lines = new ArrayList<>();
            this._linesColor = null;
            this._pointSize = pointSize;
            calculateOffset();
        }

        public void addLines(List<Point2D> lines, Color linesColor, int lineWidth) {
            this._lines = lines;
            this._linesColor = linesColor;
            this._lineWidth = lineWidth;
            repaint();
        }

        // Zoom function for mouse wheel
        public void zoom(int rotation) {
            if (rotation < 0) {
                _scale *= 1.1; // Zoom in
            } else {
                _scale /= 1.1; // Zoom out
            }
            repaint(); // re-render the panel after zoom
        }

        public void calculateOffset() {
            double minX=Double.MAX_VALUE, minY=Double.MAX_VALUE, maxX=Double.MIN_VALUE, maxY=Double.MIN_VALUE;
            for (Point2D p : _points) {
                minX = Math.min(p.getX(), minX);
                minY = Math.min(p.getY(), minY);
                maxX = Math.max(p.getX(), maxX);
                maxY = Math.max(p.getY(), maxY);
            }
            double width = maxX - minX;
            double height = maxY - minY;
            double panelWidth = getWidth();
            double panelHeight = getHeight();
            double scaleX = panelWidth / width;
            double scaleY = panelHeight / height;
            _scale = Math.min(scaleX, scaleY) * 0.8; // Use 80% of available space

            // Calculate offsets to center the bounding box
            _offsetX = (panelWidth - width * _scale) / 2 - minX * _scale;
            _offsetY = (panelHeight - height * _scale) / 2 - minY * _scale;

            repaint(); // Re-render the panel after calculations
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;

            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.scale(_scale, _scale);
            graphics2D.translate(_offsetX, _offsetY);

            if (!_points.isEmpty() && _pointColor != null) {
                graphics2D.setColor(_pointColor);
                for (Point2D p : _points) {
                    graphics2D.fillOval(
                            ((int) p.getX())-(_pointSize/2),
                            ((int) p.getY())-(_pointSize/2),
                            _pointSize,
                            _pointSize
                    );
                }
            }

            if (!_lines.isEmpty() && _lines.size() > 1 && _linesColor != null) {
                graphics2D.setColor(_linesColor);
                graphics2D.setStroke(new BasicStroke(_lineWidth));
                for (int i=1; i < _lines.size(); i++) {
                    graphics2D.drawLine(
                            ((int) _lines.get(i - 1).getX()), 
                            ((int) _lines.get(i - 1).getY()),
                            ((int) _lines.get(i).getX()),
                            ((int) _lines.get(i).getY())
                    );
                }
                graphics2D.drawLine(
                        ((int) _lines.getFirst().getX()),
                        ((int) _lines.getFirst().getY()),
                        ((int) _lines.getLast().getX()),
                        ((int) _lines.getLast().getY())
                );
            }

            calculateOffset();
        }

        private final List<Point2D> _points;
        private final Color _pointColor;
        private List<Point2D> _lines;
        private Color _linesColor;
        private double _scale = 1.0;
        private double _offsetX = 0.;
        private double _offsetY = 0.;
        private int _pointSize = 20;
        private int _lineWidth = 4;
    }

    private final ProblemPanel _panel;
    private final JScrollPane _scrollPane;

    public PointRenderer(String title, List<Point2D> points, Color color, int pointSize) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        this._panel = new ProblemPanel(points, color, pointSize);
        this._scrollPane = new JScrollPane(this._panel);
        add(_scrollPane);
        add(this._panel);

        this._panel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                _panel.zoom(e.getWheelRotation());
            }
        });

        setVisible(true);
    }

    public void addLines(List<Point2D> lines, Color color, int lineWidth) {
        this._panel.addLines(lines, color, lineWidth);
    }

}
