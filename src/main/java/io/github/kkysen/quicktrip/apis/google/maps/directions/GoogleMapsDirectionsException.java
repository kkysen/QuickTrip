package io.github.kkysen.quicktrip.apis.google.maps.directions;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class GoogleMapsDirectionsException extends RuntimeException {
    
    private static final long serialVersionUID = -6212081394000806303L;
    
    public GoogleMapsDirectionsException() {
        super();
    }
    
    /**
     * @param url url of attempted http request
     */
    public GoogleMapsDirectionsException(final String url) {
        super(url);
    }
    
}
