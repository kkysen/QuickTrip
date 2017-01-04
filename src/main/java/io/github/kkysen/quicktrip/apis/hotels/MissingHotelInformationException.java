package io.github.kkysen.quicktrip.apis.hotels;


/**
 * 
 * 
 * @author Khyber Sen
 */
public class MissingHotelInformationException extends Exception {
    
    public MissingHotelInformationException(final String missingInfo) {
        super("missing " + missingInfo);
    }
    
    public MissingHotelInformationException() {
        this("unknown");
    }
    
    public MissingHotelInformationException(final String missingInfo, final Throwable cause) {
        super("missing " + missingInfo, cause);
    }
    
    public MissingHotelInformationException(final Throwable cause) {
        this("unknown", cause);
    }
    
    
}
