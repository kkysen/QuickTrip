package io.github.kkysen.quicktrip.app;

import javafx.scene.layout.Pane;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ItineraryScreenController implements Screen {
    
    private ItineraryScreenModel model;
    private ItineraryScreenView view;
    
    public ItineraryScreenController() {}
    
    public void load() {
        model = new ItineraryScreenModel();
        view = new ItineraryScreenView();
        view.addDestinations(model.getDestinations());
    }
    
    @Override
    public Pane getPane() {
        return view.getGrid();
    }
    
}
