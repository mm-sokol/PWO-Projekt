package org.pwo.convexhull;

import org.pwo.convexhull.Algorithm.GrahamScan;
import org.pwo.convexhull.Algorithm.GrahamScanTask;
import org.pwo.convexhull.Geometry.PointOperations;

import java.awt.geom.Point2D;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class TimerMain {

    public static void differentThresholds(List<Point2D> points, int trials) {
        try (ForkJoinPool pool = new ForkJoinPool()) {

            List<Point2D> result = new ArrayList<>();
            Instant start, end;
            Duration duration;
            timeSequential(points, trials);

            int threshold = 2;
            for (int j = 0; j<25; j++) {
                threshold *= 2;
                GrahamScanTask task = new GrahamScanTask(threshold, points);

                Duration total = Duration.ZERO;
                for (int t=0; t<trials; t++) {
                    start = Instant.now();
                    result = pool.invoke(task);
                    end = Instant.now();
                    total = total.plus(Duration.between(start, end));
                }

                long avgMs = total.toMillis() / trials;
                System.out.printf("[Threshold %10d] Solving took: %d ms%n", threshold, avgMs);
            }


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void differentThreadNum(List<Point2D> points, int trials) {
        try {
            List<Point2D> result = new ArrayList<>();
            Instant start, end;
            Duration duration;
            timeSequential(points, trials);

            int threadNum = 2;
            for (int i = 0; i<12; i++) {
                threadNum *=2;
                try (ForkJoinPool pool = new ForkJoinPool(
                        threadNum
                )) {
                    GrahamScanTask task = new GrahamScanTask(24, points);

                    Duration total = Duration.ZERO;
                    for (int t=0; t<trials; t++) {
                        start = Instant.now();
                        result = pool.invoke(task);
                        end = Instant.now();
                        total = total.plus(Duration.between(start, end));
                    }

                    long avgMs = total.toMillis() / trials;
                    System.out.printf("[Threads %6d] Solving took: %d ms%n", threadNum, avgMs);
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void timeSequential(List<Point2D>  points, int trials) {
        List<Point2D> result = new ArrayList<>();
        Duration total = Duration.ZERO;

        for (int i=0; i<trials; i++) {
            Instant start = Instant.now();
            result = GrahamScan.scan(points);
            Instant end = Instant.now();
            total = total.plus(Duration.between(start, end));
        }

        long avgMs = total.toMillis() / trials;
        System.out.println("[Sequential ] Solving took: " + avgMs + " ms");
    }


    public static void main(String[] args) {
        try {
            Path problem = Paths.get("C:\\Users\\MS\\Desktop\\PWO\\Projekt\\Projekt 2\\ConvexHull\\src\\main\\resources\\problems\\extremely_large\\clustered10_extremely_large.txt");
            List<Point2D> points = PointOperations.readFromFile(problem.toString());

            differentThreadNum(points, 10);

        } catch (Exception e) {
            System.out.println("Error occurred: "+e.getMessage());
        }
    }
}
