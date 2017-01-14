package io.github.kkysen.quicktrip.apis;

/**
 * 
 * 
 * @author Khyber Sen
 */
@FunctionalInterface
public interface Request<R> {
    
    public R getResponse() throws ApiRequestException;
    
}
