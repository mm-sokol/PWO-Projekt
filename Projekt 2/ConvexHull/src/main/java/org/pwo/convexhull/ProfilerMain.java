package org.pwo.convexhull;

import org.pwo.convexhull.Algorithm.GrahamScanTask;
import org.pwo.convexhull.Geometry.PointOperations;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class ProfilerMain {


    public static void main(String[] args) {

        Path rootDir = Paths.get(".\\src\\main\\resources\\problems");

        try (ForkJoinPool pool = new ForkJoinPool()) {

            while (true) {
                try (Stream<Path> paths = Files.walk(rootDir)) {
                    paths.filter(Files::isRegularFile)
                            .filter(path -> path.toString().endsWith(".txt"))
                            .forEach(path -> {
                                System.out.println("ConvexHull task for: " + path);
                                List<Point2D> points = PointOperations.readFromFile(path.toString());
                                GrahamScanTask task = new GrahamScanTask(12, points);
                                List<Point2D> results = pool.invoke(task);
//                                for (Point2D p : results) {
//                                    System.out.println(
//                                            "(" + p.getX() + ", " + p.getY() + ")"
//                                    );
//                                }
                            });

//                    Thread.sleep(1000);
                } catch (IOException e) {
                    throw new RuntimeException("IO error: " + e.getMessage());
//                } catch (InterruptedException ie) {
//                    throw new RuntimeException("Interruption: " + ie.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: "+e.getMessage());
        }
    }
}


//ForkJoinPool pool = new ForkJoinPool(
//        parallelism,
//        ForkJoinPool.defaultForkJoinWorkerThreadFactory,
//        null,
//        false ??
//);