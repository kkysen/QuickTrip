package io.github.kkysen.quicktrip.apis.google.maps.directions;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsRequest;
import io.github.kkysen.quicktrip.apis.google.maps.directions.response.DrivingDirections;

import java.nio.file.Path;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class GoogleDrivingDirectionsRequest extends GoogleMapsRequest<DrivingDirections> {
    
    private final @QueryField String origin;
    private final @QueryField String destination;
    private final @QueryField String mode = "driving";
    private final @QueryField String waypoints;
    private final @QueryField String units = "metric";
        
    public GoogleDrivingDirectionsRequest(final String origin, final String destination,
            final List<String> waypoints, final boolean optimize) {
        this.origin = origin;
        this.destination = destination;
        if (waypoints.size() == 0) {
            this.waypoints = null;
            return;
        }
        if (optimize) {
            waypoints.add(0, "optimize:true");
        }
        this.waypoints = String.join("|", waypoints);
    }
    
    public GoogleDrivingDirectionsRequest(final String origin, final String destination,
            final List<String> waypoints) {
        this(origin, destination, waypoints, true);
    }
    
    public GoogleDrivingDirectionsRequest(final String origin, final List<String> waypoints,
            final boolean optimize) {
        this(origin, origin, waypoints, optimize);
    }
    
    public GoogleDrivingDirectionsRequest(final String origin, final List<String> waypoints) {
        this(origin, origin, waypoints, true);
    }
    
    @Override
    protected String getMapsRequestType() {
        return "directions";
    }
    
    @Override
    protected Class<? extends DrivingDirections> getResponseClass() {
        return DrivingDirections.class;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("directions");
    }
    
}