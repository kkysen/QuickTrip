package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.apis.google.LatLng;
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
public class Leg {
    
    private final Distance distance;
    
    private final Duration duration;
    
    @SerializedName("start_address")
    private final String startAddress;
    
    @SerializedName("start_location")
    private final LatLng startLocation;
    
    @SerializedName("end_address")
    private final String endAddress;
    
    @SerializedName("end_location")
    private final LatLng endLocation;
    
    private final List<Step> steps;
    
}
