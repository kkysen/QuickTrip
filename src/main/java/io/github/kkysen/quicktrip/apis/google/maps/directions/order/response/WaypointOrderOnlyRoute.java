package io.github.kkysen.quicktrip.apis.google.maps.directions.order.response;

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
public class WaypointOrderOnlyRoute {
    
    @SerializedName("waypoint_order")
    private List<Integer> waypointOrder;
    
}
