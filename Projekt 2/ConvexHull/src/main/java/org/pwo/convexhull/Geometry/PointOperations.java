package org.pwo.convexhull.Geometry;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointOperations {
    // private static final Logger logger = LoggerFactory.getLogger(PointOperations.class);

    public static List<Point2D> readFromFile(String filename) {
        List<Point2D> list = new ArrayList<Point2D>();


        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                System.out.println("We are reading file: "+filename);
                String line = scanner.nextLine();
                String[] elements = line.split("\\s+");

                double x = Double.parseDouble(elements[0]);
                double y = Double.parseDouble(elements[1]);

                list.add(new Point2D.Double(x, y));
            }
        }
        catch (IOException ioe) {
           System.out.println("IO Error occured: " + ioe.getMessage());
            // logger.error("IO Error occured: {}", ioe.getMessage());
        }
        catch (Exception e) {
           System.out.println("Error occured: " + e.getMessage());
            // logger.error("Error occured: {}", e.getMessage());
        }
        return list;
    }

    public static void saveToFile(List<Point2D> points, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Point2D point : points) {
                writer.write(point.getX()+"\t\t"+point.getY());
                writer.newLine();
            }
        }
    }



    /// if the result is 0: a, b, c are collinear
    /// if the result is positive: c lies to the left of ab vector, and we have a counter-clockwise turn
    /// if the result is negative: c lies to the right of ab vector, and we have a clockwise turn
    /// ---
    /// @param a: starting point of a vector ab
    /// @param b: ending point of a vector ab
    /// @param c: point for testing
    /// @return Relative orientation of points a, b and c
    public static double counterClockwise(Point2D a, Point2D b, Point2D c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());
    }


    /// Determines if the points a, b, and c form a counter-clockwise turn.
    /// This method uses the cross product of vectors formed by the points a, b, and c to determine
    /// the orientation of the turn. If the result is positive, the points form a counter-clockwise
    /// turn; otherwise, they form a clockwise turn or are collinear.
    /// ---
    /// @param a The first point (Point2D).
    /// @param b The second point (Point2D).
    /// @param c The third point (Point2D).
    /// @return true if the points a, b, and c form a counter-clockwise turn, false otherwise.
    public static boolean isCounterClockwise(Point2D a, Point2D b, Point2D c) {
        return counterClockwise(a, b, c) > 0;
    }
    public static boolean isClockwise(Point2D a, Point2D b, Point2D c) {
        return counterClockwise(a, b, c) < 0;
    }


    /// Calculates the polar angle (in radians) between the line from the origin to the pivot point and the positive x-axis.
    /// ---
    /// @param origin The origin point (Point2D).
    /// @param pivot The pivot point (Point2D) whose polar angle is to be calculated.
    /// @return A double representing the polar angle in radians between the positive x-axis and the line connecting the origin and the pivot point.</returns>
    public static double polarAngle(Point2D origin, Point2D pivot) {
        return Math.atan2(pivot.getY() - origin.getY(), pivot.getX() - origin.getX());
    }

}
