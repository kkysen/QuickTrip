package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.io.MyFiles;
import io.github.kkysen.quicktrip.reflect.Reflect;
import io.github.kkysen.quicktrip.reflect.annotations.AnnotationUtils;
import io.github.kkysen.quicktrip.web.Internet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.StringJoiner;

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
    
    /**
     * 
     * 
     * @author Khyber Sen
     */
    private static final class RequestCache {
        
        private static final Path URL_2_ID_PATH = Paths.get(CACHE_DIR, "url2id.csv");
        private static final Path ID_2_PATH_PATH = Paths.get(CACHE_DIR, "id2path.csv");
        
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
        
        private Map<String, String> url2id;
        private Map<String, Path> id2path;
        
        private Map<Path, String> path2id;
        private Map<String, String> id2url;
        
        private void put(final String url, final String id, final Path path) {
            if (url2id.containsKey(url)) {
                return;
            }
            
            url2id.put(url, id);
            id2path.put(id, path);
            
            path2id.put(path, id);
            id2url.put(id, url);
        }
        
        public void put(final ApiRequest<?> request) {
            final String url = request.url;
            final String id = hashToBase64(url);
            final String fileName = id + "." + request.getFileExtension();
            final Path path = Paths.get(CACHE_DIR, request.getRelativePath().toString(), fileName);
            put(url, id, path);
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
            final Path prettyPath = Paths.get(PRETTY_CACHE_DIR, relativePath);
            return Files.exists(prettyPath) ? prettyPath : null;
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
        
        private void loadLine(final String url2idLine, final String id2pathLine, final int index) {
            final String[] url2idLineParts = url2idLine.split(SEP);
            final String[] id2pathLineParts = id2pathLine.split(SEP);
            final String idFromUrl2id = url2idLineParts[1];
            final String idFromId2path = id2pathLineParts[0];
            if (idFromUrl2id != idFromId2path) {
                throw new IllegalArgumentException(
                        "IDs do not match: (line " + index + 1 + ") "
                                + idFromUrl2id + " != " + idFromId2path);
            }
            final String url = url2idLineParts[0];
            final String id = idFromUrl2id;
            final Path path = Paths.get(id2pathLineParts[1]);
            put(url, id, path);
        }
        
        private void load(final Path url2id, final Path id2path) throws IOException {
            final BufferedReader url2idReader = Files.newBufferedReader(url2id, Constants.CHARSET);
            final BufferedReader id2pathReader = Files.newBufferedReader(id2path,
                    Constants.CHARSET);
            final int numUrl2ids = Integer.parseInt(url2idReader.readLine());
            final int numId2paths = Integer.parseInt(id2pathReader.readLine());
            if (numUrl2ids != numId2paths) {
                throw new IllegalArgumentException(
                        "url2id and id2path caches have different numbers of cached requests; "
                                + "url2id: " + numUrl2ids + ", id2path: " + numId2paths);
            }
            final int numRequests = numUrl2ids;
            for (int i = 0; i < numRequests; i++) {
                loadLine(url2idReader.readLine(), id2pathReader.readLine(), i);
            }
        }
        
        private RequestCache(final Path url2id, final Path id2path) {
            // creates cache files if they don't exist
            try {
                MyFiles.createFileIfNonExists(url2id);
            } catch (final IOException e1) {
                throw new RuntimeException(e1);
            }
            try {
                MyFiles.createFileIfNonExists(id2path);
            } catch (final IOException e2) {
                throw new RuntimeException(e2);
            }
            try {
                load(url2id, id2path);
            } catch (final IOException e3) {
                throw new RuntimeException(e3);
            }
        }
        
        public RequestCache() {
            this(URL_2_ID_PATH, ID_2_PATH_PATH);
        }
        
        private static <K, V> void save(final Path path, final Map<K, V> cache) throws IOException {
            final BufferedWriter writer = Files.newBufferedWriter(path, Constants.CHARSET);
            writer.write(String.valueOf(cache.size()));
            writer.newLine();
            for (final K key : cache.keySet()) {
                writer.write(key.toString());
                writer.write(SEP);
                writer.write(cache.get(key).toString());
                writer.newLine();
            }
        }
        
        private void save(final Path url2idPath, final Path id2pathPath) throws IOException {
            save(url2idPath, url2id);
            save(id2pathPath, id2path);
        }
        
        public void save() {
            try {
                save(URL_2_ID_PATH, ID_2_PATH_PATH);
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
    
    private String url;
    
    protected final Class<? extends R> pojoClass;
    private R request;
    
    protected abstract Path getRelativePath();
    
    protected abstract String getFileExtension();
    
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
        //System.out.println(queryEntries);
        for (final Field queryField : queryEntries.keySet()) {
            //System.out.println(queryField.getName());
            final String queryValue = queryEntries.get(queryField).toString();
            //System.out.println(queryValue);
            if (!queryValue.isEmpty()) {
                query.put(queryField.getName(), queryValue);
            }
        }
        query.forEach((k, v) -> System.out.println(k + "=" + v));
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
        MyFiles.write(path, reader);
        return Files.newBufferedReader(path, Constants.CHARSET);
    }
    
    private void prettyCache() throws IOException {
        requestCache.put(this); // in case normal cache not called yet
        final Path path = requestCache.getPrettyPath(url);
        // may potentially overwrite existing prettied cache, b/c not tracked by requestCache
        MyFiles.write(path, prettify(request));
    }
        
    public R get() throws IOException {
        if (url == null) {
            setQueryAndUrl();
        }
        if (request == null) {
            Reader requestReader;
            final boolean isCached = isCached();
            if (isCached) {
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
