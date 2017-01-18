package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.app.input.EmptyInputError;
import io.github.kkysen.quicktrip.app.input.InputError;
import io.github.kkysen.quicktrip.app.input.WholeNumberInputError;
import io.github.kkysen.quicktrip.app.javafx.Screen;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class SearchController implements Screen {
    
    private static final Type MODEL_LIST_TYPE = new TypeToken<List<SearchModel>>() {}.getType();
    
    private final Thread SAVE_MODELS_ON_EXIT = new Thread(this::serializeModels);
    {
        //Runtime.getRuntime().addShutdownHook(SAVE_MODELS_ON_EXIT);
    }
    
    private final List<SearchModel> models;
    
    private final SearchView view;
    private SearchModel model;
    
    private final Button moreDestinationsBtn;
    private final Button searchBtn;
    private final Button backBtn;
    private final Button resetBtn;
    private final Button lastSearchBtn;
    
    public SearchController() {
        view = new SearchView();
        
        moreDestinationsBtn = view.getMoreDestsBtn();
        searchBtn = view.getSearchBtn();
        backBtn = view.getBackBtn();
        resetBtn = view.getResetBtn();
        lastSearchBtn = view.getLastSearchBtn();
        addButtonActions();
        
        models = deserializeModels();
        
        model = new SearchModel();
        models.add(0, model);
    }
    
    private void addButtonActions() {
        moreDestinationsBtn.setOnAction(event -> addMoreDestinations());
        
        searchBtn.setOnAction(event -> search());
        
        backBtn.setOnAction(event -> QuickTrip.SCREENS.load(WelcomeScreen.class));
        
        resetBtn.setOnAction(event -> view.reset());
        
        lastSearchBtn.setOnAction(event -> oldSearch());
    }
    
    private List<SearchModel> deserializeModels() {
        Reader reader;
        try {
            reader = Files.newBufferedReader(QuickTripConstants.SEARCH_MODEL_PATH,
                    Constants.CHARSET);
        } catch (final IOException e) {
            throw new RuntimeException(e); // shouldn't happen
        }
        final List<SearchModel> models = QuickTripConstants.GSON.fromJson(reader, MODEL_LIST_TYPE);
        models.forEach(SearchModel::setValidated);
        return models;
    }
    
    private void removeModelsIfInvalid() {
        final Iterator<SearchModel> iter = models.iterator();
        while (iter.hasNext()) {
            final SearchModel model = iter.next();
            if (model.isValidated()) {
                continue;
            }
            boolean isValid = false;
            try {
                isValid = model.validate();
                System.out.println(isValid);
            } catch (final InputError e) {}
            if (!isValid) {
                iter.remove();
            }
        }
    }
    
    void serializeModels() {
        removeModelsIfInvalid();
        final String json = QuickTripConstants.GSON.toJson(models, MODEL_LIST_TYPE);
        try {
            MyFiles.write(QuickTripConstants.SEARCH_MODEL_PATH, json);
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
        try {
            Thread.sleep(100); // make sure SearchingScreen finishes loading
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        QuickTrip.SCREENS.get(ItineraryController.class).load(model);
        // when ItineraryScreen is finished loading, switch to it
        QuickTrip.SCREENS.load(ItineraryController.class);
    }
    
    private void loadedSearch() {
        // switch to SearchingScreen while ItineraryScreen loads
        QuickTrip.SCREENS.load(SearchingScreen.class);
        try {
            System.out.println("debug");
            Thread.sleep(1000); // make sure SearchingScreen finishes loading
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(this::loadItineraryScreen).run();
    }
    
    private void oldSearch(final int searchNum) {
        models.add(0, models.remove(searchNum));
        System.out.println(models);
        model = models.get(0);
        loadedSearch();
    }
    
    @Override
    public Pane getPane() {
        return view.getGrid();
    }
    
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
