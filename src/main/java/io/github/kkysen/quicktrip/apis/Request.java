package io.github.kkysen.quicktrip.apis;

import java.io.IOException;

/**
 * 
 * 
 * @author Khyber Sen
 */
@FunctionalInterface
public interface Request<R> {
    
    public R getResponse() throws IOException;
    
}
