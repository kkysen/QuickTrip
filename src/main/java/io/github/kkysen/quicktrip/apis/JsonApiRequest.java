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
    protected String getFileExtension() {
        return "json";
    }
    
    // FIXME put in Internet class
//    @Override
//    protected BufferedReader getHttpRequestReader(final String url) {
//        final HttpURLConnection urlCon = (HttpURLConnection) new URL(url).openConnection();
//        urlCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//        urlCon.setRequestProperty("Accept", "application/json");
//        // FIXME unfinished
//    }
    
    @Override
    protected R parseRequest(final Reader reader) {
        return gson.fromJson(reader, pojoClass);
    }
    
    @Override
    protected String prettify(final R request) {
        return gson.toJson(request, pojoClass);
    }
    
}
