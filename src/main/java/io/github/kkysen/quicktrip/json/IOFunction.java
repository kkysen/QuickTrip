package io.github.kkysen.quicktrip.json;

import java.io.IOException;

/**
 * 
 * 
 * @author Khyber Sen
 */
@FunctionalInterface
public interface IOFunction<I, O> {
    
    public O apply(I input) throws IOException;
    
}
