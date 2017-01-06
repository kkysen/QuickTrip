package io.github.kkysen.quicktrip.apis.google.maps;

import io.github.kkysen.quicktrip.apis.google.GoogleApiRequest;

import java.nio.file.Path;

public abstract class GoogleMapsApiRequest<R> extends GoogleApiRequest<R> {
    
    private static final String MAPS_REQUEST_TYPE = "$MAPS_REQUEST_TYPE";
    private static final String URL = "https://maps.googleapis.com/maps/api/" + MAPS_REQUEST_TYPE + "/json";
    
    @Override
    protected final String getBaseUrl() {
        return URL.replace(MAPS_REQUEST_TYPE, getMapsRequestType());
    }
    
    // should be overriden
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("maps");
    }
    
    protected abstract String getMapsRequestType();
    
}
