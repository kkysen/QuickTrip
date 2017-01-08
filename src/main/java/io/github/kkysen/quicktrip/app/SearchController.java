package io.github.kkysen.quicktrip.app;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class SearchController implements Screen {
    
    private final SearchView view;
    private final SearchModel model;
    
    private final Button moreDestinationsBtn;
    private final Button searchBtn;
    private final Button backBtn;
    private final Button resetBtn;
    private final Button lastSearchBtn;
    
    private void addButtonActions() {
        moreDestinationsBtn.setOnAction(event -> addMoreDestinations());
        
        searchBtn.setOnAction(event -> search());
        
        backBtn.setOnAction(event -> QuickTrip.SCREENS.load(WelcomeScreen.class));
        
        resetBtn.setOnAction(event -> view.reset());
        
        lastSearchBtn.setOnAction(event -> oldSearch());
    }
    
    public SearchController() {
        view = new SearchView();
        
        moreDestinationsBtn = view.getMoreDestsBtn();
        searchBtn = view.getSearchBtn();
        backBtn = view.getBackBtn();
        resetBtn = view.getResetBtn();
        lastSearchBtn = view.getLastSearchBtn();
        addButtonActions();
        
        model = new SearchModel();
    }
    
    private void addMoreDestinations() {
        final String numDestinationsInput = view.getNumDests().getText();
        model.setNumDestinationsInput(numDestinationsInput);
        try {
            model.validateNumDestinations();
        } catch (final EmptyInputError e) {
            return;
        } catch (final WholeNumberInputError e) {
            e.getErrorDialog().showAndWait();
            return;
        }
        view.setNumDestinations(model.getNumDestinations());
        //view.makeMoreDests();
    }
    
    private boolean validateNewSearch() {
        model.setOriginInput(view.getOrigin().getText());
        model.setStartDateInput(view.getStartDate().getValue());
        model.setNumDestinationsInput(view.getNumDests().getText());
        model.setDestinationInputs(view.getDestFields());
        model.setNumPeopleInput(view.getNumPeople().getText());
        model.setBudgetInput(view.getBudget().getText());
        
        try {
            model.validate();
            return true;
        } catch (final InputError e) {
            e.getErrorDialog().showAndWait();
            return false;
        }
    }
    
    private void loadItineraryScreen() {
        final ItineraryController itineraryScreen = //
                (ItineraryController) QuickTrip.SCREENS.get(ItineraryController.class);
        itineraryScreen.load(model);
        // when ItineraryScreen is finished loading, switch to it
        QuickTrip.SCREENS.load(ItineraryController.class);
    }
    
    private void oldSearch() {
        // switch to SearchingScreen while ItineraryScreen loads
        QuickTrip.SCREENS.load(SearchingScreen.class);
        try {
            Thread.sleep(10); // make sure SearchingScreen finishes loading
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(this::loadItineraryScreen).run();
    }
    
    public void search() {
        if (validateNewSearch()) {
            oldSearch();
        }
    }
    
    @Override
    public Pane getPane() {
        return view.getGrid();
    }
    
}
