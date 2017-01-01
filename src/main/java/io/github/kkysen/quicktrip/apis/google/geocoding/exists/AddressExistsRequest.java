package io.github.kkysen.quicktrip.apis.google.geocoding.exists;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.geocoding.exists.response.AddressExistsOnly;
import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsApiRequest;

import java.io.IOException;
import java.nio.file.Path;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class AddressExistsRequest extends GoogleMapsApiRequest<AddressExistsOnly> {
    
    private final @QueryField String address;
    
    @Override
    protected String getRequestType() {
        return "geocode";
    }
    
    @Override
    protected Class<? extends AddressExistsOnly> getPojoClass() {
        return AddressExistsOnly.class;
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
