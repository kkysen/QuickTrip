package io.github.kkysen.quicktrip.apis.google.maps.directions.order.response;

import io.github.kkysen.quicktrip.apis.Json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WaypointOrderOnlyRoute {
    
    @SerializedName("waypoint_order")
    private List<Integer> waypointOrder;
    
}
