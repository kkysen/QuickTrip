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
    protected final R deserializeFromUrl(final String url) throws IOException {
        return parseFromReader(Internet.getJsonInputStreamReader(url));
    }
    
}
