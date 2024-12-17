package org.pwo.convexhull.Geometry;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PointGenerator {

    public enum DistributionType {
        uniform, gaussian, clustered
    }

    public static List<Point2D> generate(
            int number,
            DistributionType distribution,
            double param1, double param2
    ) {
        List<Point2D> result = new ArrayList<>();
        Random random = new Random();

        switch (distribution) {
            case uniform -> {
                for (int i = 0; i < number; i++) {
                    double span = param2 - param1;
                    Point2D p = new Point2D.Double(
                            random.nextDouble() * span + param1,
                            random.nextDouble() * span + param1
                    );
                    result.add(p);
                }
            }
            case gaussian -> {
                for (int i = 0; i < number; i++) {
                    Point2D p = new Point2D.Double(
                            random.nextGaussian() * param2 + param1,
                            random.nextGaussian() * param2 + param1
                    );
                    result.add(p);
                }
            }
            case clustered -> {
                int clusters = (int) Math.max(1, Math.sqrt((double) number / 10));
                List<Point2D> centroids = new ArrayList<>();
                for (int i = 0; i < clusters; i++) {
                    Point2D c = new Point2D.Double(
                            random.nextDouble() * (param2 - param1) + param1,
                            random.nextDouble() * (param2 - param1) + param1
                    );
                    centroids.add(c);
                }
                for (int i = 0; i < number; i++) {
                    Point2D clusterCenter = centroids.get(random.nextInt(clusters));
                    double x = clusterCenter.getX() + param2 * 0.1 * random.nextGaussian();
                    double y = clusterCenter.getY() + param2 * 0.1 * random.nextGaussian();
                    result.add(new Point2D.Double(x, y));
                }

            }
        }
        return result;
    }

    public static List<Point2D> generateBounded(
            int number,
            DistributionType distribution,
            double param1, double param2,
            double boundX, double boundY
    ) {
        List<Point2D> result = new ArrayList<>();
        Random random = new Random();

        switch (distribution) {
            case uniform -> {
                for (int i = 0; i < number; i++) {
                    double span = param2 - param1;
                    double x = random.nextDouble() * span + param1;
                    double y = random.nextDouble() * span + param1;

                    result.add(new Point2D.Double(fMod(x, boundX), fMod(y, boundY)));
                }
            }
            case gaussian -> {
                for (int i = 0; i < number; i++) {
                    double x = random.nextGaussian() * param2 + param1;
                    double y = random.nextGaussian() * param2 + param1;

                    result.add(new Point2D.Double(fMod(x, boundX), fMod(y, boundY)));
                }
            }
            case clustered -> {
                int clusters = (int) Math.max(1, Math.sqrt((double) number / 10));
                List<Point2D> centroids = new ArrayList<>();
                for (int i = 0; i < clusters; i++) {
                    Point2D c = new Point2D.Double(
                            random.nextDouble() * (param2 - param1) + param1,
                            random.nextDouble() * (param2 - param1) + param1
                    );
                    centroids.add(c);
                }
                for (int i = 0; i < number; i++) {
                    Point2D clusterCenter = centroids.get(random.nextInt(clusters));
                    double x = clusterCenter.getX() + param2 * 0.1 * random.nextGaussian();
                    double y = clusterCenter.getY() + param2 * 0.1 * random.nextGaussian();
                    result.add(new Point2D.Double(fMod(x, boundX), fMod(y, boundY)));
                }

            }
        }
        return result;
    }

    public static double fMod(double a, double b) {
        return  a - Math.floor(a / b) * b;
    }
}
