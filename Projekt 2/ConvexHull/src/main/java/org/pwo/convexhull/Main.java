package org.pwo.convexhull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class Main {


    public static void main(String[] args) {

        Path rootDir = Paths.get("C:\\Users\\MS\\Desktop\\PWO\\Projekt\\Projekt 2\\ConvexHull\\src\\main\\resources\\problems\\medium");

        try (ForkJoinPool pool = new ForkJoinPool()) {
            try (Stream<Path> paths = Files.list(rootDir)) {
                paths.filter(path -> path.toString().endsWith(".txt"))
                        .forEach(path -> {
                            System.out.println("ConvexHull task for: " + path);





                        });
            } catch (IOException e) {
                throw new RuntimeException("IO error: "+e.getMessage());
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