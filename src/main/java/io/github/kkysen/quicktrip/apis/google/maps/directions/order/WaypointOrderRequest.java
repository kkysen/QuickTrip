package io.github.kkysen.quicktrip.apis.google.maps.directions.order;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsApiRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.GoogleMapsDirectionsException;
import io.github.kkysen.quicktrip.apis.google.maps.directions.order.response.WaypointOrderOnlyDirections;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> NoDateDestination class containing the String address
 */
@RequiredArgsConstructor
public class WaypointOrderRequest<E> extends GoogleMapsApiRequest<WaypointOrderOnlyDirections> {
    
    private final @QueryField String origin;
    private final @QueryField(encode = false) List<E> destinations;
    private final Function<E, String> addressExtractor;
    
    @Override
    protected String getRequestType() {
        return "directions";
    }
    
    @Override
    protected Class<? extends WaypointOrderOnlyDirections> getPojoClass() {
        return WaypointOrderOnlyDirections.class;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("directions").resolve("order");
    }
    
    @Override
    protected void modifyQuery(final Map<String, String> query) {
        super.modifyQuery(query);
        final StringJoiner sj = new StringJoiner("|");
        sj.add("optimize:true");
        for (final E dest : destinations) {
            sj.add(addressExtractor.apply(dest));
        }
        final String waypoints = sj.toString();
        query.put("waypoints", waypoints);
        
        query.put("destination", origin);
    }
    
    private List<Integer> destinationOrder() throws IOException {
        final WaypointOrderOnlyDirections response = getResponse();
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
        return new WaypointOrderRequest<E>(origin, destinations, addressExtractor).orderedDestinations();
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
