package io.github.kkysen.quicktrip.apis.google.places;

import io.github.kkysen.quicktrip.apis.google.LatLng;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class GoogleNearbyAirportsRequest
        extends GoogleNearbyRequest<NearbyAirports, NearbyAirport> {
    
    @Override
    protected String getPlaceType() {
        return "airport";
    }
    
    private final @Getter(onMethod = @__(@Override)) LatLng location;
    private final @Getter(onMethod = @__(@Override)) int radius; // meters
    
}
