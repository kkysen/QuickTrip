package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.json.Json;

import java.time.Duration;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
@RequiredArgsConstructor
@Getter
public class Leg {
    
    private static final String GOOGLE_MAPS_BASE_URL = "https://www.google.com/maps/dir/";
    
    //private final Distance distance;
    
    @JsonAdapter(SecondsAdapter.class)
    private final Duration duration;
    
    @SerializedName("start_address")
    private final String startAddress;
    
    @SerializedName("start_location")
    private final LatLng startLatLng;
    
    private @Setter(AccessLevel.PACKAGE) Waypoint startLocation;
    
    @SerializedName("end_address")
    private final String endAddress;
    
    @SerializedName("end_location")
    private final LatLng endLatLng;
    
    private @Setter(AccessLevel.PACKAGE) Waypoint endLocation;
    
    private String googleMapsUrl;
    
    protected void setGoogleMapsUrl() {
        googleMapsUrl = GOOGLE_MAPS_BASE_URL
                + startLocation.getAddress() + "/" + endLocation.getAddress();
    }
    
    // I don't think this is needed
    //private final List<Step> steps;
    
}
