package io.github.kkysen.quicktrip.app;

import java.time.LocalDate;
import java.util.List;

public class Destination {
    
    private final String address;
    private final int numDays;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int numPeople;
    
    public Destination(final NoDateDestination noDateDest, final LocalDate startDate,
            final LocalDate endDate, final int numPeople) {
        address = noDateDest.getAddress();
        numDays = noDateDest.getNumDays();
        this.startDate = startDate;
        this.endDate = endDate;
        this.numPeople = numPeople;
    }
    
    public List<Hotel> possibleHotels() {
        return null; // FIXME
    }
    
}