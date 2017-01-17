package io.github.kkysen.quicktrip.apis.google.flights;

import java.util.List;

import io.github.kkysen.quicktrip.json.Json;
import lombok.Getter;

@Json
@Getter
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
		return (int) Double.parseDouble(saleTotal.subString(2));
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
		Set<String> airports = new HashSet<>();
		getSegments().stream()
			.forEach(segment -> {
				airports.add(segment.getLeg().get(0).getOrigin());
				airports.add(segment.getLeg().get(0).getDestination());
			});
		return airports;
	}
	
	
	
	private List<Airport> getAirportList() {
		List<Airport> ports = new ArrayList<>();
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
		List<Airport> l = getAirportList();
		return l.get(l.size()-1);
	}
}
