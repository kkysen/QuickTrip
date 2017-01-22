package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.app.data.Destination;
import io.github.kkysen.quicktrip.app.data.Flight;
import io.github.kkysen.quicktrip.data.airports.Airport;
import io.github.kkysen.quicktrip.data.airports.Airports;
import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.json.PostProcessable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 
 * 
 * @author Stanley Lin
 */
@Json
@Getter
@RequiredArgsConstructor
public class GoogleFlight implements Flight, PostProcessable {
    
    private String saleTotal;
    private String id;
    private List<Slice> slice;
    private List<Pricing> pricing;
    
    private List<Airport> airports;
    
    private Duration duration;
    private int price;
    private Airport startAirport;
    private Airport endAirport;
    
    private @Setter Destination origin;
    private @Setter Destination destination;
    
    @Override
    public void postDeserialize() {
        PostProcessable.super.postDeserialize();
        duration = Duration.ofMinutes(slice.get(0).getDuration());
        price = (int) Double.parseDouble(saleTotal.split("[^0-9]+", 2)[1]);
        airports = getAirportList();
        startAirport = airports.get(0);
        endAirport = airports.get(airports.size() - 1);
    }
    
    private List<Segment> getSegments() {
        return slice.stream()
                .flatMap(slice -> {
                    //reduce to stream of segments
                    return slice.getSegment().stream();
                })
                .collect(Collectors.toList());
    }
    
    private Set<String> getAirportIatas() {
        final Set<String> airports = new HashSet<>();
        getSegments().forEach(segment -> {
            airports.add(segment.getLeg().get(0).getOrigin());
            airports.add(segment.getLeg().get(0).getDestination());
        });
        return airports;
    }
    
    private List<Airport> getAirportList() {
        final List<Airport> ports = new ArrayList<>();
        getAirportIatas().forEach(airport -> {
            ports.add(Airports.AIRPORTS.withIataCode(airport));
        });
        
        return ports;
    }
    
    @Override
    public String toString() {
        final int seconds = (int) duration.getSeconds();
        int minutes = seconds / 60;
        final int hours = minutes / 60;
        minutes %= 60;
        return "Google Flight [" + startAirport.getIataCode() + " -> " + endAirport.getIataCode()
                + ", $" + price + ", " + hours + ":" + minutes + " min";
    }
    
}
