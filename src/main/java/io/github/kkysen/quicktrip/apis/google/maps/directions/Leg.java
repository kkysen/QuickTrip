package io.github.kkysen.quicktrip.apis.google.maps.directions;

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
public class Leg {
    
    @SerializedName("distance")
    private Distance distance;
    
    @SerializedName("duration")
    private Duration duration;
    
    @SerializedName("start_address")
    private String startAddress;
    
    @SerializedName("start_location")
    private LatLng startLocation;
    
    @SerializedName("end_address")
    private String endAddress;
    
    @SerializedName("end_location")
    private LatLng endLocation;
    
    @SerializedName("steps")
    private List<Step> steps;
    
}
