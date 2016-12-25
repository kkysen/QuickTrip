package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.io.MyFiles;
import io.github.kkysen.quicktrip.reflect.Reflect;
import io.github.kkysen.quicktrip.reflect.annotations.AnnotationUtils;
import io.github.kkysen.quicktrip.web.Internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of POJO representing the API request response, presumably a
 *            JSON one
 */
public abstract class ApiRequest<R> {
    
    private static final boolean SHOULD_CACHE_PRETTIED = true;
    
    private static final String CACHE_DIR = "src/main/resources/apiCache/";
    private static final String PRETTY_CACHE_DIR = CACHE_DIR + "prettied/";
    
    public static final class RequestCache {
        
        private static final Path REQUEST_CACHE_PATH = Paths.get(CACHE_DIR, "requestCache.csv");
        
        private static final String SEP = ",";
        
        private static final MessageDigest sha128;
        static {
            try {
                sha128 = MessageDigest.getInstance("SHA-1");
            } catch (final NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        
        private static final Base64.Encoder fileNameEncoder = Base64.getUrlEncoder();
        
        private static String hashToBase64(final String s) {
            sha128.update(s.getBytes(Constants.CHARSET));
            final byte[] hashed = sha128.digest();
            sha128.reset();
            return fileNameEncoder.encodeToString(hashed);
        }
        
        @RequiredArgsConstructor
        @Getter
        private static class Entry {
            
            private final String url;
            private final String id;
            private final Path path;
            
            @Override
            public String toString() {
                return url + "," + id + "," + path;
            }
            
        }
        
        private final Map<String, String> url2id = new ConcurrentHashMap<>();
        private final Map<String, Path> id2path = new ConcurrentHashMap<>();
        
        private final Map<Path, String> path2id = new ConcurrentHashMap<>();
        private final Map<String, String> id2url = new ConcurrentHashMap<>();
        
        private final Set<Entry> entries = ConcurrentHashMap.newKeySet();
        
        private void put(final String url, final String id, final Path path) {
            if (url2id.containsKey(url)) {
                return;
            }
            
            url2id.put(url, id);
            id2path.put(id, path);
            
            path2id.put(path, id);
            id2url.put(id, url);
            
            entries.add(new Entry(url, id, path));
        }
        
        public void put(final ApiRequest<?> request) {
            final String url = request.url;
            final String id = hashToBase64(url);
            final String fileName = id + "." + request.getFileExtension();
            final Path path = Paths.get(CACHE_DIR, request.getRelativePath().toString(), fileName);
            put(url, id, path);
        }
        
        public Set<Entry> entrySet() {
            return entries;
        }
        
        @Override
        public String toString() {
            return entries.toString();
        }
        
        public Path getPath(final String idOrUrl) {
            if (idOrUrl.indexOf('/') == -1) {
                final String id = idOrUrl;
                return id2path.get(id);
            } else {
                final String url = idOrUrl;
                return id2path.get(url2id.get(url));
            }
        }
        
        /**
         * @param idOrUrl id or url of ApiRequest
         * @return the prettied (formatted) path of the API response. If the
         *         prettied response was never cached, which is possible, then
         *         the prettied path doesn't exist, so null is returned.
         */
        public Path getPrettyPath(final String idOrUrl) {
            final String path = getPath(idOrUrl).toString();
            final String relativePath = path.substring(CACHE_DIR.length());
            return Paths.get(PRETTY_CACHE_DIR, relativePath);
        }
        
        public String getId(final String url) {
            return url2id.get(url);
        }
        
        public String getUrl(final String id) {
            return id2url.get(id);
        }
        
        public String getUrl(final Path path) {
            return getUrl(path2id.get(path));
        }
        
        public boolean containsUrl(final String url) {
            return url2id.containsKey(url);
        }
        
        public boolean containsId(final String id) {
            return id2path.containsKey(id);
        }
        
        public boolean containsPath(final Path path) {
            return path2id.containsKey(path);
        }
        
        private void load(final Path path) throws IOException {
            MyFiles.readLines(path).forEach(line -> {
                final String[] lineParts = line.split(SEP);
                put(lineParts[0], lineParts[1], Paths.get(lineParts[2]));
            });
        }
        
        private RequestCache(final Path path) {
            // creates cache files if they don't exist
            try {
                MyFiles.createFileIfNonExists(path);
            } catch (final IOException e1) {
                throw new RuntimeException(e1);
            }
            try {
                load(path);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public RequestCache() {
            this(REQUEST_CACHE_PATH);
        }
        
        private void save(final Path path) throws IOException {
            MyFiles.write(path, entries);
        }
        
        public void save() {
            try {
                save(REQUEST_CACHE_PATH);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        
    }
    
    private static final class QueryEncoder extends HashMap<String, String> {
        
        private static final long serialVersionUID = 3055592436293901045L;
        
        public QueryEncoder() {}
        
        @Override
        public String put(final String name, String value) {
            String oldValue;
            try {
                value = URLEncoder.encode(value, "UTF-8");
                oldValue = super.put(name, value);
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
    
    private static final RequestCache requestCache = new RequestCache();
    private static final Thread SAVE_ON_EXIT = new Thread(requestCache::save);
    static {
        // will save cache on exit as long as JVM shuts down w/o internal JVM error
        Runtime.getRuntime().addShutdownHook(SAVE_ON_EXIT);
    }
    
    private final boolean shouldCachePrettied = SHOULD_CACHE_PRETTIED;
    
    private final String apiKey;
    private final String baseUrl;
    
    private final Map<String, String> query = new QueryEncoder();
    
    protected String url;
    
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
            if (!queryValue.isEmpty()) {
                query.put(queryField.getName(), queryValue);
            }
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
    
    protected abstract Path getRelativePath();
    
    protected abstract String getFileExtension();
    
    protected abstract Class<? extends R> getPojoClass();
    
    private void addApiKey() {
        if (!apiKey.isEmpty()) {
            query.put(getApiKeyQueryName(), apiKey);
        }
    }
    
    protected ApiRequest() {
        apiKey = getApiKey();
        baseUrl = getBaseUrl();
        pojoClass = getPojoClass();
    }
    
    private void setQueryAndUrl() {
        reflectQuery();
        addApiKey();
        modifyQuery(query);
        url = baseUrl + query.toString();
    }
    
    protected abstract R parseRequest(Reader reader);
    
    protected abstract String prettify(R request);
    
    protected BufferedReader getHttpRequestReader() throws IOException {
        return new BufferedReader(Internet.getInputStreamReader(url));
    }
    
    private InputStreamReader getCachedRequestReader() throws IOException {
        return new InputStreamReader(Files.newInputStream(requestCache.getPath(url)));
    }
    
    private boolean isCached() {
        return requestCache.containsUrl(url);
    }
    
    private BufferedReader cache(final BufferedReader reader) throws IOException {
        requestCache.put(this);
        final Path path = requestCache.getPath(url);
        //Files.createFile(path);
        MyFiles.write(path, reader);
        return Files.newBufferedReader(path, Constants.CHARSET);
    }
    
    private void prettyCache() throws IOException {
        requestCache.put(this); // in case normal cache not called yet
        final Path path = requestCache.getPrettyPath(url);
        if (Files.exists(path)) {
            return;
        }
        MyFiles.write(path, prettify(request));
    }
    
    public R get() throws IOException {
        if (url == null) {
            setQueryAndUrl();
        }
        if (request == null) {
            Reader requestReader;
            if (isCached()) {
                requestReader = getCachedRequestReader();
            } else {
                requestReader = getHttpRequestReader();
                requestReader = cache((BufferedReader) requestReader);
            }
            request = parseRequest(requestReader);
            if (shouldCachePrettied) {
                prettyCache();
            }
        }
        return request;
    }
    
}
