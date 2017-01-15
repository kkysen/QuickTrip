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
public class Waypoint {
    
    @SerializedName("geocoder_status")
    private final String status;
    
    @SerializedName("place_id")
    private final String placeId;
    
    private final List<String> types;
    
}
