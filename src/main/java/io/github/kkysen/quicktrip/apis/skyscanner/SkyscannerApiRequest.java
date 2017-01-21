package io.github.kkysen.quicktrip.apis.skyscanner;

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
public abstract class SkyscannerApiRequest<R> extends JsonRequest<R> {
    
    // FIXME not sure if API key is different for flights, hotels, etc. or just one key for everything
    //private static final String API_KEY = "st671638446256855850209861703997";
    
    //The one they use for testing, might work?
    private static final String API_KEY = "prtl6749387986743898559646983194";
    
    protected static final String BASE_URL = "http://partners.api.skyscanner.net/apiservices/";
    
    // should be overriden
    @Override
    protected Path getRelativeCachePath() {
        return Paths.get("skyscanner");
    }
    
    // FIXME not sure if API key is different for flights, hotels, etc. or just one key for everything
    @Override
    protected List<String> getApiKeys() {
        return Arrays.asList(API_KEY);
    }
    
    @Override
    protected String getApiKeyQueryName() {
        return "apiKey";
    }
    
    @Override
    protected final String getBaseUrl() {
        return BASE_URL + String.join("/", getUrlPathParts());
    }
    
    protected abstract List<String> getUrlPathParts();
    
}
