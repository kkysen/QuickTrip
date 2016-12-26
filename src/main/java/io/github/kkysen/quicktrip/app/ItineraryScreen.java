package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Paths;

import com.google.gson.Gson;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ItineraryScreen implements Screen {
    
    private final GridPane grid = new GridPane();
    
    private SearchArgs searchArgs;
    
    private SearchArgs deserializeSearchArgs() {
        String json;
        try {
            json = MyFiles.read(Paths.get(QuickTrip.SEARCH_ARGS_PATH));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return new Gson().fromJson(json, SearchArgs.class);
    }
    
    public ItineraryScreen() {
        
    }
    
    public void load() {
        searchArgs = deserializeSearchArgs();
    }
    
    @Override
    public Pane getPane() {
        return grid;
    }
    
}
