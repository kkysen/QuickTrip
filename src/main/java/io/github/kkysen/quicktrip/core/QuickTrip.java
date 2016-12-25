package io.github.kkysen.quicktrip.core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class QuickTrip extends Application {
    
    @Override
    public void start(final Stage primaryStage) {
        final Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(event -> System.out.println("Hello World!"));
        
        final StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        final Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Quick Trip");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(final String[] args) {
        launch(args);
    }
}
