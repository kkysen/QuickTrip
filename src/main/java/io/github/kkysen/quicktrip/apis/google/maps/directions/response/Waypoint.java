package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

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
public class Waypoint {
    
    @SerializedName("geocoder_staus")
    private String status;
    
    @SerializedName("place_id")
    private String id;
    
    @SerializedName("types")
    private List<String> types;
    //private List<WaypointType> types;
    
}
