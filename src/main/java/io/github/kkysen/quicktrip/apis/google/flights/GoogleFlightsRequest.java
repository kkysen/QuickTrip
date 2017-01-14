package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.apis.google.GoogleApiPostRequest;
import io.github.kkysen.quicktrip.data.airports.Airport;
import io.github.kkysen.quicktrip.json.Json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonAdapter(GoogleFlightsRequestAdapter.class)
@Json
@RequiredArgsConstructor
@Getter
public class GoogleFlightsRequest extends GoogleApiPostRequest<List<GoogleFlight>> {
    
    private static final Type RESPONSE_TYPE = new TypeToken<List<GoogleFlight>>() {}.getType();
    
    private static final String BASE_URL = "https://www.googleapis.com/qpxExpress/v1/trips/search";
    
    private final Airport origin;
    private final Airport destination;
    private final LocalDate date;
    private final int numPeople;
    private final int numSolutions;
    
    public GoogleFlightsRequest(final Airport origin, final Airport destination,
            final LocalDate date, final int numPeople) {
        this(origin, destination, date, numPeople, 1);
    }
    
    public GoogleFlightsRequest(final Airport origin, final Airport destination,
            final int numPeople) {
        this(origin, destination, LocalDate.now(), numPeople);
    }
    
    @Override
    protected Class<? extends List<GoogleFlight>> getResponseClass() {
        return null;
    }
    
    @Override
    protected Type getResponseType() {
        return RESPONSE_TYPE;
    }
    
    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("flights");
    }
    
    @Override
    protected void addTypeAdapters(final List<Pair<Type, ? extends TypeAdapter<?>>> adapters) {
        super.addTypeAdapters(adapters);
        adapters.add(Pair.of(RESPONSE_TYPE, new GoogleFlightsAdapter()));
    }
    
    public static void main(final String[] args) throws IOException {
        final Airport jfk = Airport.fromIataCode("JFK");
        final Airport sfo = Airport.fromIataCode("SFO");
        final GoogleFlightsRequest request = new GoogleFlightsRequest(jfk, sfo, 5);
        final List<GoogleFlight> response = request.getResponse();
        response.forEach(System.out::println);
    }
    
}
