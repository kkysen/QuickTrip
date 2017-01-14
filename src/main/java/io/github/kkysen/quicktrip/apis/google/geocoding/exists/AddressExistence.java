package io.github.kkysen.quicktrip.apis.google.geocoding.exists;

import io.github.kkysen.quicktrip.apis.google.GoogleApiResponse;
import io.github.kkysen.quicktrip.json.Json;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
//@RequiredArgsConstructor
@Getter
public class AddressExistence extends GoogleApiResponse {
    
    /**
     * not really used since Gson uses reflection
     * 
     * @param status status
     */
    public AddressExistence(final String status) {
        super(status);
    }
    
    public boolean exists() {
        return !status.equals("ZERO_RESULTS");
    }
    
}
