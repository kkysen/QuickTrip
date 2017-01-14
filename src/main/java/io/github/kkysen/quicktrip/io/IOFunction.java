package io.github.kkysen.quicktrip.io;

import java.io.IOException;

/**
 * 
 * 
 * @author Khyber Sen
 */
@FunctionalInterface
public interface IOFunction<I, O> {
    
    public O apply(I input) throws IOException;
    
    public default O applySafely(final I input) {
        try {
            return apply(input);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
