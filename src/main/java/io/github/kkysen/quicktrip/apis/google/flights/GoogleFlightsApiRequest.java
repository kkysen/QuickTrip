package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.apis.google.GoogleApiPostRequest;

import java.nio.file.Path;

public class GoogleFlightsApiRequest
        extends GoogleApiPostRequest<FlightsRequestBody, GoogleFlights> {
    
    private static final String BASE_URL = "https://www.googleapis.com/qpxExpress/v1/trips/search";
    
    public GoogleFlightsApiRequest(final FlightsRequestBody flightsRequestBody) {
        super(flightsRequestBody);
    }
    
    @Override
    protected Class<? extends FlightsRequestBody> getRequestClass() {
        return FlightsRequestBody.class;
    }
    
    @Override
    protected Class<? extends GoogleFlights> getResponseClass() {
        return GoogleFlights.class;
    }
    
    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("flights");
    }
    
}