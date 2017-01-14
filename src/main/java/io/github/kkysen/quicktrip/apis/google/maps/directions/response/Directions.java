package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.apis.google.GoogleApiResponse;
import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.reflect.Reflect;

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
public class Directions extends GoogleApiResponse {
    
    @SerializedName("geocoded_waypoints")
    private final List<Waypoint> waypoints;
    
    private final List<Route> routes;
    
    public Directions(final String status, final List<Waypoint> waypoints,
            final List<Route> routes) {
        super(status);
        this.waypoints = waypoints;
        this.routes = routes;
    }
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
}
