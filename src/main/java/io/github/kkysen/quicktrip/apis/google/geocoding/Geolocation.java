package io.github.kkysen.quicktrip.apis.google.geocoding;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.google.GoogleApiResponse;
import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.json.Json;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
//@RequiredArgsConstructor
@Getter
public class Geolocation extends GoogleApiResponse {
    
    private final LatLng location;
    private final String country;
    private final String placeId;
    
    public Geolocation(final String status, final LatLng location, final String country, final String placeId) {
        super(status);
        this.location = location;
        this.country = country;
        this.placeId = placeId;
    }
    
    public boolean exists() {
        return isOk();
    }
    
    public static boolean exists(final String address) throws ApiRequestException {
        if (address == null || address.isEmpty()) {
            return false;
        }
        return new GoogleGeocodingRequest(address).getResponse().exists();
    }
    
    public static Geolocation createDummy(final LatLng location, final String country) {
        return new Geolocation("OK", location, country, "");
    }
    
}
