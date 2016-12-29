package io.github.kkysen.quicktrip.apis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of JSON POJO representing the API request response
 */
public abstract class JsonApiRequest<R> extends ApiRequest<R> {
    
    private static final Gson plainGson = new Gson();
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    
    @Override
    protected final String getFileExtension() {
        return "json";
    }
    
    // FIXME put in Internet class
    
    @Override
    protected BufferedReader getHttpRequestReader() throws IOException {
        final HttpURLConnection urlCon = (HttpURLConnection) new URL(url).openConnection();
        urlCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlCon.setRequestProperty("Accept", "application/json");
        urlCon.connect();
        return new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
    }
    
    @Override
    protected R parseRequest(final Reader reader) {
        return plainGson.fromJson(reader, pojoClass);
    }
    
    @Override
    protected String prettify(final R request) {
        return prettyGson.toJson(request, pojoClass);
    }
    
}
