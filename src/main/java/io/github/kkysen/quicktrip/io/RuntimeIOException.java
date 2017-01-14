package io.github.kkysen.quicktrip.io;

import java.io.IOException;

import lombok.Getter;

/**
 * wraps an IOException into a RuntimeException
 * 
 * @author Khyber Sen
 */
public class RuntimeIOException extends RuntimeException {
    
    private static final long serialVersionUID = -7522220261809796580L;
    
    private final @Getter IOException cause;
    
    public RuntimeIOException(final IOException cause) {
        super(cause);
        this.cause = cause;
    }
    
}