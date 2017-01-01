package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class AbstractJsonRequest<R> extends ApiRequest<R> {
    
    private static final Gson GSON = new Gson();
    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
    
    @Override
    protected final String getFileExtension() {
        return "json";
    }
    
    protected final R parseFromReader(final Reader reader) {
        if (pojoClass == null) {
            return GSON.fromJson(reader, pojoType);
        }
        return GSON.fromJson(reader, pojoClass);
    }
    
    @Override
    protected final R deserializeFromFile(final Path path) throws IOException {
        return parseFromReader(Files.newBufferedReader(path, Constants.CHARSET));
    }
    
    @Override
    protected final void cache(final Path path, final R response) throws IOException {
        MyFiles.write(path, PRETTY_GSON.toJson(response));
    }
    
}
