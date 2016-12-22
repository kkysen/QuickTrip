package io.github.kkysen.quicktrip.apis;

import java.io.Reader;

import com.google.gson.Gson;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of JSON POJO representing the API request response
 */
public abstract class JsonApiRequest<R> extends ApiRequest<R> {
    
    private static final Gson gson = new Gson();
    
    @Override
    protected R parseRequest(final Reader reader, final Class<? extends R> pojo) {
        return gson.fromJson(reader, pojo);
    }
    
}
