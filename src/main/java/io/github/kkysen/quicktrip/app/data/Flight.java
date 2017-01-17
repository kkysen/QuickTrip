package io.github.kkysen.quicktrip.app.data;

import io.github.kkysen.quicktrip.data.airports.Airport;

import java.time.Duration;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface Flight {
    
    public Duration getDuration();
    
    public Airport getStartAirport();
    
    public Airport getEndAirport();
    
}
