package io.github.kkysen.quicktrip.apis.google.geocoding;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsRequest;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
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
    
    public static List<Geolocation> cache(final Path placesFile, int limit) throws IOException {
        final List<String> places = MyFiles.readLines(placesFile);
        limit = limit > places.size() ? places.size() : limit;
        final GoogleGeocodingRequest request = new GoogleGeocodingRequest("");
        final List<Geolocation> locations = new ArrayList<>(limit);
        for (final String place : places.subList(0, limit)) {
            System.out.print(place + ": ");
            request.address = place;
            request.clearResponse();
            locations.add(request.getResponse());
        }
        return locations;
    }
    
    public static void main(final String[] args) throws IOException {
        System.out.println("caching major cities");
        cache(Paths.get("C:/Users/kkyse/Downloads/Top5000Population.csv"), 5000);
    }
    
}
