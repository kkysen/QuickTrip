package io.github.kkysen.quicktrip.apis.google.geocoding.exists;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsRequest;

import java.io.IOException;
import java.nio.file.Path;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class AddressExistsRequest extends GoogleMapsRequest<AddressExistence> {
    
    private final @QueryField String address;
    private final @QueryField String fields = "status";
    
    @Override
    protected String getMapsRequestType() {
        return "geocode";
    }
    
    @Override
    protected Class<? extends AddressExistence> getResponseClass() {
        return AddressExistence.class;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("geocoding").resolve("exists");
    }
    
    public static boolean exists(final String address) throws IOException {
        if (address.isEmpty()) {
            return false;
        }
        return new AddressExistsRequest(address).getResponse().exists();
    }
    
}
