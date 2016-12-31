package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.ApiRequest;
import io.github.kkysen.quicktrip.apis.hotels.HotelsHotelsScrapeRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

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
    
    private @Setter Hotel hotel;
    
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
    
    public void addHotelsHotelsScrapeRequest() {
        addHotelsRequest(new HotelsHotelsScrapeRequest(this));
    }
    
    public static void main(final String[] args) {
        final long start = System.currentTimeMillis();
        final Destination dest = new Destination("Brooklyn, NY", 5, LocalDate.parse("2017-01-03"), LocalDate.parse("2017-01-05"), 2);
        //dest.possibleHotels().forEach(System.out::println);
        dest.addHotelsHotelsScrapeRequest();
        final List<Hotel> hotels = dest.getPossibleHotels();
        System.out.println(hotels.get(0));
        for (final Hotel hotel : dest.getPossibleHotels()) {
            System.out.println(hotel);
        }
        final long elapsed = System.currentTimeMillis() - start;
        System.out.println(elapsed / 1000.0);
    }
    
}