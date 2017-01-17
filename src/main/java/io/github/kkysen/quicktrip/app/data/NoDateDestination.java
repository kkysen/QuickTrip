package io.github.kkysen.quicktrip.app.data;

import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.json.Json;

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
public class NoDateDestination {
    
    private final Geolocation location;
    private final int numDays;
    
}
