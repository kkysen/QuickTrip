package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.apis.google.GoogleApiPostRequest;
import io.github.kkysen.quicktrip.json.Json;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.LinkedHashMap;

import com.google.gson.annotations.JsonAdapter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonAdapter(GoogleFlightsApiRequestAdapter.class)
@Json
@RequiredArgsConstructor
@Getter
public class GoogleFlightsApiRequest extends GoogleApiPostRequest<GoogleFlights> {
    
    private static final String BASE_URL = "https://www.googleapis.com/qpxExpress/v1/trips/search";
    
    private final String origin;
    private final String destination;
    private final LocalDate date;
    private final int numPeople;
    private final int numSolutions;
    
    public GoogleFlightsApiRequest(final String origin, final String destination, final LocalDate date, final int numPeople) {
        this(origin, destination, date, numPeople, 1);
    }
    
    public GoogleFlightsApiRequest(final String origin, final String destination, final int numPeople) {
        this(origin, destination, LocalDate.now(), numPeople);
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
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(final String[] args) throws IOException {
        final GoogleFlightsApiRequest request = new GoogleFlightsApiRequest("JFK", "SFO", 5);
        final LinkedHashMap response = request.getResponse();
        response.entrySet().forEach(System.out::println);
    }
    
}
