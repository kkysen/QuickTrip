package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.google.maps.directions.order.WaypointOrderRequest;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

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
        return QuickTrip.GSON.fromJson(json, SearchArgs.class);
    }
    
    public ItineraryScreen() {
        
    }
    
    @RequiredArgsConstructor
    private static class Destination {
        
        private final String address;
        private final String numDays;
        private final LocalDate startDate;
        private final LocalDate endDate;
        
    }
    
    private static List<Destination> orderDestinations(final SearchArgs searchArgs) throws IOException {
        final List<io.github.kkysen.quicktrip.app.Destination> orderedDestinations = //
                WaypointOrderRequest.orderedDestinations(
                        searchArgs.getOrigin(),
                        searchArgs.getDestinations(),
                        io.github.kkysen.quicktrip.app.Destination::getAddress);
        final List<Destination> dests = new ArrayList<>(orderedDestinations.size());
        final LocalDate startDate = searchArgs.getDate();
        //for ()
        return null;
    }
    
    public void load() {
        searchArgs = deserializeSearchArgs();
        
        final LocalDate startDate = searchArgs.getDate();
        
        final List<String> destinations = new ArrayList<>();
    }
    
    @Override
    public Pane getPane() {
        return grid;
    }
    
}
