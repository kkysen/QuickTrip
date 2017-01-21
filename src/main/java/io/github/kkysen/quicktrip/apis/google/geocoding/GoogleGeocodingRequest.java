package io.github.kkysen.quicktrip.apis.google.geocoding;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsRequest;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.TypeAdapter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class GoogleGeocodingRequest extends GoogleMapsRequest<Geolocation> {
    
    private @QueryField String address;
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
    
    public static void main(final String[] args) throws IOException {
        System.out.println("caching major cities");
        final List<String> cities = MyFiles
                .readLines(Paths.get("C:/Users/kkyse/Downloads/Top5000Population.csv")).subList(0, 2000);
        System.out.println(cities.size());
        final GoogleGeocodingRequest request = new GoogleGeocodingRequest("");
        for (final String city : cities) {
            System.out.print(city + ": ");
            request.address = city;
            request.clearResponse();
            request.getResponse();
        }
    }
    
}
