package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.json.Json;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
@Getter
public class Waypoint extends Geolocation {

    public Waypoint(final String status, final LatLng location, final String address, final String placeId) {
        super(status, location, address, placeId);
    }
    
    public void setAddressLocationFromLeg(final LatLng latLng, final String address) {
        this.latLng = latLng;
        this.address = address;
        postDeserialize();
    }
    
}
