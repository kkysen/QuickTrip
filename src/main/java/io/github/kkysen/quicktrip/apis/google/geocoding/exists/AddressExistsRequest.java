package io.github.kkysen.quicktrip.apis.google.geocoding.exists;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.geocoding.GoogleGeocodingRequest;

import java.nio.file.Path;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Deprecated
public class AddressExistsRequest extends GoogleGeocodingRequest {
    
    private final @QueryField String fields = "status";
    
    public AddressExistsRequest(final String address) {
        super(address);
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("exists");
    }
    
    public static boolean exists(final String address) throws ApiRequestException {
        if (address.isEmpty()) {
            return false;
        }
        return new AddressExistsRequest(address).getResponse().exists();
    }
    
}
