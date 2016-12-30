package io.github.kkysen.quicktrip.app;

import lombok.Getter;

import javafx.scene.layout.GridPane;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Getter
public class ItineraryScreenView {
    
    private final GridPane grid = new GridPane();
    private final GridRows rows = new GridRows(grid);
    
}
