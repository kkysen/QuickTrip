package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.apis.google.GoogleApiResponse;
import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.json.Json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

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
    
    private Route route;
    
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
        return route.getWaypointOrder();
    }
    
    @Override
    public void postDeserialize() {
        super.postDeserialize();
        route = routes.get(0);
        final List<Leg> legs = route.getLegs();
        final List<Pair<LatLng, String>> addressLocations = new ArrayList<>(legs.size());
        final Leg firstLeg = legs.get(0);
        addressLocations.add(Pair.of(firstLeg.getStartLocation(), firstLeg.getStartAddress()));
        for (final Leg leg : legs) {
            addressLocations.add(Pair.of(leg.getEndLocation(), leg.getEndAddress()));
        }
        for (int i = 0; i < legs.size(); i++) {
            final Pair<LatLng, String> addressLocation = addressLocations.get(i);
            waypoints.get(i).setAddressLocationFromLeg(addressLocation.getKey(), addressLocation.getValue());
        }
    }
    
}
