package io.github.kkysen.quicktrip.apis.hotels;

import io.github.kkysen.quicktrip.apis.CachedApiRequest.QueryParams;
import io.github.kkysen.quicktrip.app.data.Destination;

import java.time.LocalDate;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Getter
public class HotelsHotelsQuery {
    
    private final String destination;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int numPeople;
    private final int numRooms;
    
    private final QueryParams query = new QueryParams();
    
    private void addQuery(final String name, final String value) {
        query.put("q-" + name, value);
    }
    
    private void addRooms() {
        addQuery("rooms", String.valueOf(numRooms));
        for (int roomNum = 0; roomNum < numRooms; roomNum++) {
            final String room = "room-" + roomNum + "-";
            addQuery(room + "adults", "2");
            addQuery(room + "children", "0"); // not dealing w/ children yet
        }
        // if odd numPeople, last room should have only 1 adult
        if ((numPeople & 1) == 1) {
            addQuery("room-" + (numRooms - 1) + "-adults", "1");
        }
    }
    
    private void finalizeQuery() {
        addQuery("destination", destination);
        addQuery("check-in", startDate.toString());
        addQuery("check-out", endDate.toString());
        addRooms();
        query.put("sort-order", "DISTANCE_FROM_LANDMARK");
    }
    
    private HotelsHotelsQuery(final String destination, final LocalDate startDate,
            final LocalDate endDate, final int numPeople, final int numRooms) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numPeople = numPeople;
        this.numRooms = numRooms;
        finalizeQuery();
    }
    
    public HotelsHotelsQuery(final Destination dest) {
        this(
                dest.getAddress(),
                dest.getStartDate(),
                dest.getEndDate(),
                dest.getNumPeople(),
                dest.getNumRooms());
    }
    
}
