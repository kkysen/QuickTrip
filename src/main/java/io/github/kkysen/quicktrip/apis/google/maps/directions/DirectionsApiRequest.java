package io.github.kkysen.quicktrip.apis.google.maps.directions;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsApiRequest;

import java.util.Map;

import lombok.RequiredArgsConstructor;

// dummy class for now
// will become a JSON POJO

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class DirectionsApiRequest extends GoogleMapsApiRequest<Directions> {
    
    // FIXME
    // these are Strings for now just so I can work on the rest
    // should be changed to appropriate types later
    // and QueryField#encode set to false if they are weird non-string good formatty types
    private final @QueryField String origin;
    private final @QueryField String destination;
    private final @QueryField String mode;
    private final @QueryField String waypoints;
    private final @QueryField String departureTime;
    private final @QueryField String arrivalTime;
    
    @Override
    protected String getRequestType() {
        return "directions";
    }
    
    @Override
    protected void modifyQuery(final Map<String, String> query) {
        
    }
    
    @Override
    protected Class<? extends Directions> getPojoClass() {
        return Directions.class;
    }
    
}