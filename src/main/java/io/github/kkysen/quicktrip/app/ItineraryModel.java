package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.apis.google.geocoding.GoogleGeocodingRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.GoogleDrivingDirectionsRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.DrivingDirections;
import io.github.kkysen.quicktrip.app.data.Destination;
import io.github.kkysen.quicktrip.app.data.Flight;
import io.github.kkysen.quicktrip.app.data.Flights;
import io.github.kkysen.quicktrip.app.data.Hotel;
import io.github.kkysen.quicktrip.app.data.Hotels;
import io.github.kkysen.quicktrip.app.data.Itinerary;
import io.github.kkysen.quicktrip.app.data.NoDateDestination;
import io.github.kkysen.quicktrip.app.input.InputError;
import io.github.kkysen.quicktrip.optimization.simulatedAnnealing.SimulatedAnnealer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ItineraryModel {
    
    private final transient List<NoDateDestination> noDateDests;
    private transient List<String> waypoints;
    
    private final @Getter int numPeople;
    private final @Getter long budget;
    private final @Getter LocalDate startDate;
    private final @Getter Geolocation origin;
    private final @Getter List<Destination> destinations;
    private @Getter List<Flight> flights;
    private final List<Hotel> hotels;
    private final @Getter int cost;
    
    private final Itinerary itinerary;
    
    private DrivingDirections getInitialDirections() {
        waypoints = new ArrayList<>(noDateDests.size());
        for (final NoDateDestination noDateDest : noDateDests) {
            waypoints.add(noDateDest.getAddress());
        }
        return new GoogleDrivingDirectionsRequest(origin.getAddress(), waypoints)
                .getResponseSafely();
    }
    
    private List<Destination> orderDestinations() {
        final DrivingDirections directions = getInitialDirections();
        if (directions.isImpossible()) {
            return orderOverseasDestinations();
        }
        itinerary.addDirections(directions, noDateDests);
        return itinerary.getDestinations();
    }
    
    private List<Geolocation> getGeolocations() {
        final List<Geolocation> locations = waypoints
                .stream()
                .map(GoogleGeocodingRequest::new)
                .map(GoogleGeocodingRequest::getResponseSafely)
                .collect(Collectors.toList());
        locations.add(origin); // order doesn't matter
        return locations;
    }
    
    private List<List<Geolocation>> getClusters(final List<Geolocation> locations) {
        final double eps = QuickTripConstants.FLYING_THRESHOLD.toHours() * 100; // (100 kph ~ 60 mph)
        final Clusterer<Geolocation> clusterer = new DBSCANClusterer<>(eps, 1,
                LatLng.HAVERSINE_DISTANCE);
        return clusterer.cluster(locations)
                .stream()
                .map(Cluster::getPoints)
                .collect(Collectors.toList());
    }
    
    /**
     * @param locations geolocations to search from
     * @return origin if it's in the list, last Geolocation if it's not
     */
    private Geolocation findOrigin(final List<Geolocation> locations) {
        int index = locations.indexOf(origin);
        if (index == -1) {
            index = locations.size() - 1;
        }
        return locations.remove(index);
    }
    
    private List<DrivingDirections> getClusteredDirections(final List<List<Geolocation>> clusters) {
        return clusters.parallelStream()
                .filter(cluster -> cluster.size() >= 2) // can't have directions w/ less than 2 places
                .map(cluster -> {
                    // order doesn't matter enough for me to care about it
                    final String origin = findOrigin(cluster).getAddress();
                    final List<String> destinations = cluster.stream().map(Geolocation::getAddress)
                            .collect(Collectors.toList());
                    return new GoogleDrivingDirectionsRequest(origin, destinations);
                })
                .map(GoogleDrivingDirectionsRequest::getResponseSafely)
                .collect(Collectors.toList());
    }
    
    private static class NoTripFoundError extends InputError {
        
        private static final long serialVersionUID = 65378636161469734L;
        
        private final List<List<Geolocation>> clustersWithNoTrips = new ArrayList<>();
        
        public NoTripFoundError() {
            super("No Trip Found", null);
        }
        
        public void add(final List<Geolocation> cluster) {
            clustersWithNoTrips.add(cluster);
        }
        
        public boolean hasErrors() {
            return !clustersWithNoTrips.isEmpty();
        }
        
        public void createMsg() {
            msg = clustersWithNoTrips.stream()
                    .map(cluster -> {
                        return cluster.stream()
                                .map(Geolocation::toString)
                                .collect(Collectors.joining("\n"));
                    })
                    .collect(Collectors.joining("\n\n\n"));
        }
        
    }
    
    private void checkForImpossibleTrips(final List<DrivingDirections> directions,
            final List<List<Geolocation>> clusters) throws NoTripFoundError {
        final NoTripFoundError error = new NoTripFoundError();
        for (int i = 0; i < clusters.size(); i++) {
            if (directions.get(i).isImpossible()) {
                error.add(clusters.get(i));
            }
        }
        if (error.hasErrors()) {
            error.createMsg();
            throw error;
        }
    }

    
    private List<Destination> orderOverseasDestinations() throws NoTripFoundError {
        final List<Geolocation> locations = getGeolocations();
        final List<List<Geolocation>> clusters = getClusters(locations);
        final List<DrivingDirections> clusteredDirections = getClusteredDirections(clusters);
        checkForImpossibleTrips(clusteredDirections, clusters);
        try {
            itinerary.addClusteredDirections(clusteredDirections, noDateDests);
        } catch (final ApiRequestException e) {
            throw new RuntimeException(e); // FIXME not sure what to do
        }
        return itinerary.getDestinations();
    }
    
    private Flights findOptimalFlights(
            final List<Pair<Geolocation, Geolocation>> pairedFlightDests) {
        return null;
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
        itinerary = new Itinerary(origin, startDate, numPeople);
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
