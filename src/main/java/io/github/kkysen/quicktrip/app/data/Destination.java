package io.github.kkysen.quicktrip.app.data;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.CachedApiRequest;
import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.apis.hotels.scrape.HotelsHotelsScrapeRequest;
import io.github.kkysen.quicktrip.apis.hotels.secret.HotelsHotelsSecretRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Destination {
    
    private static final int numPeoplePerRoom = 2;
    
    private final Geolocation location;
    private final String address;
    private final int numDays;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int numPeople;
    private final int numRooms;
    
    private final List<Hotel> possibleHotels = new ArrayList<>();
    
    private @Setter Hotel hotel;
    
    public Destination(final Geolocation location, final int numDays, final LocalDate startDate,
            final LocalDate endDate, final int numPeople) {
        this.location = location;
        address = location.getAddress();
        this.numDays = numDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numPeople = numPeople;
        numRooms = (int) Math.ceil((double) numPeople / numPeoplePerRoom);
    }
    
//    public Destination(final NoDateDestination noDateDest, final LocalDate startDate,
//            final LocalDate endDate, final int numPeople) {
//        this(noDateDest.getAddress(), noDateDest.getNumDays(), startDate, endDate, numPeople);
//    }
    
    private void addHotelsRequest(final CachedApiRequest<List<Hotel>> request) {
        try {
            possibleHotels.addAll(request.getResponse());
        } catch (final ApiRequestException e) {
            // don't add any hotels
        }
    }
    
    public void addHotelsHotelsScrapeRequest() {
        addHotelsRequest(new HotelsHotelsScrapeRequest(this));
    }
    
    public void addHotelHotelsSecretRequest() {
        addHotelsRequest(new HotelsHotelsSecretRequest(this));
    }
    
//    public static void main(final String[] args) {
//        final long start = System.currentTimeMillis();
//        final Destination dest = new Destination("Brooklyn, NY", 5, LocalDate.parse("2017-01-05"),
//                LocalDate.parse("2017-01-08"), 2);
//        //dest.possibleHotels().forEach(System.out::println);
//        //dest.addHotelsHotelsScrapeRequest();
//        dest.addHotelHotelsSecretRequest();
//        final List<Hotel> hotels = dest.getPossibleHotels();
//        System.out.println(hotels.get(0));
//        for (final Hotel hotel : dest.getPossibleHotels()) {
//            System.out.println(hotel);
//        }
//        final long elapsed = System.currentTimeMillis() - start;
//        System.out.println(elapsed / 1000.0);
//    }
    
    @Override
    public String toString() {
        return "Destination [address=" + address + ", numDays=" + numDays + ", startDate="
                + startDate + ", endDate=" + endDate + ", numPeople=" + numPeople + ", numRooms="
                + numRooms + ", possibleHotels=" + possibleHotels + ", hotel=" + hotel + "]";
    }
    
}