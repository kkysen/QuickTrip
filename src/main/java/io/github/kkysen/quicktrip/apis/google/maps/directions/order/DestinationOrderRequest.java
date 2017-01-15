package io.github.kkysen.quicktrip.apis.google.maps.directions.order;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.google.maps.directions.GoogleDrivingDirectionsRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.GoogleMapsDirectionsException;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.DrivingDirections;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> NoDateDestination class containing the String address
 */
public class DestinationOrderRequest<E> extends GoogleDrivingDirectionsRequest {
    
    private final List<E> destinations;
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("order");
    }
    
    public DestinationOrderRequest(final String origin, final List<E> destinations, final Function<E, String> addressExtractor) {
        super(origin, destinations.stream().map(addressExtractor).collect(Collectors.toList()));
        this.destinations = destinations;
    }
    
    private List<Integer> destinationOrder() throws ApiRequestException {
        final DrivingDirections response = getResponse();
        if (response == null) {
            throw new GoogleMapsDirectionsException(getUrl());
        }
        final List<Integer> order = response.waypointOrder();
        if (order == null) {
            throw new NullPointerException(getUrl());
        }
        return order;
    }
    
    public List<E> orderedDestinations() throws IOException {
        final List<E> orderedDestinations = new ArrayList<>(destinations.size());
        for (final int orderedIndex : destinationOrder()) {
            orderedDestinations.add(destinations.get(orderedIndex));
        }
        return orderedDestinations;
    }
    
    public static <E> List<E> orderedDestinations(final String origin, final List<E> destinations,
            final Function<E, String> addressExtractor) throws IOException {
        return new DestinationOrderRequest<>(origin, destinations, addressExtractor).orderedDestinations();
    }
    
    public static void main(final String[] args) throws IOException {
        final String[] dests = {
//            "Albuquerque, New Mexico",
//            "Augusta, Maine",
            "San Diego, California",
            "Orlando, Florida",
            "Seattle, Washington",
            "San Antonio, Texas",
            "Brooklyn, New York",
            "Los Angeles, California",
//            "Boston, Massachusetts",
//            "Phoenix, Arizona",
//            "Philadelphia, Pennsylvania",
//            "Portland, Oregon",
//            "Tampa Bay, Florida",
//            "Denver, Colorado",
//            "Chicago, Illinois",
//            "San Francisco, California"
        };
        
        final String origin = "296 6th St, Brooklyn, NY";
        final List<String> destinations = new ArrayList<>(Arrays.asList(dests));
        final Function<String, String> addressExtractor = Function.identity();
        orderedDestinations(origin, destinations, addressExtractor).forEach(System.out::println);
    }
    
}
