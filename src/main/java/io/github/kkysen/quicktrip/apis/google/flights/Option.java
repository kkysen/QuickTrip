package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.app.data.Flight;
import io.github.kkysen.quicktrip.data.airports.Airport;
import io.github.kkysen.quicktrip.data.airports.Airports;
import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.reflect.Reflect;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Stanley Lin
 */
@Json
@Getter
@RequiredArgsConstructor
public class Option implements Flight {
    
    private String saleTotal;
    private String id;
    private List<Slice> slice;
    private List<Pricing> pricing;
    
    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(slice.get(0).getDuration());
    }
    
    @Override
    public int getPrice() {
        return (int) Double.parseDouble(saleTotal.split("[^0-9]+", 2)[1]);
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
        getSegments().stream()
                .forEach(segment -> {
                    airports.add(segment.getLeg().get(0).getOrigin());
                    airports.add(segment.getLeg().get(0).getDestination());
                });
        return airports;
    }
    
    private List<Airport> getAirportList() {
        final List<Airport> ports = new ArrayList<>();
        getAirportIatas().stream()
                .forEach(airport -> {
                    ports.add(Airports.AIRPORTS.withIataCode(airport));
                });
        
        return ports;
    }
    
    @Override
    public Airport getStartAirport() {
        return getAirportList().get(0);
    }
    
    @Override
    public Airport getEndAirport() {
        final List<Airport> l = getAirportList();
        return l.get(l.size() - 1);
    }
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
}
