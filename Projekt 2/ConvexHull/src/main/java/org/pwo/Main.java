package org.pwo;

import org.pwo.parallel.GrahamScanTask;
import org.pwo.utils.PointRenderer;
import org.pwo.utils.PointUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");

        List<Point2D> points = PointUtils.readList("src/main/resources/problem1.point2d");

        PointRenderer renderer = new PointRenderer("Convex Hull: problem 1", points, Color.red, 20);

        GrahamScanTask grahamScanTask = new GrahamScanTask(points, 10);

        try (ForkJoinPool pool = new ForkJoinPool()) {
            List<Point2D> result = pool.invoke(grahamScanTask);
            for (Point2D p : result) {
                System.out.println(p);
            }
            renderer.addLines(result, Color.blue, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}