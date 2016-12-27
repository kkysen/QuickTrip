package io.github.kkysen.quicktrip.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class WelcomeScreen implements Screen {
    
    private final VBox vBox;
    
    private void setupVBox() {
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(25, 25, 25, 25));
    }
    
    public WelcomeScreen() {
        final Text welcomeMsg = new Text("Welcome to " + QuickTrip.TITLE);
        welcomeMsg.setFont(Font.font("Tahoma", FontWeight.NORMAL, 72));
        
        final Button startBtn = new Button("Start Planning Your Trip");
        startBtn.setAlignment(Pos.CENTER);
        startBtn.setFont(Font.font(48));
        
        startBtn.setOnAction(event -> QuickTrip.SCREENS.load(SearchScreen.class));
        
        vBox = new VBox(300, welcomeMsg, startBtn);
        setupVBox();
    }
    
    @Override
    public Pane getPane() {
        return vBox;
    }
    
}
