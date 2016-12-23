package io.github.kkysen.quicktrip.apis.google.maps.directions;

import io.github.kkysen.quicktrip.apis.Json;

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
public class LatLng {
    
    @SerializedName("lat")
    private double lat;
    
    @SerializedName("lng")
    private double lng;
    
}
