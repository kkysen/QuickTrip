package io.github.kkysen.quicktrip.apis;

import java.io.IOException;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ApiRequestException extends IOException {
    
    private static final long serialVersionUID = -8529548460757699985L;
    
    private final @Getter IOException cause;
    private final CachedApiRequest<?> request;
    private final Class<? extends CachedApiRequest<?>> klass;
    private final @Getter String url;
    
    @SuppressWarnings("unchecked")
    public <R extends CachedApiRequest<?>> ApiRequestException(final R request,
            final IOException cause) {
        super(request.getUrl(), cause);
        this.cause = cause;
        this.request = request;
        klass = (Class<R>) request.getClass();
        url = request.getUrl();
    }
    
    @SuppressWarnings("unchecked")
    public <R extends CachedApiRequest<?>> R getRequest() {
        return (R) klass.cast(request);
    }
    
}
