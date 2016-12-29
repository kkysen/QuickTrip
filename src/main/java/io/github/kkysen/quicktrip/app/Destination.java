package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.ApiRequest;
import io.github.kkysen.quicktrip.apis.hotels.HotelsScrapeRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    
    private final List<Hotel> possibleHotels = new ArrayList<>();
    
    private Destination(final String address, final int numDays, final LocalDate startDate,
            final LocalDate endDate, final int numPeople) {
        this.address = address;
        this.numDays = numDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numPeople = numPeople;
        numRooms = (int) Math.ceil((double) numPeople / numPeoplePerRoom);
    }
    
    public Destination(final NoDateDestination noDateDest, final LocalDate startDate,
            final LocalDate endDate, final int numPeople) {
        this(noDateDest.getAddress(), noDateDest.getNumDays(), startDate, endDate, numPeople);
    }
    
    private void addHotelsRequest(final ApiRequest<List<Hotel>> request) {
        try {
            possibleHotels.addAll(request.getReponse());
        } catch (final IOException e) {
            // don't add any hotels
        }
    }
    
    public List<Hotel> possibleHotels() {
        addHotelsRequest(new HotelsScrapeRequest(this));
        return possibleHotels;
    }
    
}