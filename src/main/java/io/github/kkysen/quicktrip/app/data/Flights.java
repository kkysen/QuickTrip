package io.github.kkysen.quicktrip.app.data;

import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.optimization.simulatedAnnealing.AnnealingState;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;


/**
 * 
 * 
 * @author Khyber Sen
 */
public class Flights implements AnnealingState {
    
    private List<Flight> flights;
    
    public Flights(final List<Pair<Geolocation, Geolocation>> pairedFlightDestinations) {
        
    }
    
    @Override
    public void perturb() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void undo() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public double energy() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public AnnealingState clone() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public List<Flight> getFlights() {
        return new ArrayList<>(flights);
    }
    
}
