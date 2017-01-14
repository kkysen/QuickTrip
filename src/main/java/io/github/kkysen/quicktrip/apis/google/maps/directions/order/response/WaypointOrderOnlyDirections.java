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
public class WaypointOrderOnlyDirections {
    
    @SerializedName("routes")
    private List<WaypointOrderOnlyRoute> waypointOrderOnlyRoutes;
    
    public List<Integer> waypointOrder() {
        if (waypointOrderOnlyRoutes.size() == 0) {
            return null;
        }
        return waypointOrderOnlyRoutes.get(0).getWaypointOrder();
    }
    
}
