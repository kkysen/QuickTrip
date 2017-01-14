package io.github.kkysen.quicktrip.apis.google.places;

import io.github.kkysen.quicktrip.json.Json;

import java.util.List;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
@Getter
public class NearbyAirports extends NearbySearch<NearbyAirport> {
    
    public NearbyAirports(final String status, final List<NearbyAirport> results) {
        super(status, results);
    }
    
}
