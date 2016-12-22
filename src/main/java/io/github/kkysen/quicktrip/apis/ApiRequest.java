package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.web.Internet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of POJO representing the API request response, presumably a JSON one
 */
public abstract class ApiRequest<R> {
    
    private static final String CACHE_DIRECTORY = ""; // FIXME
    private static final String REQUEST_CACHE_PATH = CACHE_DIRECTORY + "IDs.txt";
    private static final String CACHE_SEP = ",";  // FIXME
    
    private static Stream<String> getRequestCacheLines() {
        try {
            return Files.lines(Paths.get(REQUEST_CACHE_PATH));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static Map<String, String> getRequestCache() {
        return getRequestCacheLines()
                .map(line -> line.split(CACHE_SEP))
                .collect(Collectors.toConcurrentMap(
                        lineParts -> lineParts[0],
                        lineParts -> lineParts[1]));
    }
    
    private static final Map<String, String> requestCache = getRequestCache();
    
    private final String apiKey;
    private final String baseUrl;
    private final Map<String, String> query;
    private final String url;
    
    private String id;
    
    private R request;
    
    protected String getApiKeyQueryName() {
        return "key";
    }
    
    protected abstract String getApiKey();
    
    protected abstract String getBaseUrl();
    
    protected abstract Map<String, String> getQuery();
    
    protected abstract Class<?> getJsonClass();
    
    private void addApiKey() {
        if (!apiKey.isEmpty()) {
            query.put(getApiKeyQueryName(), apiKey);
        }
    }
    
    private String assembleUrl() {
        final StringJoiner url = new StringJoiner("&", baseUrl + '?', "");
        for (final String name : query.keySet()) {
            url.add(name + '=' + query.get(name));
        }
        return url.toString();
    }
    
    protected ApiRequest() {
        apiKey = getApiKey();
        baseUrl = getBaseUrl();
        query = getQuery();
        addApiKey();
        url = assembleUrl();
    }
    
    protected abstract R parseRequest(Reader reader, Class<? extends R> pojo);
    
    private Reader getHttpRequestReader() throws IOException {
        return Internet.getInputStreamReader(url);
    }
    
    private Reader getCachedRequestReader() throws IOException {
        return new InputStreamReader(Files.newInputStream(Paths.get(requestCache.get(id))));
    }
    
    private boolean isCached() {
        return requestCache.containsKey(id);
    }
    
    public R get(final Class<? extends R> pojo) throws IOException {
        if (request == null) {
            Reader requestReader;
            if (isCached()) {
                requestReader = getCachedRequestReader();
            } else {
                requestReader = getHttpRequestReader();
            }
            request = parseRequest(requestReader, pojo);
        }
        return request;
    }
    
}
