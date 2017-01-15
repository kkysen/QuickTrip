package io.github.kkysen.quicktrip.apis.google.geocoding;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.google.GoogleApiResponse;
import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.json.Json;

import com.google.gson.annotations.SerializedName;

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
    
    protected LatLng location;
    
    @SerializedName("formatted_address")
    protected String address;
    
    private String country;
    
    @SerializedName("place_id")
    private final String placeId;
    
    public Geolocation(final String status, final LatLng location, final String address,
            final String placeId) {
        super(status);
        this.location = location;
        this.address = address;
        this.placeId = placeId;
        postDeserialize();
    }
    
    @Override
    public void postDeserialize() {
        super.postDeserialize();
        final String[] addressParts = address.split(", ");
        country = addressParts[addressParts.length - 1];
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
