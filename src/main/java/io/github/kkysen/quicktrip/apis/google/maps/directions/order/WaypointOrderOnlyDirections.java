package io.github.kkysen.quicktrip.apis.google.maps.directions.order;

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
