package io.github.kkysen.quicktrip.apis.google;

import io.github.kkysen.quicktrip.apis.JsonRequest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class GoogleApiRequest<R> extends JsonRequest<R> {
    
    private static final List<String> API_KEYS = Arrays.asList(
            "AIzaSyBrc16mMFU7w8Hyo7nFD6ny5SjeOapkY9Q", // kkysen@gmail.com
            "AIzaSyD6B2pl0M_KD1871uxBxkoHztqhTzmLJv0", // ksen1@stuy.edu
            "AIzaSyAsqHJ3GjHqaQ19jZdLsa_owCHfE1kf7uY" // KhyberSenQuickTrip@gmail.com
    );
    
    @Override
    protected final List<String> getApiKeys() {
        return API_KEYS;
    }
    
    // should be overriden
    @Override
    protected Path getRelativeCachePath() {
        return Paths.get("google");
    }
    
}
