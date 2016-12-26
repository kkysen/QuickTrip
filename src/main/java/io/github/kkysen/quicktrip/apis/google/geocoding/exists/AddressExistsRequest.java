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
    protected Path getRelativePath() {
        return super.getRelativePath().resolve("geocoding").resolve("exists");
    }
    
    public static boolean exists(final String address) throws IOException {
        return new AddressExistsRequest(address).get().exists();
    }
    
}