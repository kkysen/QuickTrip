package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.json.TypeReaderAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 
 * @author Khyber Sen
 */
public class GoogleFlightsAdapter extends TypeReaderAdapter<List<GoogleFlight>> {

    List<GoogleFlight> flights = new ArrayList<>();
    
    @Override
    protected void addPropertyReaders() {
        
    }

    
    @Override
    public void read() throws IOException {
        
    }

    
    @Override
    public List<GoogleFlight> get() {
        return flights;
    }
    
}
