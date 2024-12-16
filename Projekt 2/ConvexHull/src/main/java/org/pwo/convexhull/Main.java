package org.pwo.convexhull;

import org.pwo.convexhull.Geometry.PointOperations;
import org.pwo.convexhull.Geometry.PointGenerator;

import java.awt.geom.Point2D;
import java.util.List;

public class Main {

//    C:\Users\MS\Desktop\PWO\Projekt\Projekt 2\ConvexHull\src\main\resources\problem10.txt
    public static void main(String[] args) {

        int version = 11;
        String filename = "C:\\Users\\MS\\Desktop\\PWO\\Projekt\\Projekt 2\\ConvexHull\\src\\main\\resources\\problem" + version + ".txt";

        List<Point2D> problem = PointGenerator.generate( 600,
                PointGenerator.DistributionType.clustered,
                0,
                1200
        );

        try {
            PointOperations.saveToFile(problem, filename);
        } catch (Exception ex) {
            System.out.println("Something went wrong :(");
        }
    }
}
