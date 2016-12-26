package io.github.kkysen.quicktrip.core;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class QuickTripOld extends Application {
    
    static final String TITLE = "Quick Trip";
        
    private TextField createDestination(final int destNum, final GridPane grid, final int rowIndex) {
        final Label destLabel = new Label("Destination " + destNum);
        grid.add(destLabel, 0, rowIndex);
        
        final TextField dest = new TextField();
        grid.add(dest, 1, rowIndex);
        return dest;
    }
    
    private List<TextField> createDestinations(int numDests, final GridPane grid, int startRowIndex) {
        numDests++;
        startRowIndex--;
        final List<TextField> destinations = new ArrayList<>(numDests);
        for (int destNum = 1; destNum < numDests; destNum++) {
            destinations.add(createDestination(destNum, grid, startRowIndex + destNum));
        }
        return destinations;
    }
    
    @Override
    public void start(final Stage primaryStage) {
        
//        final GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(25, 25, 25, 25));
//        
//        final Scene scene = new Scene(grid, 300, 250);
//        
//        
//        final Text welcomeMsg = new Text("Welcome to " + TITLE);
//        welcomeMsg.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//        grid.add(welcomeMsg, 0, 0, 2, 1);
//        
//        final Label originLabel = new Label("Origin");
//        grid.add(originLabel, 0, 1);
//        
//        final TextField origin = new TextField();
//        grid.add(origin, 1, 1);
//        
////        final Label destinationLabel = new Label("Destination");
////        grid.add(destinationLabel, 0, 2);
////        
////        final TextField destination = new TextField();
////        grid.add(destination, 1, 2);
//        
//        final List<TextField> destinations = createDestinations(5, grid, 2);
        
        final Scene scene = new WelcomePage().toScene();
        
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(final String[] args) {
        launch(args);
    }
}
