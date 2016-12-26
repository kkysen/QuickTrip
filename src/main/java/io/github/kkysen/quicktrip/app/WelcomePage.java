package io.github.kkysen.quicktrip.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class WelcomePage {
    
    private final GridPane grid = new GridPane();
    
    private void setupGrid() {
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
    }
    
    public WelcomePage() {
        setupGrid();
        
        final Text welcomeMsg = new Text("Welcome to " + QuickTrip.TITLE);
        welcomeMsg.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(welcomeMsg, 0, 0, 2, 1);
        
        final Button startBtn = new Button("Start Planning Your Trip");
        startBtn.setAlignment(Pos.CENTER);
        grid.add(startBtn, 0, 2);
        
        startBtn.setOnAction(event -> {
            
        });;
    }
    
    public Scene toScene() {
        return new Scene(grid, 500, 500);
    }
    
}
