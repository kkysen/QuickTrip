package io.github.kkysen.quicktrip.apis.google;

import io.github.kkysen.quicktrip.apis.JsonRequest;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class GoogleApiRequest<R> extends JsonRequest<R> {
    
    private static final String API_KEY = "AIzaSyBrc16mMFU7w8Hyo7nFD6ny5SjeOapkY9Q";
    
    @Override
    protected final String getApiKey() {
        return API_KEY;
    }
    
    // should be overriden
    @Override
    protected Path getRelativePath() {
        return Paths.get("google");
    }
    
}
