package io.github.kkysen.quicktrip.apis.google.maps;

import io.github.kkysen.quicktrip.apis.JsonApiRequest;

public abstract class GoogleMapsApiRequest<R> extends JsonApiRequest<R> {
    
    private static final String API_KEY = "AIzaSyBrc16mMFU7w8Hyo7nFD6ny5SjeOapkY9Q";
    
    private static final String URL = "https://maps.googleapis.com/maps/api/$REQUEST_TYPE/json";
    
    @Override
    protected String getApiKey() {
        return API_KEY;
    }
    
    @Override
    protected String getBaseUrl() {
        return URL.replace("$REQUEST_TYPE", getRequestType());
    }
    
    protected abstract String getRequestType();
    
}
