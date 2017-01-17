package io.github.kkysen.quicktrip.apis.google.flights;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.kkysen.quicktrip.app.data.Flight;
import io.github.kkysen.quicktrip.json.Json;

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
public class GoogleFlight implements Flight {
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
		
		//Now to get the ariports
		List<Set<String>> airports = sortedOptions.stream()
				.flatMap(option -> {
					option.getSlice().stream()
						.flatMap(slice -> {
							
						})
					return null;
				})
				
	}
}
