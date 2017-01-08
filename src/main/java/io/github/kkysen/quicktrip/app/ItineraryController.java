package io.github.kkysen.quicktrip.app;

import javafx.scene.layout.Pane;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ItineraryController implements Screen {
    
    private ItineraryModel model;
    private ItineraryView view;
    
    public ItineraryController() {}
    
    public void load(final SearchModel searchArgs) {
        model = new ItineraryModel(searchArgs);
        view = new ItineraryView();
        view.addDestinations(model.getDestinations());
    }
    
    @Override
    public Pane getPane() {
        return view.getGrid();
    }
    
}
