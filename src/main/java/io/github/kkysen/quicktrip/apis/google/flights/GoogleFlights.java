package io.github.kkysen.quicktrip.apis.google.flights;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.kkysen.quicktrip.app.data.Flight;
import io.github.kkysen.quicktrip.data.airports.Airport;
import io.github.kkysen.quicktrip.json.Json;

import java.util.List;

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
public class GoogleFlights implements Flight {
	private Trip trips;

	@Override
	public Duration getDuration() {
		return null;
	}
	
	public void listCheapest() {
		//The total price is in Trip.Option.saletotal
		//The airports involved is in Trip.Option.Slice.Segment.Leg.origin and destination
		List<Option> sortedOptions = trips.getTripOption().stream()
			.sorted((first, second) -> {
				//signum gets the sign of the value
				return (int) Math.signum(
						Double.parseDouble(first.getSaleTotal().substring(2)) -
						Double.parseDouble(first.getSaleTotal().substring(2)));
			})
			.collect(Collectors.toList());
		
		//Now to get the airports
		/*List<Set<String>> airports = sortedOptions.stream()
				.flatMap(option -> {
					option.getSlice().stream()
						.flatMap(slice -> {
							slice.getSegment().stream();
						})
					return List<Set<String>>;
				})
				.collect(Collectors.toList());*/
		
		//time for drastic measures
		//slice to segment
		/*List<String> airports = */
		List<Segment> seg = sortedOptions.get(0).getSlice().stream()
				.flatMap(slice -> {
					//reduce to stream of segments
					return slice.getSegment().stream();
				})
				.collect(Collectors.toList());
		
		//segment to set of ariports
		Set<String> airports = new HashSet<>();
		seg.stream()
			.forEach(segment -> {
				airports.add(segment.getLeg().get(0).getOrigin());
				airports.add(segment.getLeg().get(0).getDestination());
			});

				/*.flatMap(option -> {
					option.getSlice().get(0).getSegment().stream()
						.flatMap(segment -> {
							Set<String> s =
							segment.getLeg().get(0).getOrigin()
						})
						
				})*/
				
	}
	
	public List<Flight> getFlights() {
        return null;
    }

	@Override
	public Airport getStartAirport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Airport getEndAirport() {
		// TODO Auto-generated method stub
		return null;
	}



}
