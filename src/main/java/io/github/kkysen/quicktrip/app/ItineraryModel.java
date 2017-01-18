package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.apis.google.geocoding.GoogleGeocodingRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.GoogleDrivingDirectionsRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.DrivingDirections;
import io.github.kkysen.quicktrip.app.data.Itinerary;
import io.github.kkysen.quicktrip.app.data.NoDateDestination;
import io.github.kkysen.quicktrip.app.input.InputError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import lombok.Getter;

import javafx.application.Platform;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ItineraryModel {
    
    private final transient List<NoDateDestination> noDateDests;
    private transient List<String> waypoints;
    
    private final @Getter Geolocation origin;
    
    private final Itinerary itinerary;
    
    private DrivingDirections getInitialDirections() {
        waypoints = noDateDests.stream()
                .map(NoDateDestination::getLocation)
                .map(Geolocation::getAddress)
                .collect(Collectors.toList());
        return new GoogleDrivingDirectionsRequest(origin.getAddress(), waypoints)
                .getResponseSafely();
    }
    
    private void finishItinerary() {
        final DrivingDirections directions = getInitialDirections();
        if (directions.isImpossible()) {
            try {
                finishOverseasItinerary();
                return;
            } catch (final NoTripFoundError e) {
                e.getErrorDialog().showAndWait();
                Platform.exit(); // this is a fatal error
            }
        }
        itinerary.addDirections(directions, noDateDests);
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
                    final String destination = destinations.remove(destinations.size() - 1);
                    return new GoogleDrivingDirectionsRequest(origin, destination, destinations);
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
    
    private void finishOverseasItinerary() throws NoTripFoundError {
        final List<Geolocation> locations = getGeolocations();
        final List<List<Geolocation>> clusters = getClusters(locations);
        final List<DrivingDirections> clusteredDirections = getClusteredDirections(clusters);
        checkForImpossibleTrips(clusteredDirections, clusters);
        try {
            itinerary.addClusteredDirections(clusteredDirections, noDateDests);
        } catch (final ApiRequestException e) {
            throw new RuntimeException(e); // FIXME not sure what to do
        }
    }
    
    public ItineraryModel(final SearchModel searchArgs) {
        origin = searchArgs.getOrigin();
        noDateDests = searchArgs.getDestinations();
        itinerary = new Itinerary(searchArgs);
        finishItinerary();
        itinerary.close();
    }
    
}
