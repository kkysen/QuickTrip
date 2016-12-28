package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.google.maps.directions.order.WaypointOrderRequest;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    
    private LocalDate startDate;
    private List<Destination> destinations;
    
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
    
    private class Destination {
        
        private final String address;
        private final int numDays;
        private final LocalDate startDate;
        private final LocalDate endDate;
        
        public Destination(final io.github.kkysen.quicktrip.app.Destination noDateDest,
                final LocalDate startDate, final LocalDate endDate) {
            address = noDateDest.getAddress();
            numDays = noDateDest.getNumDays();
            this.startDate = startDate;
            this.endDate = endDate;
        }
        
        public List<Hotel> possibleHotels() {
            
        }
        
    }
    
    private List<Destination> orderDestinations() {
        List<io.github.kkysen.quicktrip.app.Destination> orderedDestinations;
        try {
            orderedDestinations = WaypointOrderRequest.orderedDestinations(
                    searchArgs.getOrigin(),
                    searchArgs.getDestinations(),
                    io.github.kkysen.quicktrip.app.Destination::getAddress);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        final List<Destination> dests = new ArrayList<>(orderedDestinations.size());
        int numDaysSinceStart = 0;
        for (final io.github.kkysen.quicktrip.app.Destination noDateDest : orderedDestinations) {
            final LocalDate startDate = this.startDate.plusDays(numDaysSinceStart);
            numDaysSinceStart += noDateDest.getNumDays();
            final LocalDate endDate = this.startDate.plusDays(numDaysSinceStart);
            dests.add(new Destination(noDateDest, startDate, endDate));
        }
        return dests;
    }
    
    public void load() {
        searchArgs = deserializeSearchArgs();
        
        startDate = searchArgs.getDate();
        
        destinations = orderDestinations();
    }
    
    @Override
    public Pane getPane() {
        return grid;
    }
    
}
