package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.apis.google.GoogleApiResponse;
import io.github.kkysen.quicktrip.json.Json;

import java.util.List;
import java.util.StringJoiner;

import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
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
    
    private @Getter(AccessLevel.NONE) List<Route> routes; // will be set to null
    
    private transient Route route;
    
    private Waypoint origin;
    private Waypoint destination;
    private List<Leg> legs;
    private List<Integer> waypointOrder;
    
    public DrivingDirections(final String status, final List<Waypoint> waypoints,
            final List<Route> routes) {
        super(status);
        this.waypoints = waypoints;
        this.routes = routes;
        postDeserialize();
    }
    
    private void setFields() {
        numWaypoints = waypoints.size();
        numLegs = numWaypoints - 1;
        origin = waypoints.get(0);
        destination = waypoints.get(numWaypoints - 1);
        route = routes.get(0);
        routes = null;
        legs = route.getLegs();
        waypointOrder = route.getWaypointOrder();
    }
    
    private void postDeserializeWaypointsAndLegs() {
        final Leg firstLeg = legs.get(0);
        origin.setAddressLocationFromLeg(firstLeg.getStartLatLng(), firstLeg.getStartAddress());
        for (int i = 0; i < numLegs; i++) {
            final Leg leg = legs.get(i);
            final Waypoint waypoint = waypoints.get(i);
            final Waypoint nextWaypoint = waypoints.get(i + 1);
            waypoint.setAddressLocationFromLeg(leg.getStartLatLng(), leg.getStartAddress());
            nextWaypoint.setAddressLocationFromLeg(leg.getEndLatLng(), leg.getEndAddress());
            leg.setStartLocation(waypoint);
            leg.setEndLocation(nextWaypoint);
            leg.setGoogleMapsUrl();
        }
    }
    
    @Override
    public void postDeserialize() {
        super.postDeserialize();
        if (isImpossible()) {
            return;
        }
        setFields();
        postDeserializeWaypointsAndLegs();
    }
    
    @Override
    public void preSerialize() {
        super.preSerialize();
        routes = null;
        route = null;
    }
    
    public String waypointsToString() {
        final StringJoiner sj = new StringJoiner(" -> ");
        for (final Waypoint waypoint : waypoints) {
            sj.add(waypoint.getAddress());
        }
        return sj.toString();
    }
    
    @Override
    public String toString() {
        return "DrivingDirections [" + waypointsToString() + "]";
    }
    
}
