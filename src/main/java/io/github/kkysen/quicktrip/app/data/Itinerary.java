package io.github.kkysen.quicktrip.app.data;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.google.flights.GoogleFlightsRequest;
import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.apis.google.geocoding.GoogleGeocodingRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.GoogleDrivingDirectionsRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.DrivingDirections;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.Leg;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.Waypoint;
import io.github.kkysen.quicktrip.app.QuickTripConstants;
import io.github.kkysen.quicktrip.app.SearchModel;
import io.github.kkysen.quicktrip.data.airports.Airport;
import io.github.kkysen.quicktrip.optimization.simulatedAnnealing.SimulatedAnnealer;

import java.io.Closeable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Itinerary implements Closeable {
    
    private final Geolocation origin;
    private final LocalDate startDate;
    private final int numPeople;
    private int budget;
    
    private int daysElapsed = 0;
    
    private @Getter int numClusters;
    private @Getter final List<List<Destination>> destinations = new ArrayList<>();
    private List<Destination> currentDestinations = new ArrayList<>();
    private final Destination lastDestination;
    
    // a null in the list means there is a flight there
    private final @Getter List<Destination> hotelDestinations = new ArrayList<>();
    private List<Hotel> hotels;
    private @Getter int hotelCost;
    
    private final List<List<Flight>> possibleFlights = new ArrayList<>();
    private final List<Destination> missingAirports = new ArrayList<>(); // locations must be set later
    private List<Flight> flights = new ArrayList<>();
    private @Getter int flightCost;
    
    private @Getter List<DrivingDirections> directions;
    private @Getter int totalCost;
    
    public Itinerary(final SearchModel searchArgs) {
        origin = searchArgs.getOrigin();
        startDate = searchArgs.getStartDate();
        numPeople = searchArgs.getNumPeople();
        budget = searchArgs.getNumPeople();
        addDestination(origin, 0);
        lastDestination = currentDestinations.get(0);
    }
    
    private LocalDate getDate() {
        return startDate.plusDays(daysElapsed);
    }
    
    private void flushCurrentDestinations() {
        destinations.add(currentDestinations);
        currentDestinations = new ArrayList<>();
    }
    
    // base call
    private void addDestination(final Geolocation location, final int numDays) {
        final LocalDate startDate = getDate();
        daysElapsed += numDays;
        final LocalDate endDate = getDate();
        final Destination destination = new Destination(location, numDays, startDate, endDate,
                numPeople);
        if (numDays != 0) {
            hotelDestinations.add(destination);
        }
        currentDestinations.add(destination);
    }
    
    private void addAirport() {
        addDestination((Geolocation) null, 0);
        missingAirports.add(lastDestination);
    }
    
    // base call
    public void addFlight(final Geolocation from, final Geolocation to) throws ApiRequestException {
        possibleFlights.add(GoogleFlightsRequest.near(from, to, getDate(), numPeople));
        addAirport();
        flushCurrentDestinations();
        if (daysElapsed != 0) {
            daysElapsed++;
        }
        addAirport();
    }
    
    public void addFlight(final Geolocation to) throws ApiRequestException {
        addFlight(lastDestination.getLocation(), to);
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
        addDestination(legs.get(legs.size() - 1), 0); // no hotel
    }
    
    private void moveOriginDirections(final List<DrivingDirections> clusteredDirections) {
        // leave out last one b/c that will be where directions to origin are placed
        for (int i = 0; i < clusteredDirections.size() - 1; i++) {
            final DrivingDirections directions = clusteredDirections.get(i);
            if (directions.getOrigin().equals(origin)) {
                Collections.swap(clusteredDirections, i, 0);
            } else if (directions.getDestination().equals(origin)) {
                Collections.swap(clusteredDirections, i, clusteredDirections.size() - 1);
            }
        }
    }
    
    /*private DrivingDirections getDirectionsWithOrigin(
            final List<DrivingDirections> clusteredDirections) {
        for (int i = 0; i < clusteredDirections.size(); i++) {
            if (clusteredDirections.get(i).getOrigin().equals(origin)) {
                return clusteredDirections.remove(i);
            }
        }
        return null; // shouldn't happen
    }*/
    
    private void addDirections(final DrivingDirections directions,
            final Map<Geolocation, Integer> numDaysAtEachLocation) {
        // no destination at local origin, already taken care of
        for (final Leg leg : directions.getLegs()) {
            final Waypoint endLocation = leg.getEndLocation();
            addDestination(leg, numDaysAtEachLocation.get(endLocation));
        }
    }
    
    public void addClusteredDirections(final List<DrivingDirections> clusteredDirections,
            final List<NoDateDestination> noDateDests) throws ApiRequestException {
        noDateDests.add(new NoDateDestination(origin, 0));
        final Map<Geolocation, Integer> numDaysAtEachLocation = new HashMap<>(noDateDests.size());
        noDateDests.forEach(noDateDest -> numDaysAtEachLocation.put(noDateDest.getLocation(),
                noDateDest.getNumDays()));
        //final DrivingDirections directionsToOrigin = getDirectionsWithOrigin(clusteredDirections);
        moveOriginDirections(clusteredDirections);
        for (final DrivingDirections directions : clusteredDirections) {
            final Geolocation localOrigin = directions.getOrigin();
            addFlight(localOrigin, numDaysAtEachLocation.get(localOrigin));
            addDirections(directions, numDaysAtEachLocation);
        }
        
        //// I chose to put directions w/ origin first, no particular reason
        //addFlight(directionsToOrigin.getOrigin(), numDaysAtEachLocation.get(localOrigin));
        //addDirections(directionsToOrigin, numDaysAtEachLocation);
    }
    
    private Geolocation getAirportLocation(final Airport airport) {
        return new GoogleGeocodingRequest(airport.getLocation()).getResponseSafely();
    }
    
    public List<Flight> findOptimalFlights() {
        // need to get this list of optimal flights somehow
        //final List<OtherFlight> flights = new ArrayList<>(); // FIXME
        //        return possibleFlights.stream()
        //                .map(list -> list.get(0))
        //                .collect(Collectors.toList());
        // FIXME still
        return new Flights(possibleFlights).getFlights();
    }
    
    private void setMissingAirports() {
        final Iterator<Destination> missingAirportIter = missingAirports.iterator();
        for (final Flight flight : flights) {
            if (flight == null) {
                missingAirportIter.next();
                missingAirportIter.next();
                continue;
            }
            missingAirportIter.next().setLocation(getAirportLocation(flight.getStartAirport()));
            missingAirportIter.next().setLocation(getAirportLocation(flight.getEndAirport()));
        }
    }
    
    public List<Hotel> findOptimalHotels() {
        System.out.println("scraping hotels");
        final Hotels originalHotels = new Hotels(hotelDestinations, budget);
        System.out.println("annealing");
        final SimulatedAnnealer<Hotels> annealer = new SimulatedAnnealer<>(originalHotels); // FIXME add tuning args
        annealer.search(); // FIXME add numIters
        return annealer.getMinState().getHotels();
    }
    
    public List<DrivingDirections> findDirections() {
        System.out.println("\ndestinations");
        destinations.forEach(dests -> dests.forEach(dest -> System.out.println(dest.getAddress())));
        System.out.println("\n");
        return destinations.parallelStream()
                .map(interFlightDestinations -> {
                    System.out.println(interFlightDestinations);
                    final List<String> waypoints = interFlightDestinations.stream()
                            .map(Destination::getAddress)
                            .collect(Collectors.toList());
                    final String localDestination = waypoints.remove(waypoints.size() - 1);
                    final String localOrigin = waypoints.remove(0);
                    System.out.println(waypoints);
                    return new GoogleDrivingDirectionsRequest(localOrigin, localDestination,
                            waypoints);
                })
                .peek(GoogleDrivingDirectionsRequest::getResponseSafely)
                .peek(System.out::println)
                .map(GoogleDrivingDirectionsRequest::getResponseSafely)
                .collect(Collectors.toList());
    }
    
    private int flightCost() {
        int cost = 0;
        for (final Flight flight : flights) {
            if (flight == null) {
                continue;
            }
            cost += flight.getPrice();
        }
        return cost;
    }
    
    private int hotelCost() {
        int cost = 0;
        for (final Destination dest : hotelDestinations) {
            System.out.println(dest);
            cost += dest.getHotel().getPrice();
        }
        return cost;
    }
    
    /**
     * not IO related at all, but close() should be called after adding things
     * to this itinerary
     * 
     * must be closed after setting airport locations
     * getDirections() won't work before airport locations are set
     */
    @Override
    public void close() {
        flushCurrentDestinations();
        numClusters = destinations.size();
        flights = findOptimalFlights();
        setMissingAirports();
        flightCost = flightCost();
        budget -= flightCost;
        hotels = findOptimalHotels();
        hotelCost = hotelCost();
        directions = findDirections();
        totalCost = hotelCost + flightCost();
        
        System.out.println(flightCost);
        flights.forEach(System.out::println);
        System.out.println();
        
        System.out.println(hotelCost);
        hotels.forEach(System.out::println);
        System.out.println();
        
        System.out.println(totalCost);
        directions.forEach(System.out::println);
    }
    
}
