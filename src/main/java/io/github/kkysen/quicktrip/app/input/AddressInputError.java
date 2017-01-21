package io.github.kkysen.quicktrip.app.input;

import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.apis.google.geocoding.GoogleGeocodingRequest;

public class AddressInputError extends InputError {
    
    private static final long serialVersionUID = -5302828164507728071L;
    
    private static String quote(final Object o) {
        return '"' + o.toString() + '"';
    }
    
    AddressInputError(final String error, final String msg) {
        super(error, msg);
    }
    
    public static EmptyInputError none() {
        return new EmptyInputError("No Address", "Please enter an address");
    }
    
    public static AddressInputError nonexistent(final String address) {
        return new AddressInputError("Nonexistent Addres", quote(address) + " does not exist.");
    }
    
    public static Geolocation validate(final String address)
            throws AddressInputError, EmptyInputError {
        if (address.isEmpty()) {
            throw none();
        }
        final Geolocation location = new GoogleGeocodingRequest(address).getResponseSafely();
        System.out.println(location);
        if (!location.exists()) {
            throw nonexistent(address);
        }
        return location;
    }
    
}