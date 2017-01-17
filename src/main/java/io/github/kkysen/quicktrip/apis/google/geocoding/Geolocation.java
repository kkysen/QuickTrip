package io.github.kkysen.quicktrip.apis.google.geocoding;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.google.GoogleApiResponse;
import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.json.Json;

import org.apache.commons.math3.ml.clustering.Clusterable;

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
public class Geolocation extends GoogleApiResponse implements Clusterable {
    
    protected LatLng latLng;
    
    @SerializedName("formatted_address")
    protected String address;
    
    private String country;
    
    @SerializedName("place_id")
    private final String placeId;
    
    public Geolocation(final String status, final LatLng location, final String address,
            final String placeId) {
        super(status);
        this.latLng = location;
        this.address = address;
        this.placeId = placeId;
        postDeserialize();
    }
    
    @Override
    public void postDeserialize() {
        super.postDeserialize();
        if (address != null) {
            final String[] addressParts = address.split(", ");
            country = addressParts[addressParts.length - 1];
        }
        
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
    
    @Override
    public double[] getPoint() {
        return latLng.getCoords();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (placeId == null ? 0 : placeId.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Geolocation other = (Geolocation) obj;
        if (placeId == null) {
            if (other.placeId != null) {
                return false;
            }
        } else if (!placeId.equals(other.placeId)) {
            return false;
        }
        return true;
    }
    
}
