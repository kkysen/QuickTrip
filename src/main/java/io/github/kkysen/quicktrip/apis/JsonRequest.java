package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.web.Internet;

import java.io.IOException;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of JSON POJO representing the API request response
 */
public abstract class JsonRequest<R> extends AbstractJsonRequest<R> {
    
    @Override
    protected R deserializeFromUrl(final String url) throws IOException {
        return deserializeFromReader(Internet.getJsonInputStreamReader(url));
    }
    
}
