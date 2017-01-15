package io.github.kkysen.quicktrip.apis.google.geocoding;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsRequest;

import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.TypeAdapter;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class GoogleGeocodingRequest extends GoogleMapsRequest<Geolocation> {
    
    private final @QueryField String address;
    
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
    protected <T> void addClassAdapters(final List<Pair<Class<?>, TypeAdapter<?>>> adapters) {
        super.addClassAdapters(adapters);
        adapters.add(Pair.of(Geolocation.class, new GeolocationAdapter()));
    }
    
    public static void main(final String[] args) throws ApiRequestException {
        System.out.println(new GoogleGeocodingRequest("296 6th St, Brooklyn, NY 11215").getResponse());
    }
    
}
