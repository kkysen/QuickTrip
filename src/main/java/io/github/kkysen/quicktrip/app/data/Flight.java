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
    
    public int getPrice();
    
    public Destination getOrigin();
    
    public void setOrigin(Destination origin);
    
    public Destination getDestination();
    
    public void setDestination(Destination destination);
    
    public default void setDestinationLocations() {
        getOrigin().setLocation(getStartAirport().getGeolocation());
        getDestination().setLocation(getEndAirport().getGeolocation());
        System.out.println("origin airport: " + getOrigin().getAddress());
        System.out.println("destiination airport: " + getDestination().getAddress());
    }
    
}
