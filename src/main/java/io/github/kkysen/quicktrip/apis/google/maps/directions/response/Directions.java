package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.reflect.Reflect;

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
public class Directions {
    
    @SerializedName("geocoded_waypoints")
    private List<Waypoint> waypoints;
    
    @SerializedName("routes")
    private List<Route> routes;
    
    @SerializedName("status")
    private String status;
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
}
