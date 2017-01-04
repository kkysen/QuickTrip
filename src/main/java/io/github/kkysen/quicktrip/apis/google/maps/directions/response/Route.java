package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.json.Json;

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
public class Route {
    
    @SerializedName("bounds")
    private Bounds bounds;
    
    @SerializedName("coprights")
    private String copyrights;
    
    @SerializedName("legs")
    private List<Leg> legs;
    
    @SerializedName("overview_polyline")
    private Polyline polyline;
    
    @SerializedName("summary")
    private String summary;
    
    @SerializedName("warnings")
    private List<String> warnings; // FIXME not sure if supposed to be String
    
    @SerializedName("waypoint_order")
    private List<Integer> waypointOrder;
    
}
