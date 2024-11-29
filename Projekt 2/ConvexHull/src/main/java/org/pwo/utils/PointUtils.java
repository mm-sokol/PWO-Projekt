package org.pwo.utils;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PointUtils {

    /// @param a: starting point of a vector ab
    /// @param b: ending point of a vector ab
    /// @param c: point for testing
    /// @return Relative orientation of points a, b and c
    /// if the result is 0: a, b, c are collinear
    /// if the result is positive: c lies to the left of ab vector, and we have a counter-clockwise turn
    /// if the result is negative: c lies to the right of ab vector, and we have a clockwise turn
    public static double counterClockwise(Point2D a, Point2D b, Point2D c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    public static boolean isCounterClockwise(Point2D a, Point2D b, Point2D c) {
        return counterClockwise(a, b, c) > 0;
    }
    public static boolean isClockwise(Point2D a, Point2D b, Point2D c) {
        return counterClockwise(a, b, c) < 0;
    }

    public static double polarAngle(Point2D origin, Point2D pivot) {
        return Math.atan2(pivot.getY() - origin.getY(), pivot.getX() - origin.getX());
    }

    public static List<Point2D> readList(String filename) {
        List<Point2D> list = new ArrayList<Point2D>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] elements = line.split("\\s+");
//                System.out.println(Arrays.toString(elements));

                double x = Double.parseDouble(elements[1]);
                double y = Double.parseDouble(elements[2]);

                list.add(new Point2D.Double(x, y));
            }
        }
        catch (IOException ioe) {
            System.out.println("IO Error occured: " + ioe.getMessage());
            ioe.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Error occured: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

}
