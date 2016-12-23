package io.github.kkysen.quicktrip.apis.google.maps.directions;

import io.github.kkysen.quicktrip.apis.Json;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum WaypointType {
    
    LOCALITY("locality"),
    POLITICAL("political"),
    ;
    
    private final String name;
    
}
