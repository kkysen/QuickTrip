package io.github.kkysen.quicktrip.apis.google.geocoding;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsRequest;

import java.nio.file.Path;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class GoogleGeocodingRequest extends GoogleMapsRequest<Geolocation> {
    
    private final @QueryField String address;
    
    @Override
    protected String getMapsRequestType() {
        return "geocode";
    }
    
    @Override
    protected Class<? extends Geolocation> getResponseClass() {
        return Geolocation.class;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("geocoding");
    }
    
}
