package io.github.kkysen.quicktrip.apis.google.geocoding;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsRequest;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.TypeAdapter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class GoogleGeocodingRequest extends GoogleMapsRequest<Geolocation> {
    
    private final @QueryField String address;
    private final @QueryField LatLng latlng;
    
    public GoogleGeocodingRequest(final String address) {
        this.address = address;
        latlng = null;
    }
    
    public GoogleGeocodingRequest(final LatLng latLng) {
        latlng = latLng;
        address = null;
    }
    
    @Override
    protected String getMapsRequestType() {
        return "geocode";
    }
    
    @Override
    protected Class<? extends Geolocation> getResponseClass() {
        return Geolocation.class;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("geocoding");
    }
    
    @Override
    protected Duration getRefreshDuration() {
        return Duration.ofDays(365);
    }
    
    @Override
    protected <T> void addClassAdapters(final List<Pair<Class<?>, TypeAdapter<?>>> adapters) {
        super.addClassAdapters(adapters);
        adapters.add(Pair.of(Geolocation.class, new GeolocationAdapter()));
    }
    
    public static void main(final String[] args) throws ApiRequestException {
        System.out.println(
                new GoogleGeocodingRequest("296 6th St, Brooklyn, NY 11215").getResponse());
        System.out.println(Instant.now());
    }
    
}
