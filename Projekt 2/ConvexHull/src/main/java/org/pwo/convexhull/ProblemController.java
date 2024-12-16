package org.pwo.convexhull;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.pwo.convexhull.Geometry.PointOperations;
import org.pwo.convexhull.Geometry.PointScaler;

import java.awt.geom.Point2D;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

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

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vBox;

    private List<Point2D> _points;
    private List<Point2D> _lines;

    @FXML
    void onOkClicked(ActionEvent event) {
        String filename = problemFileName.getText();
        System.out.println("Text entered (Ok button): " + filename);
        if (!filename.isEmpty())
            setUpProblem(filename);
    }

    @FXML
    void onEnterTyped(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String filename = problemFileName.getText();
            System.out.println("Text entered (Enter): " + filename);
            if (!filename.isEmpty())
                setUpProblem(filename);
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

        assert okButton != null : "fx:id=\"okButton\" was not injected: check your FXML file 'problem-view.fxml'.";
        assert problemCanvas != null : "fx:id=\"problemCanvas\" was not injected: check your FXML file 'problem-view.fxml'.";
        assert problemFileName != null : "fx:id=\"problemFileName\" was not injected: check your FXML file 'problem-view.fxml'.";
        assert scrollPane != null : "fx:id=\"scrollPane\" was not injected: check your FXML file 'problem-view.fxml'.";

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

    private void drawLine(Point2D start, Point2D end, Color color) {
        GraphicsContext gc = problemCanvas.getGraphicsContext2D();
        gc.setStroke(color);
        gc.setLineWidth(3);
        gc.strokeLine(
                start.getX(), start.getY(),
                end.getX(), end.getY()
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

}
