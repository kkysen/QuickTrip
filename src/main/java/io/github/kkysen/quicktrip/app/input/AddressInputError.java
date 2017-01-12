package io.github.kkysen.quicktrip.app.input;

import io.github.kkysen.quicktrip.apis.google.geocoding.exists.AddressExistsRequest;

import java.io.IOException;

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
    
    public static boolean validate(final String address) throws AddressInputError, EmptyInputError {
        if (address.isEmpty()) {
            throw none();
        }
        try {
            if (!AddressExistsRequest.exists(address)) {
                throw nonexistent(address);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    
}