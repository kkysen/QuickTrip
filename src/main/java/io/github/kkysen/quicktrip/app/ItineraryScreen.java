package io.github.kkysen.quicktrip.app;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ItineraryScreen implements Screen {
    
    private final GridPane grid = new GridPane();
    
    private void deserializeSearchArgs() {
        
    }
    
    public ItineraryScreen() {
        
    }
    
    @Override
    public Pane getPane() {
        return grid;
    }
    
}