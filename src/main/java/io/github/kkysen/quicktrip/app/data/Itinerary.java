package io.github.kkysen.quicktrip.app.data;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.google.flights.GoogleFlightsRequest;
import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.DrivingDirections;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.Leg;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.Waypoint;
import io.github.kkysen.quicktrip.app.QuickTripConstants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Itinerary {
    
    private final Geolocation origin;
    private final LocalDate startDate;
    private final int numPeople;
    
    private int daysElapsed = 0;
    
    // a null in the list means there is a flight there
    private final @Getter List<Destination> destinations = new ArrayList<>();
    
    private final List<List<Flight>> possibleFlights = new ArrayList<>();
    
    public Itinerary(final Geolocation origin, final LocalDate startDate, final int numPeople) {
        this.origin = origin;
        this.startDate = startDate;
        this.numPeople = numPeople;
    }
    
    private LocalDate getDate() {
        return startDate.plusDays(daysElapsed);
    }
    
    public void addFlight(final Geolocation from, final Geolocation to) throws ApiRequestException {
        possibleFlights.add(GoogleFlightsRequest.near(from, to, getDate(), numPeople));
        destinations.add(null);
        if (daysElapsed != 0) {
            daysElapsed++;
        }
    }
    
    public void addFlight(final Geolocation to) throws ApiRequestException {
        Geolocation from;
        final int numDests = destinations.size();
        if (numDests == 0) {
            from = origin;
        } else {
            from = destinations.get(numDests - 1).getLocation();
        }
        addFlight(from, to);
    }
    
    private void addDestination(final Geolocation location, final int numDays) {
        final LocalDate startDate = getDate();
        daysElapsed += numDays;
        final LocalDate endDate = getDate();
        destinations.add(new Destination(location, numDays, startDate, endDate, numDays));
    }
    
    private void addDestination(final Leg leg, final int numDays) {
        final Geolocation from = leg.getStartLocation();
        final Geolocation to = leg.getEndLocation();
        if (leg.getDuration().compareTo(QuickTripConstants.FLYING_THRESHOLD) > 0) {
            try {
                addFlight(from, to);
            } catch (final ApiRequestException e) {
                // flight not necessary
                daysElapsed++; // since drive is still so long, next hotel is next day
            }
        }
        addDestination(to, numDays);
    }
    
    public void addFlight(final Geolocation to, final int numDays) throws ApiRequestException {
        addFlight(to);
        addDestination(to, numDays);
    }
    
    public void addDestination(final Leg leg, final NoDateDestination noDateDest) {
        addDestination(leg, noDateDest.getNumDays());
    }
    
    public void addDirections(final DrivingDirections directions,
            final List<NoDateDestination> noDateDests) {
        final List<Integer> waypointOrder = directions.getWaypointOrder();
        final List<Leg> legs = directions.getLegs();
        for (int i = 0; i < waypointOrder.size(); i++) {
            addDestination(legs.get(i), noDateDests.get(waypointOrder.get(i)));
        }
        // "add" last leg whose destination is the origin in order to check for a flight
        addDestination(legs.get(legs.size() - 1), 0);
        // remove the last origin destination if it is the origin
        final int lastIndex = destinations.size() - 1;
        final Destination lastDest = destinations.get(lastIndex);
        if (!lastDest.getLocation().equals(origin)) {
            destinations.remove(lastIndex);
        }
    }
    
    private DrivingDirections getDirectionsWithOrigin(
            final List<DrivingDirections> clusteredDirections) {
        for (int i = 0; i < clusteredDirections.size(); i++) {
            if (clusteredDirections.get(i).getOrigin().equals(origin)) {
                return clusteredDirections.remove(i);
            }
        }
        return null; // shouldn't happen
    }
    
    private void addDirections(final DrivingDirections directions, final Map<Geolocation, Integer> numDaysAtEachLocation) {
        // no destination at local origin, already taken care of
        for (final Leg leg : directions.getLegs()) {
            final Waypoint endLocation = leg.getEndLocation();
            addDestination(leg, numDaysAtEachLocation.get(endLocation));
        }
    }
    
    public void addClusteredDirections(final List<DrivingDirections> clusteredDirections,
            final List<NoDateDestination> noDateDests) throws ApiRequestException {
        final Map<Geolocation, Integer> numDaysAtEachLocation = new HashMap<>(noDateDests.size());
        noDateDests.forEach(noDateDest -> numDaysAtEachLocation.put(noDateDest.getLocation(),
                noDateDest.getNumDays()));
        final DrivingDirections directionsFromOrigin = getDirectionsWithOrigin(clusteredDirections);
        for (final DrivingDirections directions : clusteredDirections) {
            final Geolocation localOrigin = directions.getOrigin();
            addFlight(localOrigin, numDaysAtEachLocation.get(localOrigin));
            addDirections(directions, numDaysAtEachLocation);
        }
        // I chose to put directions w/ origin at end, no particular reason
        addFlight(origin);
        addDirections(directionsFromOrigin, numDaysAtEachLocation);
    }
    
    public List<DrivingDirections> getDirections() {
        
    }
    
}
