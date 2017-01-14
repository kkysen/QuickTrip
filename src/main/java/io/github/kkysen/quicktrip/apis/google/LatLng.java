package io.github.kkysen.quicktrip.apis.google;

import io.github.kkysen.quicktrip.json.Json;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@JsonAdapter(LatLngAdapter.class)
@Json
//@RequiredArgsConstructor
@Getter
public class LatLng {
    
    @SerializedName("lat")
    private final String latitude;
    
    @SerializedName("lng")
    private final String longitude;
    
    private final double latitudeDouble;
    private final double longitudeDouble;
    
    public LatLng(final String latitude, final String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        latitudeDouble = Double.parseDouble(latitude);
        longitudeDouble = Double.parseDouble(longitude);
    }
    
    public LatLng(final double latitude, final double longitude) {
        latitudeDouble = latitude;
        longitudeDouble = longitude;
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
    }
    
    @Override
    public String toString() {
        return latitude + "," + longitude;
    }
    
}
