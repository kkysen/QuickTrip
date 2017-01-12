package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.app.javafx.Screen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
public class SearchingScreen implements Screen {
    
    private final VBox vBox;
    
    private final Text searchingText;
    
    public SearchingScreen() {
        
        searchingText = new Text("Searching...");
        searchingText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 100));
        
        vBox = new VBox(searchingText);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(25, 25, 25, 25));
        
    }
    
    @Override
    public Pane getPane() {
        return vBox;
    }
    
}
