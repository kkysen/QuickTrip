package io.github.kkysen.quicktrip.apis.google.places;

import io.github.kkysen.quicktrip.apis.google.GoogleApiResponse;
import io.github.kkysen.quicktrip.json.Json;

import java.util.List;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> type of result
 */
@Json
@Getter
public class PlaceSearch<E extends PlaceResult> extends GoogleApiResponse {
    
    public PlaceSearch(final String status, final List<E> results) {
        super(status);
        this.results = results;
    }
    
    private final List<E> results;
    
}
