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

    public static void differentThresholds(List<Point2D> points, int thresholdCounts, int trials) {
        try (ForkJoinPool pool = new ForkJoinPool()) {

            List<Point2D> result = new ArrayList<>();
            Instant start, end;
            Duration duration;
            timeSequential(points, trials);

            int threshold = 2;
            for (int j = 0; j<thresholdCounts; j++) {
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

    public static void differentThreadNum(List<Point2D> points, int threadCounts, int increment, int trials) {
        try {
            List<Point2D> result = new ArrayList<>();
            Instant start, end;
            Duration duration;
            timeSequential(points, trials);

            int threadNum = 2;
            for (int i = 0; i<threadCounts; i++,threadNum*=increment) {

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
                    System.out.printf("[Threads   %10d] Solving took: %d ms%n", threadNum, avgMs);
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
        System.out.println("[Sequential          ] Solving took: " + avgMs + " ms");
    }


    public static void main(String[] args) {
        try {
            Path problem = Paths.get(".\\src\\main\\resources\\problems\\extremely_large\\clustered1_extremely_large.txt");
            List<Point2D> points = PointOperations.readFromFile(problem.toString());
            System.out.println("Problem size: "+points.size());
            System.out.println("-------------------------------------------------------");
            differentThreadNum(points, 12, 2,20);
            System.out.println("-------------------------------------------------------");
            differentThresholds(points, 20, 20);

        } catch (Exception e) {
            System.out.println("Error occurred: "+e.getMessage());
        }
    }
}
