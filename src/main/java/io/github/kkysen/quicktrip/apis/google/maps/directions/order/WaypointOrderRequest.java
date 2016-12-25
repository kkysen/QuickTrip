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

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class WaypointOrderRequest extends GoogleMapsApiRequest<WaypointOrderOnlyDirections> {
    
    private final @QueryField String origin;
    private final @QueryField(encode = false) List<String> destinations;
    
    @Override
    protected String getRequestType() {
        return "directions";
    }
    
    @Override
    protected Class<? extends WaypointOrderOnlyDirections> getPojoClass() {
        return WaypointOrderOnlyDirections.class;
    }
    
    @Override
    protected Path getRelativePath() {
        return super.getRelativePath().resolve("directions").resolve("order");
    }
    
    @Override
    protected void modifyQuery(final Map<String, String> query) {
        super.modifyQuery(query);
        final String waypoints = "optimize:true|" + String.join("|", destinations);
        query.put("waypoints", waypoints);
        
        query.put("destination", origin);
    }
    
    public List<String> orderedDestinations() throws IOException {
        final List<String> orderedDestinations = new ArrayList<>(destinations.size());
        final WaypointOrderOnlyDirections response = get();
        if (response == null) {
            throw new GoogleMapsDirectionsException(url);
        }
        for (final int orderedIndex : get().waypointOrder()) {
            orderedDestinations.add(destinations.get(orderedIndex));
        }
        return orderedDestinations;
    }
    
    public static List<String> orderedDestinations(final String origin,
            final List<String> destinations) throws IOException {
        return new WaypointOrderRequest(origin, destinations).orderedDestinations();
    }
    
    public static void main(final String[] args) throws IOException {
        final List<String> destinations = new ArrayList<>(Arrays.asList(
                "San Francisco, CA",
                "Seattle, WA",
                "Los Angeles, CA",
                "Austin, TX",
                "Orlando, FL",
                "Chicago, IL",
                "Montreal, Canada"
                ));
        final String origin = "296 6th St, Brooklyn, NY";
        orderedDestinations(origin, destinations).forEach(System.out::println);
    }
    
}
