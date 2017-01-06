package io.github.kkysen.quicktrip.json;

import java.util.Collection;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MissingInformationException extends Exception {
    
    private static final long serialVersionUID = 1002695619260309449L;
    
    public MissingInformationException(final Throwable cause, final String... missingInfo) {
        super("missing: " + String.join(", ", missingInfo), cause);
    }
    
    public MissingInformationException(final Throwable cause,
            final Collection<String> missingInfo) {
        super("missing: " + String.join(", ", missingInfo), cause);
    }
    
    public MissingInformationException(final String... missingInfo) {
        super("missing: " + String.join(", ", missingInfo));
    }
    
    public MissingInformationException(final Collection<String> missingInfo) {
        super("missing: " + String.join(", ", missingInfo));
    }
    
    public MissingInformationException(final Throwable cause) {
        this(cause, "unknown");
    }
    
    public MissingInformationException() {
        this("unknown");
    }
    
}
