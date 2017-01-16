package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.apis.google.LatLng;
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
public class Step {
    private String travel_mode;
    private LatLng start_location;
    private LatLng end_location;
    private String polyline;
    private Duration duration;
    private String html_instructions;
    private Distance distance;
}
