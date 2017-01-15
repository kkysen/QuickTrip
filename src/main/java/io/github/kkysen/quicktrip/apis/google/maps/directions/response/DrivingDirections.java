package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.apis.google.GoogleApiResponse;
import io.github.kkysen.quicktrip.json.Json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
//@RequiredArgsConstructor need super call
@Getter
public class DrivingDirections extends GoogleApiResponse {
    
    @SerializedName("geocoded_waypoints")
    private final List<Waypoint> waypoints;
    
    private final List<Route> routes;
    
    public DrivingDirections(final String status, final List<Waypoint> waypoints,
            final List<Route> routes) {
        super(status);
        this.waypoints = waypoints;
        this.routes = routes;
    }
    
    /**
     * {@link #isImpossible()} should be called beforehand
     * 
     * @see #isImpossible()
     * 
     * @return waypoint order
     */
    public List<Integer> waypointOrder() {
        return routes.get(0).getWaypointOrder();
    }
    
}
