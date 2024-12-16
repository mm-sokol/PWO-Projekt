package org.pwo.convexhull;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.pwo.convexhull.Algorithm.GrahamScan;
import org.pwo.convexhull.Algorithm.GrahamScanTask;
import org.pwo.convexhull.Geometry.PointOperations;
import org.pwo.convexhull.Geometry.PointScaler;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

public class ProblemController {

    @FXML
    private ToggleGroup algorithmModeChoice;

    @FXML
    private HBox hBox;

    @FXML
    private Button okButton;

    @FXML
    private Canvas problemCanvas;

    @FXML
    private TextField problemFileName;

    @FXML
    private RadioButton radioBtnParallel;

    @FXML
    private RadioButton radioBtnSequential;

//    @FXML
//    private ScrollPane scrollPane;

    @FXML
    private VBox vBox;

    private List<Point2D> _points;
    private List<Point2D> _lines;
    PointScaler _pointScaler;

    private Stage _stage;
    private Scene _scene;
    private Parent _root;

    @FXML
    void onOkClicked(ActionEvent event) {
        String filename = problemFileName.getText();
        System.out.println("Text entered (Ok button): " + filename);
        if (!filename.isEmpty())
            setUpProblem(filename);

        Toggle selectedOption = algorithmModeChoice.getSelectedToggle();
        if (selectedOption == null) {
            return;
        }

        String algorithmChoice = ((RadioButton) selectedOption).getText();
        if (_points.isEmpty()) {
            return;
        }

        solveProblem(algorithmChoice);
    }

    @FXML
    void onEnterTyped(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String filename = problemFileName.getText();
            System.out.println("Text entered (Enter): " + filename);
            if (!filename.isEmpty())
                setUpProblem(filename);
        } else if (event.getCode() == KeyCode.T && event.isAltDown()) {
            debugTangentFindingAlgorithm();
        } else if (event.getCode() == KeyCode.M && event.isAltDown()) {
            debugMergeAlgorithm();
        }
    }

    @FXML
    private void onMouseClick(MouseEvent event) {
        // Get mouse click coordinates on the canvas
        double x = event.getX();
        double y = event.getY();

        // Print the coordinates to the console
        System.out.println("Mouse clicked at: X = " + x + ", Y = " + y);

        // Optional: Draw a point on the canvas at the clicked position (you can add more logic here)
        problemCanvas.getGraphicsContext2D().fillOval(x - 2, y - 2, 4, 4);
    }

    @FXML
    void initialize() {
        _points = new ArrayList<>();
        _lines = new ArrayList<>();
        _pointScaler = new PointScaler(problemCanvas.getWidth(), problemCanvas.getHeight());

        assert okButton != null : "fx:id=\"okButton\" was not injected: check your FXML file 'problem-view.fxml'.";
        assert problemCanvas != null : "fx:id=\"problemCanvas\" was not injected: check your FXML file 'problem-view.fxml'.";
        assert problemFileName != null : "fx:id=\"problemFileName\" was not injected: check your FXML file 'problem-view.fxml'.";
//        assert scrollPane != null : "fx:id=\"scrollPane\" was not injected: check your FXML file 'problem-view.fxml'.";

        algorithmModeChoice.selectedToggleProperty().addListener((
                (observable, oldChoice, newChoice) -> {
                    if (newChoice != null) {
                        RadioButton selected = (RadioButton) algorithmModeChoice.getSelectedToggle();
                        String label = selected.getText();
                        System.out.println("Selected: "+label);
                    }
                }));
    }

    private void drawPoint(Point2D point, Color color) {
        GraphicsContext gc = problemCanvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillOval(point.getX(), point.getY(), 5, 5);
    }

    private void drawLine(Point2D start, Point2D end) {
        GraphicsContext gc = problemCanvas.getGraphicsContext2D();

        gc.strokeLine(
                start.getX(), start.getY(),
                end.getX(), end.getY()
        );
    }

    private void drawLines(List<Point2D> lines, Color color, int width) {
        if (lines.size() < 2) {
            return;
        }
        GraphicsContext gc = problemCanvas.getGraphicsContext2D();
        gc.setStroke(color);
        gc.setLineWidth(width);
        _pointScaler.calculateOffsetsAndScale(_points);

        Point2D firstTransformed = _pointScaler.transformToScale(lines.getFirst());
        Point2D lastTransformed = firstTransformed;
        for (int i = 1; i < lines.size(); i++) {
            Point2D currentTransformed = _pointScaler.transformToScale(lines.get(i));
            drawLine(
                    lastTransformed,
                    currentTransformed
            );
            lastTransformed = currentTransformed;
        }
        drawLine(
                lastTransformed,
                firstTransformed
        );
    }

    private void setUpProblem(String filename) {
        GraphicsContext gc = problemCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, problemCanvas.getWidth(), problemCanvas.getHeight());
        try {
            List<Point2D> points = PointOperations.readFromFile(filename);
            if (points.isEmpty())
                return;

            PointScaler scaler = new PointScaler(problemCanvas.getWidth(), problemCanvas.getHeight());
            scaler.calculateOffsetsAndScale(points);

            for (Point2D p : points) {
                drawPoint(scaler.transformToScale(p), Color.MEDIUMVIOLETRED);
            }

            _points = points;

        } catch (Exception e) {
            System.out.println("Error occurred: "+e.getMessage());
        }
    }

    private void solveProblem(String choice) {
        if (choice.equals("Sequential")) {
            System.out.println("Solving ConvexHull sequentially");
            _lines = GrahamScan.scan(_points);
            drawLines(_lines, Color.BLUE, 3);
            System.out.println("Resulting set of points has "+_lines.size()+" elements");

        } else if (choice.equals("Parallel")) {

            System.out.println("Solving ConvexHull using fork-join");
            GrahamScanTask task = new GrahamScanTask(12, _points);
            try (ForkJoinPool pool = new ForkJoinPool()) {
                _lines = pool.invoke(task);
                drawLines(_lines, Color.MEDIUMVIOLETRED, 3);
                System.out.println("Resulting set of points has "+_lines.size()+" elements");


            } catch (RejectedExecutionException e) {
                throw new RuntimeException(e);
            }catch (CancellationException ex) {
                System.out.println("Task has been canceled: "+ex.getMessage());
            } catch (IllegalStateException  ex) {
                System.out.println("Illegal state exception: "+ex.getMessage());
            } catch (RuntimeException ex) {
                System.out.println("Runtime exception: "+ex.getMessage());
            } catch (Exception ex) {
                System.out.println("General purpose exception caught: "+ex.getMessage());
            }
        }
    }

    private void debugTangentFindingAlgorithm() {
        if (_points.size() < 2) {
            return;
        }
        // make split
        int middle = _points.size() / 2;
        _points.sort(Comparator.comparingDouble(Point2D::getX));
        List<Point2D> leftHalf = _points.subList(0, middle);
        List<Point2D> rightHalf = _points.subList(middle, _points.size());

        // compute hulls sequentially
        List<Point2D> leftHull = GrahamScan.scan(leftHalf);
        List<Point2D> rigthHull = GrahamScan.scan(rightHalf);
        drawLines(leftHull, Color.BLUE, 3);
        drawLines(rigthHull, Color.VIOLET, 3);

        GraphicsContext gc = problemCanvas.getGraphicsContext2D();
        gc.setStroke(Color.GREEN);
        // draw upper tangent (might be buggy)
        GrahamScan.Tuple<Integer, Integer> indexes = GrahamScan.findUpperTangent(leftHull, rigthHull);
        Point2D start = leftHull.get(indexes.first());
        Point2D end = rigthHull.get(indexes.second());
        drawLine(
                _pointScaler.transformToScale(start),
                _pointScaler.transformToScale(end)
        );

        gc.setStroke(Color.MEDIUMAQUAMARINE);
        // draw lower tangent
        GrahamScan.Tuple<Integer, Integer> lowerIndexes = GrahamScan.findLowerTangent(leftHull, rigthHull);
        Point2D lowerStart = leftHull.get(lowerIndexes.first());
        Point2D lowerEnd = rigthHull.get(lowerIndexes.second());
        drawLine(
                _pointScaler.transformToScale(lowerStart),
                _pointScaler.transformToScale(lowerEnd)
        );
    }


    private void debugMergeAlgorithm() {
        if (_points.size() < 2) {
            return;
        }
        // make split
        int middle = _points.size() / 2;
        _points.sort(Comparator.comparingDouble(Point2D::getX));
        List<Point2D> leftHalf = _points.subList(0, middle);
        List<Point2D> rightHalf = _points.subList(middle, _points.size());

        // compute hulls sequentially
        List<Point2D> leftHull = GrahamScan.scan(leftHalf);
        List<Point2D> rigthHull = GrahamScan.scan(rightHalf);
        drawLines(leftHull, Color.BLUE, 3);
        drawLines(rigthHull, Color.VIOLET, 3);

        GraphicsContext gc = problemCanvas.getGraphicsContext2D();
        gc.setStroke(Color.GREEN);
        // draw upper tangent (might be buggy)
        GrahamScan.Tuple<Integer, Integer> indexes = GrahamScan.findUpperTangent(leftHull, rigthHull);
        Point2D start = leftHull.get(indexes.first());
        Point2D end = rigthHull.get(indexes.second());
        drawLine(
                _pointScaler.transformToScale(start),
                _pointScaler.transformToScale(end)
        );

        gc.setStroke(Color.MEDIUMAQUAMARINE);
        // draw lower tangent
        GrahamScan.Tuple<Integer, Integer> lowerIndexes = GrahamScan.findLowerTangent(leftHull, rigthHull);
        Point2D lowerStart = leftHull.get(lowerIndexes.first());
        Point2D lowerEnd = rigthHull.get(lowerIndexes.second());
        drawLine(
                _pointScaler.transformToScale(lowerStart),
                _pointScaler.transformToScale(lowerEnd)
        );

        List<Point2D> mergedHull = GrahamScan.merge(leftHull, rigthHull);
        drawLines(mergedHull, Color.SALMON, 3);
    }

}
