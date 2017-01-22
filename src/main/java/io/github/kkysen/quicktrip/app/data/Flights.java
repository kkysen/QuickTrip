package io.github.kkysen.quicktrip.app.data;

import io.github.kkysen.quicktrip.optimization.simulatedAnnealing.AnnealingState;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Flights implements AnnealingState {
    
    private final List<List<Flight>> possibleFlights;
    
    private final List<Flight> flights;
    
    public Flights(final List<List<Flight>> possibleFlights) {
        this.possibleFlights = possibleFlights;
        
        // temp for now w/o annealing
        flights = new ArrayList<>();
        for (final List<Flight> flightPool : possibleFlights) {
            if (flightPool.size() == 0) {
                throw new NullPointerException();
            }
            flightPool.sort(
                    (flight1, flight2) -> flight1.getDuration().compareTo(flight2.getDuration()));
            flights.add(flightPool.get(0));
        }
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
