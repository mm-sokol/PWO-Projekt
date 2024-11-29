package org.pwo;

import org.pwo.parallel.GrahamScanTask;
import org.pwo.utils.PointUtils;

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

        GrahamScanTask grahamScanTask = new GrahamScanTask(points, 10);

        try (ForkJoinPool pool = new ForkJoinPool()) {
            List<Point2D> result = pool.invoke(grahamScanTask);
            for (Point2D p : result) {
                System.out.println(p);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}