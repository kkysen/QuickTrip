package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.app.data.Flight;
import io.github.kkysen.quicktrip.data.airports.Airport;
import io.github.kkysen.quicktrip.data.airports.Airports;
import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.reflect.Reflect;

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
 * @author Khyber Sen
 */
@Json
@RequiredArgsConstructor
@Getter
public class GoogleFlights {
    
    private Trip trips;
    //private List<GoogleFlight> sortedOptions;
    //private int duration;
    //private  ports;
    
    /*public void listCheapest() {
    	//The total price is in Trip.Option.saletotal
    	//The airports involved is in Trip.Option.Slice.Segment.Leg.origin and destination
    	sortedOptions = trips.getTripOption().stream()
    		.sorted((first, second) -> {
    			//signum gets the sign of the value
    			return (int) Math.signum(
    					Double.parseDouble(first.getSaleTotal().substring(2)) -
    					Double.parseDouble(first.getSaleTotal().substring(2)));
    		})
    		.collect(Collectors.toList());
    	
    	//Now to get the airports
    	List<Set<String>> airports = sortedOptions.stream()
    			.flatMap(option -> {
    				option.getSlice().stream()
    					.flatMap(slice -> {
    						slice.getSegment().stream();
    					})
    				return List<Set<String>>;
    			})
    			.collect(Collectors.toList());
    	
    	//time for drastic measures
    	//slice to segment
    	List<String> airports = 
    	List<Segment> seg = sortedOptions.get(0).getSlice().stream()
    			.flatMap(slice -> {
    				//reduce to stream of segments
    				return slice.getSegment().stream();
    			})
    			.collect(Collectors.toList());
    	
    	//duration = seg.get(0).getDuration();
    	
    	//segment to set of ariports
    	Set<String> airports = new HashSet<>();
    	seg.stream()
    		.forEach(segment -> {
    			airports.add(segment.getLeg().get(0).getOrigin());
    			airports.add(segment.getLeg().get(0).getDestination());
    		});
    
    			.flatMap(option -> {
    				option.getSlice().get(0).getSegment().stream()
    					.flatMap(segment -> {
    						Set<String> s =
    						segment.getLeg().get(0).getOrigin()
    					})
    					
    			})
    	
    	//convert string iata to airport
    	List<Airport> ports = new ArrayList<>();
    	airports.stream()
    		.forEach(airport -> {
    			ports.add(Airports.AIRPORTS.withIataCode(airport));
    		});
    	
    	
    }*/
    
    //for each option, pricae, duration
    public List<GoogleFlight> sortOptions() {
        final List<GoogleFlight> sortedOptions = trips.getTripOption().stream()
                .sorted((first, second) -> {
                    //signum gets the sign of the value
                    return (int) Math.signum(
                            Double.parseDouble(first.getSaleTotal().substring(2)) -
                                    Double.parseDouble(first.getSaleTotal().substring(2)));
                })
                .collect(Collectors.toList());
        return sortedOptions;
    }
    
    private List<GoogleFlight> shortestFlights() {
        final List<GoogleFlight> flights = trips.getTripOption();
        for (final Flight flight : flights) {
            System.out.println(flight.getDuration().getSeconds());
        }
        flights.sort((flight1, flight2) -> {
            return flight1.getDuration().compareTo(flight2.getDuration());
        });
        return flights;
    }
    
    public List<? extends Flight> getFlights() {
        if (trips == null || trips.getTripOption() == null) {
            return new ArrayList<>();
        }
        return shortestFlights();
    }
    
    public GoogleFlight getOption(final int index) {
        return sortOptions().get(index);
    }
    
    public int getNumOptions() {
        return sortOptions().size();
    }
    
    private List<Segment> getSegments() {
        return sortOptions().get(0).getSlice().stream()
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
    
    public List<Airport> getAirportList() {
        final List<Airport> ports = new ArrayList<>();
        getAirportIatas().stream()
                .forEach(airport -> {
                    ports.add(Airports.AIRPORTS.withIataCode(airport));
                });
        
        return ports;
    }
    
    //	@Override
    //	public Duration getDuration() {
    //		return Duration.ofMinutes(getSegments().get(0).getDuration());
    //	}
    //	
    //	@Override
    //	public Airport getStartAirport() {
    //		return getAirportList().get(0);
    //	}
    //
    //	@Override
    //	public Airport getEndAirport() {
    //		List<Airport> l = getAirportList();
    //		return l.get(l.size()-1);
    //	}
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
}
