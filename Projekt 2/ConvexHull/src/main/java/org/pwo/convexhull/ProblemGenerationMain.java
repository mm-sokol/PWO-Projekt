package org.pwo.convexhull;

import org.pwo.convexhull.Geometry.PointOperations;
import org.pwo.convexhull.Geometry.PointGenerator;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

public class ProblemGenerationMain {

//    C:\Users\MS\Desktop\PWO\Projekt\Projekt 2\ConvexHull\src\main\resources\problem10.txt

    // Problem sizes
    // small: number < 50
    // medium: 100 < number < 1000
    // large: 2000 < number < 10000
    // very_large: number > 2000

    public static void main(String[] args) {

        int num = 10;
        int start = 1;
        String size = "extremely_large";
        Random random = new Random();
        int min = 120_000;
        int max = 200_000;

        for (int version = start; version < (start + num); version++) {
            String filename = "C:\\Users\\MS\\Desktop\\PWO\\Projekt\\Projekt 2\\ConvexHull\\src\\main\\resources\\problems\\" +
                     size + "\\clustered" + version + "_" + size + ".txt";

            List<Point2D> problem = PointGenerator.generateBounded( random.nextInt(max - min + 1) + min,
                    PointGenerator.DistributionType.clustered,
                    120, 600,
                    1600, 1000
            );

            try {
                PointOperations.saveToFile(problem, filename);
            } catch (Exception ex) {
                System.out.println("Something went wrong: "+ ex.getMessage());
            }
        }
    }
}
