package io.github.kkysen.quicktrip.apis.google.maps.directions;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.Directions;

import java.nio.file.Path;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class GoogleDirectionsRequest extends GoogleMapsRequest<Directions> {
    
    private final @QueryField String origin;
    private final @QueryField String destination;
    private final @QueryField String mode = "driving";
    private final @QueryField String waypoints;
    private final @QueryField String units = "metric";
    
    public GoogleDirectionsRequest(final String origin, final String destination,
            final List<String> waypoints, final boolean optimize) {
        this.origin = origin;
        this.destination = destination;
        if (optimize) {
            waypoints.add(0, "optimize:true");
        }
        this.waypoints = String.join("|", waypoints);
    }
    
    public GoogleDirectionsRequest(final String origin, final String destination,
            final List<String> waypoints) {
        this(origin, destination, waypoints, true);
    }
    
    public GoogleDirectionsRequest(final String origin, final List<String> waypoints,
            final boolean optimize) {
        this(origin, origin, waypoints, optimize);
    }
    
    public GoogleDirectionsRequest(final String origin, final List<String> waypoints) {
        this(origin, origin, waypoints, true);
    }
    
    @Override
    protected String getMapsRequestType() {
        return "directions";
    }
    
    @Override
    protected void modifyQuery(final QueryParams query) {
        // TODO
    }
    
    @Override
    protected Class<? extends Directions> getResponseClass() {
        return Directions.class;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("directions");
    }
    
    public static Directions request(final String origin, final List<String> waypoints)
            throws ApiRequestException {
        return new GoogleDirectionsRequest(origin, waypoints).getResponse();
    }
    
}