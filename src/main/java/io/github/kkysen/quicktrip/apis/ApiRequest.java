package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.util.annotations.AnnotationUtils;
import io.github.kkysen.quicktrip.util.reflect.Reflect;
import io.github.kkysen.quicktrip.web.Internet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of POJO representing the API request response, presumably a
 *            JSON one
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
    
    private static class QueryEncoder extends HashMap<String, String> {
        
        private static final long serialVersionUID = 3055592436293901045L;
        
        public QueryEncoder() {}
        
        @Override
        public String put(final String name, final String value) {
            String oldValue;
            try {
                oldValue = URLEncoder.encode(value, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            return oldValue;
        }
        
        @Override
        public String toString() {
            final StringJoiner queryString = new StringJoiner("&", "?", "");
            for (final String name : keySet()) {
                queryString.add(name + '=' + get(name));
            }
            return queryString.toString();
        }
        
    }
    
    private final String apiKey;
    private final String baseUrl;
    
    private final Map<String, String> query = new QueryEncoder();
    
    private final String url;
    
    private String id;
    
    protected final Class<? extends R> pojoClass;
    private R request;
    
    protected String getApiKeyQueryName() {
        return "key";
    }
    
    protected abstract String getApiKey();
    
    protected abstract String getBaseUrl();
    
    /**
     * uses reflection to find all the @QueryFields and adds them to the query
     * (QueryEncoder)
     */
    private void reflectQuery() {
        final List<Field> queryFields = Reflect.getInstanceVars(this);
        // filter out fields that are not QueryFields or have encode = false
        queryFields.removeIf(field -> {
            final QueryField queryField = AnnotationUtils.getAnnotation(field, QueryField.class);
            return queryField == null || !queryField.encode();
        });
        final Map<Field, Object> queryEntries = Reflect.getFieldEntries(queryFields, this);
        for (final Field queryField : queryEntries.keySet()) {
            final String queryValue = queryEntries.get(queryField).toString();
            query.put(queryField.getName(), queryValue);
        }
    }
    
    /**
     * allows a subclass to modify the final query before it's assembled into
     * the final url
     * without overriding, this method does nothing
     * 
     * @param query the existing query with the reflected query fields and api
     *            key added
     */
    protected void modifyQuery(final Map<String, String> query) {}
    
    protected abstract Class<? extends R> getPojoClass();
    
    private void addApiKey() {
        if (!apiKey.isEmpty()) {
            query.put(getApiKeyQueryName(), apiKey);
        }
    }
    
    protected ApiRequest() {
        apiKey = getApiKey();
        baseUrl = getBaseUrl();
        reflectQuery();
        addApiKey();
        modifyQuery(query);
        url = baseUrl + query.toString();
        pojoClass = getPojoClass();
    }
    
    protected abstract R parseRequest(Reader reader);
    
    private Reader getHttpRequestReader() throws IOException {
        return Internet.getInputStreamReader(url);
    }
    
    private Reader getCachedRequestReader() throws IOException {
        return new InputStreamReader(Files.newInputStream(Paths.get(requestCache.get(id))));
    }
    
    private boolean isCached() {
        return requestCache.containsKey(id);
    }
    
    public R get() throws IOException {
        if (request == null) {
            Reader requestReader;
            if (isCached()) {
                requestReader = getCachedRequestReader();
            } else {
                requestReader = getHttpRequestReader();
            }
            request = parseRequest(requestReader);
        }
        return request;
    }
    
}
