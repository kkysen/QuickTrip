package io.github.kkysen.quicktrip.apis.hotels;

import io.github.kkysen.quicktrip.json.MissingInformationException;

import java.util.Collection;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MissingHotelInformationException extends MissingInformationException {
    
    private static final long serialVersionUID = -6961085226898461370L;
    
    public MissingHotelInformationException() {
        super();
    }
    
    public MissingHotelInformationException(final Collection<String> missingInfo) {
        super(missingInfo);
    }
    
    public MissingHotelInformationException(final String... missingInfo) {
        super(missingInfo);
    }
    
    public MissingHotelInformationException(final Throwable cause,
            final Collection<String> missingInfo) {
        super(cause, missingInfo);
    }
    
    public MissingHotelInformationException(final Throwable cause, final String... missingInfo) {
        super(cause, missingInfo);
    }
    
    public MissingHotelInformationException(final Throwable cause) {
        super(cause);
    }
    
}
