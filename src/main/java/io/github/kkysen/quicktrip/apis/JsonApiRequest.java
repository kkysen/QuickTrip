package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.io.MyFiles;
import io.github.kkysen.quicktrip.web.Internet;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of JSON POJO representing the API request response
 */
public abstract class JsonApiRequest<R> extends ApiRequest<R> {
    
    private static final Gson plainGson = new Gson();
    
    @Override
    protected final String getFileExtension() {
        return "json";
    }
    
    private final R parseFromReader(final Reader reader) {
        return plainGson.fromJson(reader, pojoClass);
    }
    
    @Override
    protected final R parseFromFile(final Path path) throws IOException {
        return parseFromReader(Files.newBufferedReader(path, Constants.CHARSET));
    }
    
    @Override
    protected final R parseFromUrl(final String url) throws IOException {
        return parseFromReader(Internet.getJsonInputStreamReader(url));
    }
    
    @Override
    protected final void cache(final Path path, final R response) throws IOException {
        MyFiles.write(path, plainGson.toJson(response));
    }
    
}
