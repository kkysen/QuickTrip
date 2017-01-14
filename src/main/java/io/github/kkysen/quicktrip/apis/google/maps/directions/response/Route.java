package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.json.Json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
@RequiredArgsConstructor
@Getter
public class Route {
    
    private final Bounds bounds;
    
    private final String copyrights;
    
    private final List<Leg> legs;
    
    @SerializedName("overview_polyline")
    private final Polyline polyline;
    
    private final String summary;
    
    private final List<String> warnings; // FIXME not sure if supposed to be String
    
    @SerializedName("waypoint_order")
    private final List<Integer> waypointOrder;
    
}
