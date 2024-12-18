package org.pwo.convexhull;

import org.pwo.convexhull.Geometry.PointOperations;
import org.pwo.convexhull.Geometry.PointGenerator;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

public class ProblemGenerationMain {
    // Problem sizes
    // small: number < 50
    // medium: 100 < number < 1000
    // large: 2000 < number < 10000
    // very_large: number > 2000

    public static void main(String[] args) {

        int num = 10;
        int start = 1;
        String size = "medium";
        Random random = new Random();
        int min = 100;
        int max = 1000;

        for (int version = start; version < (start + num); version++) {
            String filename = ".\\src\\main\\resources\\problems\\" +
                     size + "\\gaussian" + version + "_" + size + ".txt";

            List<Point2D> problem = PointGenerator.generateBounded( random.nextInt(max - min + 1) + min,
                    PointGenerator.DistributionType.clustered,
                    200, 320,
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
