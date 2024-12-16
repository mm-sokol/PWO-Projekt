package org.pwo.convexhull;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ProblemApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("problem-view.fxml"));

        // Load the AnchorPane from the FXML file
        Parent root = loader.load();



        // Set up the Scene
        Scene scene = new Scene(root, 800, 800);
        Image icon = new Image("coding-brackets.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(true);
        // Set the title of the stage (window)
        primaryStage.setTitle("Convex Hull Problem");

        // Set the scene for the stage
        primaryStage.setScene(scene);

        // Show the stage (open the window)
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}