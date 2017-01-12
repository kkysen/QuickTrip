package io.github.kkysen.quicktrip.apis.google.maps.directions;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsApiRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.Directions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class DirectionsApiRequest extends GoogleMapsApiRequest<Directions> {
    
    private final @QueryField String origin;
    private final @QueryField String destination;
    private final @QueryField String mode = "driving";
    private final @QueryField String waypoints;
    private final @QueryField String units = "imperial";
    
    public DirectionsApiRequest(final String origin, final String destination,
            final List<String> waypoints, final boolean optimize) {
        this.origin = origin;
        this.destination = destination;
        if (optimize) {
            waypoints.add(0, "optimize:true");
        }
        this.waypoints = String.join("|", waypoints);
    }
    
    public DirectionsApiRequest(final String origin, final String destination,
            final List<String> waypoints) {
        this(origin, destination, waypoints, true);
    }
    
    public DirectionsApiRequest(final String origin, final List<String> waypoints,
            final boolean optimize) {
        this(origin, origin, waypoints, optimize);
    }
    
    public DirectionsApiRequest(final String origin, final List<String> waypoints) {
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
            throws IOException {
        return new DirectionsApiRequest(origin, waypoints).getResponse();
    }
    
}