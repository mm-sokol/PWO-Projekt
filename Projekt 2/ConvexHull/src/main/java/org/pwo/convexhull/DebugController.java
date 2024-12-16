package org.pwo.convexhull;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DebugController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox hBox;

    @FXML
    private Button leftButton;

    @FXML
    private Label mainLabel;

    @FXML
    private Button okButton;

    @FXML
    private Canvas problemCanvas;

    @FXML
    private TextField problemFileName;

    @FXML
    private Button rightButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vBox;

    @FXML
    void onEnterTyped(KeyEvent event) {

    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.Q && event.isAltDown()) {
            //
            try {
                switchToProblemScene(event);
            } catch(RuntimeException e) {
                System.out.println("Error in handler onKeyPressed: "+e.getMessage());
            }
        }
    }

    @FXML
    void onLeftBtnClicked(MouseEvent event) {

    }

    @FXML
    void onMouseClick(MouseEvent event) {

    }

    @FXML
    void onOkClicked(ActionEvent event) {

    }

    @FXML
    void onRightButtonClicked(MouseEvent event) {

    }

    private List<Point2D> _points;
    private List<Point2D> _lines;

    private Stage _stage;
    private Scene _scene;
    private Parent _root;

    @FXML
    void initialize() {
        assert hBox != null : "fx:id=\"hBox\" was not injected: check your FXML file 'debug-view.fxml'.";
        assert leftButton != null : "fx:id=\"leftButton\" was not injected: check your FXML file 'debug-view.fxml'.";
        assert mainLabel != null : "fx:id=\"mainLabel\" was not injected: check your FXML file 'debug-view.fxml'.";
        assert okButton != null : "fx:id=\"okButton\" was not injected: check your FXML file 'debug-view.fxml'.";
        assert problemCanvas != null : "fx:id=\"problemCanvas\" was not injected: check your FXML file 'debug-view.fxml'.";
        assert problemFileName != null : "fx:id=\"problemFileName\" was not injected: check your FXML file 'debug-view.fxml'.";
        assert rightButton != null : "fx:id=\"rightButton\" was not injected: check your FXML file 'debug-view.fxml'.";
        assert scrollPane != null : "fx:id=\"scrollPane\" was not injected: check your FXML file 'debug-view.fxml'.";
        assert vBox != null : "fx:id=\"vBox\" was not injected: check your FXML file 'debug-view.fxml'.";

    }

    public void setPoints(List<Point2D> points) {
        _points = points;
    }

    public void switchToProblemScene(Event event) throws RuntimeException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("problem-view.fxml"));
            _root = loader.load();



            _stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            _scene = new Scene(_root, 800, 800);
            _stage.setScene(_scene);
            _stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

