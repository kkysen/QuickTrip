package io.github.kkysen.quicktrip.apis.google.flights;

import static io.github.kkysen.quicktrip.data.airports.Airports.AIRPORTS;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.QueryField;
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
    
    private final @QueryField Airport origin;
    private final @QueryField Airport destination;
    private final @QueryField LocalDate date;
    private final @QueryField int numPeople;
    private final @QueryField int numSolutions;
    
    public GoogleFlightsRequest(final Airport origin, final Airport destination,
            final LocalDate date, final int numPeople) {
        this(origin, destination, date, numPeople, 1);
    }
    
    public GoogleFlightsRequest(final Airport origin, final Airport destination,
            final int numPeople) {
        this(origin, destination, LocalDate.now(), numPeople);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (date == null ? 0 : date.hashCode());
        result = prime * result + (destination == null ? 0 : destination.hashCode());
        result = prime * result + numPeople;
        result = prime * result + numSolutions;
        result = prime * result + (origin == null ? 0 : origin.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GoogleFlightsRequest other = (GoogleFlightsRequest) obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (destination == null) {
            if (other.destination != null) {
                return false;
            }
        } else if (!destination.equals(other.destination)) {
            return false;
        }
        if (numPeople != other.numPeople) {
            return false;
        }
        if (numSolutions != other.numSolutions) {
            return false;
        }
        if (origin == null) {
            if (other.origin != null) {
                return false;
            }
        } else if (!origin.equals(other.origin)) {
            return false;
        }
        return true;
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
        System.out.println(originAirports);
        final List<Airport> destinationAirports = AIRPORTS.near(destination);
        System.out.println(destinationAirports);
        for (final Airport originAirport : originAirports) {
            for (final Airport destinationAirport : destinationAirports) {
                final GoogleFlightsRequest request = new GoogleFlightsRequest(originAirport,
                        destinationAirport, date, numPeople);
                System.out.println(request.getResponse());
                flights.addAll(request.getResponse().getFlights());
            }
        }
        return flights;
    }
    
    public static void main(final String[] args) throws ApiRequestException {
        final Airport jfk = AIRPORTS.withIataCode("JFK");
        final Airport sfo = AIRPORTS.withIataCode("SFO");
        final GoogleFlightsRequest request = new GoogleFlightsRequest(jfk, sfo, LocalDate.now(), 5,
                1);
        System.out.println(request.hashCode());
        request.getResponse().getFlights().forEach(System.out::println);
        
        near(Geolocation.at("Brooklyn"), Geolocation.at("Chicago"), LocalDate.now(), 3)
                .forEach(System.out::println);
    }
    
}
