package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class SearchController implements Screen {
    
    private static final Path MODEL_PATH = Paths.get("src/main/resources/searchModels.json");
    
    private static final Type MODEL_LIST_TYPE = new TypeToken<List<SearchModel>>() {}.getType();
    
    private final Thread SAVE_MODELS_ON_EXIT = new Thread(this::serializeModels);
    {
        Runtime.getRuntime().addShutdownHook(SAVE_MODELS_ON_EXIT);
    }
    
    private final List<SearchModel> models;
    
    private final SearchView view;
    private SearchModel model;
    
    private FXMLLoader loader;
    
    /*private final Button moreDestinationsBtn;
    private final Button searchBtn;
    private final Button backBtn;
    private final Button resetBtn;
    private final Button lastSearchBtn;*/
    
    public SearchController() {
        view = new SearchView();
        loader = view.getLoader();
        loader.setController(this);
        view.reset();
        
        /*moreDestinationsBtn = view.getMoreDestsBtn();
        searchBtn = view.getSearchBtn();
        backBtn = view.getBackBtn();
        resetBtn = view.getResetBtn();
        lastSearchBtn = view.getLastSearchBtn();
        addButtonActions();*/
        
        models = deserializeModels();
        
        model = new SearchModel();
        models.add(0, model);
    }
    
    /*private void addButtonActions() {
        moreDestinationsBtn.setOnAction(event -> addMoreDestinations());
        
        searchBtn.setOnAction(event -> search());
        
        backBtn.setOnAction(event -> QuickTrip.SCREENS.load(WelcomeScreen.class));
        
        resetBtn.setOnAction(event -> view.reset());
        
        lastSearchBtn.setOnAction(event -> oldSearch());
    }*/
    
    private List<SearchModel> deserializeModels() {
        Reader reader;
        try {
            reader = Files.newBufferedReader(MODEL_PATH, Constants.CHARSET);
        } catch (final IOException e) {
            throw new RuntimeException(e); // shouldn't happen
        }
        return QuickTripConstants.GSON.fromJson(reader, MODEL_LIST_TYPE);
    }
    
    private void removeModelIfInvalid() {
        boolean isValid = false;
        try {
            isValid = model.validate();
        } catch (final InputError e) {}
        if (!isValid) {
            models.remove(0); // don't serialize invalid model
        }
    }
    
    private void serializeModels() {
        System.out.println("serializingModels");
        if (!QuickTrip.SCREENS.getCurrentScreen().equals(ItineraryController.class)) {
            removeModelIfInvalid();
        }
        final String json = QuickTripConstants.GSON.toJson(models, MODEL_LIST_TYPE);
        try {
            MyFiles.write(MODEL_PATH, json);
        } catch (final IOException e) {
            throw new RuntimeException(e); // shouldn't happen
        }
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
    
    private void loadedSearch() {
        // switch to SearchingScreen while ItineraryScreen loads
        QuickTrip.SCREENS.load(SearchingScreen.class);
        try {
            Thread.sleep(10); // make sure SearchingScreen finishes loading
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(this::loadItineraryScreen).run();
    }
    
    
    
    private void oldSearch(final int searchNum) {
        models.add(0, models.remove(searchNum));
        model = models.get(0);
        loadedSearch();
    }
    
    @Override
    public Pane getPane() {
        return view.getGrid();
    }
    
    
    /*
     * Button Callbacks
     * 
     * */
    public void addMoreDestinations() {
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
    public void search() {
        if (validateNewSearch()) {
            loadedSearch();
        }
    }
    public void oldSearch() {
        oldSearch(1);
    }
    public void onBackPressed() {
    	QuickTrip.SCREENS.load(WelcomeScreen.class);
    }
    public void onReset() {
    	view.reset();
    }
    
}
