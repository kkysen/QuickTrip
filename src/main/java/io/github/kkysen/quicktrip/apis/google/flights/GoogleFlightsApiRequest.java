package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.apis.google.GoogleApiPostRequest;
import io.github.kkysen.quicktrip.json.Json;

import java.nio.file.Path;
import java.time.LocalDate;

import com.google.gson.annotations.JsonAdapter;

import lombok.Getter;

@JsonAdapter(GoogleFlightsApiRequestAdapter.class)
@Json
@Getter
public class GoogleFlightsApiRequest
        extends GoogleApiPostRequest<GoogleFlights> {
    
    private static final String BASE_URL = "https://www.googleapis.com/qpxExpress/v1/trips/search";
    
    private String origin;
    private String destination;
    private LocalDate date;
    private int numPeople;
    private int numSolutions;
    
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