package io.github.kkysen.quicktrip.apis.google.flights;

import java.time.Duration;

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
}
