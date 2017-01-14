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
public class NearbySearch<E extends NearbyResult> extends PlaceSearch<E> {
    
    public NearbySearch(final String status, final List<E> results) {
        super(status, results);
    }
    
}
