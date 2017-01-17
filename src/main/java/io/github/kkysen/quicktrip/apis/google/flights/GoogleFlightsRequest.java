package io.github.kkysen.quicktrip.apis.google.flights;

import static io.github.kkysen.quicktrip.data.airports.Airports.AIRPORTS;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.google.GoogleApiPostRequest;
import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.app.data.Flight;
import io.github.kkysen.quicktrip.data.airports.Airport;
import io.github.kkysen.quicktrip.data.airports.Airports;
import io.github.kkysen.quicktrip.json.Json;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.JsonAdapter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonAdapter(GoogleFlightsRequestAdapter.class)
@Json
@RequiredArgsConstructor
@Getter
public class GoogleFlightsRequest extends GoogleApiPostRequest<GoogleFlights> {
    
    //private static final Type RESPONSE_TYPE = new TypeToken<List<GoogleFlights>>() {}.getType();
    
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
    protected Class<? extends GoogleFlights> getResponseClass() {
        return GoogleFlights.class;
    }
    
//    @Override
//    protected Type getResponseType() {
//        return RESPONSE_TYPE;
//    }
    
    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("flights");
    }
    
//    @Override
//    protected void addTypeAdapters(final List<Pair<Type, TypeAdapter<?>>> adapters) {
//        super.addTypeAdapters(adapters);
//        adapters.add(Pair.of(RESPONSE_TYPE, new GoogleFlightsAdapter()));
//    }
    
    public static List<Flight> near(final Geolocation origin, final Geolocation destination,
            final LocalDate date, final int numPeople) throws ApiRequestException {
        final List<Flight> flights = new ArrayList<>(Airports.NUM_NEAR * Airports.NUM_NEAR);
        final List<Airport> originAirports = AIRPORTS.near(origin);
        final List<Airport> destinationAirports = AIRPORTS.near(destination);
        for (final Airport originAirport : originAirports) {
            for (final Airport destinationAirport : destinationAirports) {
                final GoogleFlightsRequest request = new GoogleFlightsRequest(originAirport,
                        destinationAirport, date, numPeople);
                flights.addAll(request.getResponse().getFlights());
            }
        }
        return flights;
    }
    
    public static void main(final String[] args) throws ApiRequestException {
        final Airport jfk = Airport.fromIataCode("JFK");
        final Airport sfo = Airport.fromIataCode("SFO");
        final GoogleFlightsRequest request = new GoogleFlightsRequest(jfk, sfo, 5);
        final List<Flight> response = request.getResponse().getFlights();
        response.forEach(System.out::println);
    }
    
}
