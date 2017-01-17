package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.app.data.Flight;
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
public class GoogleFlights {
    
    private Trip trips;
    
    public List<Flight> getFlights() {
        
    }
    
}
