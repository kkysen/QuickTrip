package io.github.kkysen.quicktrip.json;

import java.util.Collection;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MissingInformationException extends Exception {
    
    public MissingInformationException() {}
    
    public MissingInformationException(final Throwable cause, final String... missingInfo) {
        super("missing: " + String.join(", ", missingInfo), cause);
    }
    
    public MissingInformationException(final Throwable cause, final Collection<String> missingInfo) {
        super("missing: " + String.join(", ", missingInfo), cause);
    }
    
    public MissingInformationException(final String... missingInfo) {
        super("missing: " + String.join(", ", missingInfo));
    }
    
    public MissingInformationException(final Collection<String> missingInfo) {
        super("missing: " + String.join(", ", missingInfo));
    }
    
    public MissingInformationException(final Throwable cause) {
        super(cause);
    }
    
}
