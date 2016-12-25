package io.github.kkysen.quicktrip.apis.google.maps.directions;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsApiRequest;

import java.io.IOException;
import java.nio.file.Path;
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
    
    @Override
    protected Path getRelativePath() {
        return super.getRelativePath().resolve("directions");
    }
    
    public static Directions request(final String origin, final String destination, final String mode,
            final String waypoints, final String departureTime, final String arrivalTime) throws IOException {
        return new DirectionsApiRequest(origin, destination, mode, waypoints, departureTime, arrivalTime).get();
    }
    
    public static void main(final String[] args) throws Exception {
        request("75 9th Ave, New York, NY",
                "MetLife Stadium Dr East Rutherford, NJ 0703",
                "driving",
                "",
                "",
                "");
        
        
        request("296 6th St, Brooklyn, NY",
                "15 Claremont Ave, New York, NY",
                "driving",
                "",
                "",
                "");
        
        request("75 9th Ave, New York, NY",
                "MetLife Stadium Dr East Rutherford, NJ 0703",
                "driving",
                "optimize:true|San Francisco,+CA",
                "",
                "");
        
        final String[] waypoints = {
            "optimize:true",
            "San Francisco,+CA",
            "Seattle,+WA",
            "Los Angeles,+CA",
            "Austin,+TX",
            "Orlando,+FL",
            "Chicago,+IL"
        };
        request("296 6th St, Brooklyn, NY",
                "15 Claremont Ave, New York, NY",
                "driving",
                String.join("|", waypoints),
                "",
                "");
    }
    
}