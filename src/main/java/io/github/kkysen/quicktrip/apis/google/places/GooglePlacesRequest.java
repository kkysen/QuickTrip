package io.github.kkysen.quicktrip.apis.google.places;

import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsRequest;

import java.nio.file.Path;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class GooglePlacesRequest<R> extends GoogleMapsRequest<R> {
    
    protected abstract String getPlacesRequestType();
    
    @Override
    protected String getMapsRequestType() {
        return "places/" + getPlacesRequestType();
    }
    
    @Override
    protected Path getRelativeCachePath() {
        final Path mapsPath = super.getRelativeCachePath();
        // remove maps part
        // ideally I would make a super.super call, but that's impossible
        final Path googlePath = mapsPath.subpath(0, mapsPath.getNameCount() - 2);
        return googlePath.resolve("places");
    }
    
}
