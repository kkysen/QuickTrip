package io.github.kkysen.quicktrip.apis.google.maps.directions.order;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsApiRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
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
    
    public List<String> orderedDestinations() {
        final List<String> orderedDestinations = new ArrayList<>(destinations.size());
        WaypointOrderOnlyDirections response;
        try {
            response = get();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        for (final int orderedIndex : response.waypointOrder()) {
            orderedDestinations.add(destinations.get(orderedIndex));
        }
        return orderedDestinations;
    }
    
}
