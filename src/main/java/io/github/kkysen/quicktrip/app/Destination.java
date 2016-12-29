package io.github.kkysen.quicktrip.app;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;

@Getter
public class Destination {
    
    private static final int numPeoplePerRoom = 2;
    
    private final String address;
    private final int numDays;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int numPeople;
    private final int numRooms;
    
    public Destination(final NoDateDestination noDateDest, final LocalDate startDate,
            final LocalDate endDate, final int numPeople) {
        address = noDateDest.getAddress();
        numDays = noDateDest.getNumDays();
        this.startDate = startDate;
        this.endDate = endDate;
        this.numPeople = numPeople;
        numRooms = (int) Math.ceil((double) numPeople / numPeoplePerRoom);
    }
    
    public List<Hotel> possibleHotels() {
        return null; // FIXME
    }
    
}