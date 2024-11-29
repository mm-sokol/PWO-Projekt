package org.pwo.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class PointRenderer extends JFrame {

    public PointRenderer(String title, List<Point2D> points, List<Point2D> lines) {
        super(title);
        this._points = points;
        this._lines = lines;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);


        add(new ProblemPanel(_points, Color.blue));
        setVisible(true);
    }

    private List<Point2D> _points;
    private List<Point2D> _lines;


    public class ProblemPanel extends JPanel {
        public ProblemPanel(List<Point2D> points, Color color) {
            this._points = points;
            this._color = color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;

            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        private List<Point2D> _points;
        private Color _color;
    }

    public class SolutionPanel extends JPanel {

    }

}
