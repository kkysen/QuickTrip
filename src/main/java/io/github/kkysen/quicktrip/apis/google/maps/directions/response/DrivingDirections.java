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
    
    private int numWaypoints;
    private int numLegs;
    
    @SerializedName("geocoded_waypoints")
    private final List<Waypoint> waypoints;
    
    private final List<Route> routes;
    
    private Route route;
    private List<Leg> legs;
    private List<Integer> waypointOrder;
    
    public DrivingDirections(final String status, final List<Waypoint> waypoints,
            final List<Route> routes) {
        super(status);
        this.waypoints = waypoints;
        this.routes = routes;
    }
    
    @Override
    public void postDeserialize() {
        super.postDeserialize();
        numWaypoints = waypoints.size();
        numLegs = numWaypoints - 1;
        route = routes.get(0);
        legs = route.getLegs();
        waypointOrder = route.getWaypointOrder();
        final Leg firstLeg = legs.get(0);
        final Waypoint firstWaypoint = waypoints.get(0);
        firstWaypoint.setAddressLocationFromLeg(firstLeg.getStartLatLng(), firstLeg.getEndAddress());
        for (int i = 0; i < numLegs; i++) {
            final Leg leg = legs.get(i);
            final Waypoint waypoint = waypoints.get(i);
            final Waypoint nextWaypoint = waypoints.get(i + 1);
            waypoint.setAddressLocationFromLeg(leg.getEndLatLng(), leg.getEndAddress());
            nextWaypoint.setAddressLocationFromLeg(leg.getStartLatLng(), leg.getStartAddress());
            leg.setStartLocation(waypoint);
            leg.setEndLocation(nextWaypoint);
        }
    }
    
}
