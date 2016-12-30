package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.google.maps.directions.order.WaypointOrderRequest;
import io.github.kkysen.quicktrip.io.MyFiles;
import io.github.kkysen.quicktrip.optimization.simulatedAnnealing.SimulatedAnnealer;

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
    
    private int numPeople;
    private int budget;
    private LocalDate startDate;
    private List<Destination> destinations;
    private List<Hotel> hotels;
    
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
    
    private List<Destination> orderDestinations() {
        List<NoDateDestination> orderedDestinations;
        try {
            orderedDestinations = WaypointOrderRequest.orderedDestinations(
                    searchArgs.getOrigin(),
                    searchArgs.getDestinations(),
                    NoDateDestination::getAddress);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        final List<Destination> dests = new ArrayList<>(orderedDestinations.size());
        int numDaysSinceStart = 0;
        for (final NoDateDestination noDateDest : orderedDestinations) {
            final LocalDate startDate = this.startDate.plusDays(numDaysSinceStart);
            numDaysSinceStart += noDateDest.getNumDays();
            final LocalDate endDate = this.startDate.plusDays(numDaysSinceStart);
            dests.add(new Destination(noDateDest, startDate, endDate, numPeople));
        }
        return dests;
    }
    
    private List<Hotel> getOptimalHotels() {
        System.out.println("scraping hotels");
        final Hotels hotels = new Hotels(destinations, budget);
        System.out.println("annealing");
        final SimulatedAnnealer<Hotels> annealer = new SimulatedAnnealer<>(hotels); // FIXME add tuning args
        annealer.search(); // FIXME add numIters
        System.out.println(annealer.getMinState().totalPrice());
        return annealer.getMinState().getHotels();
    }
    
    public void load() {
        searchArgs = deserializeSearchArgs();
        numPeople = searchArgs.getNumPeople();
        budget = searchArgs.getBudget();
        startDate = searchArgs.getDate();
        destinations = orderDestinations();
        hotels = getOptimalHotels();
        hotels.forEach(System.out::println);
    }
    
    @Override
    public Pane getPane() {
        return grid;
    }
    
}
