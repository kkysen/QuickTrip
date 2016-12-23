package io.github.kkysen.quicktrip.apis.google.maps.directions;

import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsApiRequest;

import java.util.Map;

// dummy class for now
// will become a JSON POJO

/**
 * 
 * 
 * @author Khyber Sen
 */
public class DirectionsApiRequest extends GoogleMapsApiRequest<Directions> {
    
    public DirectionsApiRequest(final String origin, final String destination, final String mode, final String waypoints,
            final String departureTime, final String arrivalTime) {
        
    }
    
    @Override
    protected String getRequestType() {
        return "directions";
    }
    
    @Override
    protected Map<String, String> getQuery(final Map<String, String> query) {
        
    }
    
    @Override
    protected Class<? extends Directions> getPojoClass() {
        return Directions.class;
    }
    
}