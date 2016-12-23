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
    
    private static final Gson gson = new Gson(); // FIXME
    
    @Override
    protected R parseRequest(final Reader reader) {
        return gson.fromJson(reader, pojoClass);
    }
    
}
