package org.pwo;

import org.pwo.parallel.GrahamScanTask;
import org.pwo.utils.PointRenderer;
import org.pwo.utils.PointUtils;

import javax.swing.*;
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

        int version = 1;
        List<Point2D> points = PointUtils.readList("src/main/resources/problem"+version+".point2d");


        for (Point2D p : points) {
            System.out.println(p.getX()+", "+p.getY());
        }
        System.out.println("---------------------------------------------------");

        GrahamScanTask grahamScanTask = new GrahamScanTask(points, 5);

        try (ForkJoinPool pool = new ForkJoinPool()) {
            List<Point2D> result = pool.invoke(grahamScanTask);
            System.out.println("----- R E S U L T --------------------------------------");
            for (Point2D p : result) {
                System.out.println(p.getX()+", "+p.getY());
            }
//            try {
//                PointRenderer renderer = new PointRenderer(
//                        "Convex Hull: problem " + version,
//                        points, Color.magenta, 20,
//                        result, Color.lightGray, 8);
//                renderer.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}