package io.github.kkysen.quicktrip.apis.google;

import io.github.kkysen.quicktrip.apis.JsonRequest;
import io.github.kkysen.quicktrip.apis.KeyManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class GoogleApiRequest<R> extends JsonRequest<R> {
    
    private static final KeyManager API_KEYS = new KeyManager(
            "AIzaSyD6B2pl0M_KD1871uxBxkoHztqhTzmLJv0", // ksen1@stuy.edu
            //"AIzaSyAsqHJ3GjHqaQ19jZdLsa_owCHfE1kf7uY", // KhyberSenQuickTrip@gmail.com
            //"AIzaSyBrc16mMFU7w8Hyo7nFD6ny5SjeOapkY9Q", // kkysen@gmail.com
            "AIzaSyBNfL0S156AGynDg6cAA8w-svLbhQTZIwM" // yuangenghis@gmail.com
    );
    
    @Override
    protected final List<String> getApiKeys() {
        return null;
    }
    
    @Override
    protected final KeyManager getKeyManager() {
        return API_KEYS;
    }
    
    // should be overriden
    @Override
    protected Path getRelativeCachePath() {
        return Paths.get("google");
    }
    
}
