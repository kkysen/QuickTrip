package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.google.maps.directions.GoogleDrivingDirectionsRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.order.DestinationOrderRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.DrivingDirections;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.Leg;
import io.github.kkysen.quicktrip.app.data.Destination;
import io.github.kkysen.quicktrip.app.data.Flight;
import io.github.kkysen.quicktrip.app.data.Hotel;
import io.github.kkysen.quicktrip.app.data.Hotels;
import io.github.kkysen.quicktrip.app.data.NoDateDestination;
import io.github.kkysen.quicktrip.optimization.simulatedAnnealing.SimulatedAnnealer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ItineraryModel {
    
    private static final Duration FLYING_THRESHOLD = Duration.ofHours(6);
    
    private final List<NoDateDestination> noDateDests;
    
    private final @Getter int numPeople;
    private final @Getter long budget;
    private final @Getter LocalDate startDate;
    private final @Getter String origin;
    private final @Getter List<Destination> destinations;
    private final @Getter List<Flight> flights;
    private final List<Hotel> hotels;
    private final @Getter int cost;
    
    private List<Destination> orderDestinations() {
        final List<String> waypoints = new ArrayList<>(noDateDests.size());
        for (final NoDateDestination noDateDest : noDateDests) {
            waypoints.add(noDateDest.getAddress());
        }
        final DrivingDirections directions = new GoogleDrivingDirectionsRequest(origin, waypoints)
                .getResponseSafely();
        if (directions.isImpossible()) {
            return orderOverseasDestinations();
        }
        
        final List<Integer> waypointOrder = directions.getWaypointOrder();
        final List<Destination> dests = new ArrayList<>(waypointOrder.size());
        final int numDaysSinceStart = 0;
        final List<Leg> legs = directions.getLegs();
        for (int i = 0; i < legs.size(); i++) {
            final Leg leg = legs.get(i);
            if (leg.getDuration().compareTo(FLYING_THRESHOLD) > 0) {
                
            }
        }
        
        List<NoDateDestination> orderedDestinations;
        try {
            orderedDestinations = DestinationOrderRequest
                    .orderedDestinations(origin, noDateDests, NoDateDestination::getAddress);
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
    
    private Hotels findOptimalHotels() {
        System.out.println("scraping hotels");
        final Hotels originalHotels = new Hotels(destinations, budget);
        System.out.println("annealing");
        final SimulatedAnnealer<Hotels> annealer = new SimulatedAnnealer<>(originalHotels); // FIXME add tuning args
        annealer.search(); // FIXME add numIters
        return annealer.getMinState();
    }
    
    public ItineraryModel(final SearchModel searchArgs) {
        numPeople = searchArgs.getNumPeople();
        budget = searchArgs.getBudget();
        startDate = searchArgs.getStartDate();
        origin = searchArgs.getOrigin();
        noDateDests = searchArgs.getDestinations();
        
        destinations = orderDestinations();
        
        final Hotels optimalHotels = findOptimalHotels();
        cost = optimalHotels.totalPrice();
        hotels = optimalHotels.getHotels();
        for (int i = 0; i < hotels.size(); i++) {
            destinations.get(i).setHotel(hotels.get(i));
        }
        
        hotels.forEach(System.out::println);
    }
    
}
