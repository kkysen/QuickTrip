package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.io.MyFiles;
import io.github.kkysen.quicktrip.reflect.Reflect;
import io.github.kkysen.quicktrip.reflect.annotations.AnnotationUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.google.common.reflect.TypeToken;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of POJO representing the API response response, presumably a
 *            JSON one
 */
public abstract class CachedApiRequest<R> implements Request<R> {
    
    private static final String CACHE_DIR = "src/main/resources/apiCache/";
    
    /**
     * 
     * 
     * @author Khyber Sen
     */
    public static final class RequestCache {
        
        private static final Path REQUEST_CACHE_PATH = Paths.get(CACHE_DIR, "requestCache.txt");
        
        /**
         * separator in requestCache.txt
         * used to be ",", but the URL for skyscanner hotels includes a comma
         * now switched to a tab
         */
        private static final String SEP = "\t";
        
        private static final MessageDigest sha128;
        static {
            try {
                sha128 = MessageDigest.getInstance("SHA-1");
            } catch (final NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        
        private static final Base64.Encoder fileNameEncoder = Base64.getUrlEncoder();
        
        /**
         * @param s any String
         * @return a SHA-128 hash in base 64 and URL encoded
         */
        private static String hashToBase64(final String s) {
            sha128.update(s.getBytes(Constants.CHARSET));
            final byte[] hashed = sha128.digest();
            sha128.reset();
            return fileNameEncoder.encodeToString(hashed);
        }
        
        /**
         * an entry in a RequestCache
         * 
         * @author Khyber Sen
         */
        @RequiredArgsConstructor
        @Getter
        private static class Entry implements Comparable<Entry> {
            
            private final String url;
            private final String id;
            private final Path path;
            private final Instant time;
            
            /**
             * @return a {@value RequestCache#SEP} (tab) separate line
             * 
             * @see java.lang.Object#toString()
             */
            @Override
            public String toString() {
                return String.join(SEP, time.toString(), url, id, path.toString());
            }
            
            /**
             * compares by time
             * 
             * @see java.lang.Comparable#compareTo(java.lang.Object)
             */
            @Override
            public int compareTo(final Entry other) {
                int cmp = time.compareTo(other.time);
                if (cmp == 0) {
                    cmp = url.compareTo(other.url);
                }
                return cmp;
            }
            
            public boolean equals(final Entry other) {
                return id.equals(other.id);
            }
            
            @Override
            public int hashCode() {
                return id.hashCode();
            }
            
        }
        
        private final Map<String, String> url2id = new ConcurrentHashMap<>();
        private final Map<String, Path> id2path = new ConcurrentHashMap<>();
        
        private final Map<Path, String> path2id = new ConcurrentHashMap<>();
        private final Map<String, String> id2url = new ConcurrentHashMap<>();
        
        private final Map<String, Instant> url2time = new ConcurrentSkipListMap<>();
        
        private final Set<Entry> entries = ConcurrentHashMap.newKeySet();
        
        private final Map<String, TypeToken<?>> id2typeToken = new ConcurrentHashMap<>();
        
        /**
         * puts this "request" in the request cache (all the underlying maps)
         * 
         * @param time the timestamp
         * @param url the url
         * @param id the id, the URL encoded, base 64, SHA-128 hash or the url
         * @param path the relative path with a filename matching the id
         * @param typeToken a TypeToken containing the Type of the request
         */
        private void put(final Instant time, final String url, final String id, final Path path,
                final TypeToken<?> typeToken) {
            if (url2id.containsKey(url)) {
                return;
            }
            
            url2id.put(url, id);
            id2path.put(id, path);
            
            path2id.put(path, id);
            id2url.put(id, url);
            
            url2time.put(url, time);
            
            entries.add(new Entry(url, id, path, time));
            
            id2typeToken.put(id, typeToken);
        }
        
        /**
         * puts a request in the request cache by its url, id, cachePath, and
         * generic runtime type
         * 
         * the id is a URL, base 64 encoded SHA-128 hash of the url
         * {@link #hashToBase64(String)}
         * 
         * the path is the relative directory path and the filename
         * {@link CachedApiRequest#getRelativeCachePath()}
         * 
         * the filename is the id with the file extension
         * {@link CachedApiRequest#getFileExtension()}
         * 
         * the Type is from {@link CachedApiRequest#getResponseClass()} or
         * {@link CachedApiRequest#getResponseType()}
         * 
         * @param request a request
         */
        public void put(final CachedApiRequest<?> request) {
            final String url = request.url;
            final String id = hashToBase64(url);
            final String fileName = id + "." + request.getFileExtension();
            final Path path = //
                    Paths.get(CACHE_DIR, request.getRelativeCachePath().toString(), fileName);
            final TypeToken<?> typeToken = TypeToken.of(request.getResponseType());
            put(Instant.now(), url, id, path, typeToken);
        }
        
        private void changePath(final Path oldPath, final Path newPath) {
            final String id = path2id.get(oldPath);
            if (id == null) {
                throw new NullPointerException(oldPath.toString());
            }
            id2path.replace(id, oldPath, newPath);
            path2id.remove(oldPath, id);
            path2id.put(newPath, id);
            final String url = id2url.get(id);
            final Instant time = url2time.get(url);
            final Entry entry = new Entry(url, id, newPath, time);
            entries.remove(entry);
            entries.add(entry); // will replace old one b/c hashCode based only on id
        }
        
        private void movePath(final Path oldPath, final Path newPath) throws IOException {
            try {
                changePath(oldPath, newPath);
            } catch (final NullPointerException e) {
                Files.deleteIfExists(oldPath);
                Files.deleteIfExists(newPath);
            }
            Files.move(oldPath, newPath);
        }
        
        /**
         * @return a view of the entry set
         * 
         * @see Entry
         */
        public Set<Entry> entrySet() {
            return entries;
        }
        
        /**
         * @return {@link HashSet#toString()} of {@link #entries}
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return entries.toString();
        }
        
        /**
         * @param idOrUrl the ID or URL of a request
         * @return the path of the request
         */
        public Path getPath(final String idOrUrl) {
            if (idOrUrl.indexOf('/') == -1) {
                final String id = idOrUrl;
                return id2path.get(id);
            } else {
                final String url = idOrUrl;
                final String id = url2id.get(url);
                if (id == null) {
                    return null;
                }
                return id2path.get(id);
            }
        }
        
        /**
         * @param url the URL of a request
         * @return the ID of the request
         */
        public String getId(final String url) {
            return url2id.get(url);
        }
        
        /**
         * @param path the path of a request
         * @return the ID of the request
         */
        public String getId(final Path path) {
            return path2id.get(path);
        }
        
        /**
         * @param id the ID of a request
         * @return the URL of the request
         */
        public String getUrl(final String id) {
            return id2url.get(id);
        }
        
        /**
         * @param path the path of a request
         * @return the URL of the request
         */
        public String getUrl(final Path path) {
            return getUrl(path2id.get(path));
        }
        
        /**
         * @param url a URL
         * @return the time of the request
         */
        public Instant getTime(final String url) {
            return url2time.get(url);
        }
        
        /**
         * @param url the URL of a request
         * @return true if a request with that URL has been cached
         */
        public boolean containsUrl(final String url) {
            return url2id.containsKey(url);
        }
        
        /**
         * @param id the ID of a request
         * @return true if a request with that ID has been cached
         */
        public boolean containsId(final String id) {
            return id2path.containsKey(id);
        }
        
        /**
         * @param path the path of a request
         * @return true if a request with that path has been cached
         */
        public boolean containsPath(final Path path) {
            return path2id.containsKey(path);
        }
        
        private TypeToken<?> getTypeToken(final String urlOrId) {
            String id;
            if (urlOrId.indexOf('/') == -1) {
                id = urlOrId;
            } else {
                id = id2url.get(urlOrId);
            }
            return id2typeToken.get(id);
        }
        
        private TypeToken<?> getTypeToken(final Path path) {
            return getTypeToken(path2id.get(path));
        }
        
        public Type getType(final String urlOrId) {
            return getTypeToken(urlOrId).getType();
        }
        
        public Type getType(final Path path) {
            return getTypeToken(path).getType();
        }
        
        private String getTypeTokenPath(final Path cachePath) {
            final String path = cachePath.toString();
            return path.substring(0, path.lastIndexOf('.') + 1) + "ser";
        }
        
        public void serializeTypeToken(final Path cachePath) throws IOException {
            final String id = path2id.get(cachePath);
            final TypeToken<?> typeToken = id2typeToken.get(id);
            final FileOutputStream fileOut = new FileOutputStream(getTypeTokenPath(cachePath));
            final ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(typeToken);
            out.close();
            fileOut.close();
        }
        
        @SuppressWarnings("serial")
        private TypeToken<?> deserializeTypeToken(final Path cachePath) throws IOException {
            final FileInputStream fileIn;
            try {
                fileIn = new FileInputStream(getTypeTokenPath(cachePath));
            } catch (final FileNotFoundException e) {
                return new TypeToken<Object>() {};
            }
            final ObjectInputStream in = new ObjectInputStream(fileIn);
            try {
                return (TypeToken<?>) in.readObject();
            } catch (final ClassNotFoundException e) {
                throw new RuntimeException(e); // shouldn't happen
            } finally {
                in.close();
                fileIn.close();
            }
        }
        
        private void load(final Path path) throws IOException {
            final List<String> lines = MyFiles.readLines(path);
            lines.removeIf(String::isEmpty);
            for (final String line : lines) {
                final String[] lineParts = line.split(SEP);
                final Instant time = Instant.parse(lineParts[0]);
                final String url = lineParts[1];
                final String id = lineParts[2];
                final Path requestPath = Paths.get(lineParts[3]);
                final TypeToken<?> typeToken = deserializeTypeToken(requestPath);
                put(time, url, id, requestPath, typeToken);
            }
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
        
        /**
         * creates the main RequestCache (only one is used statically) from
         * {@link #REQUEST_CACHE_PATH}
         */
        public RequestCache() {
            this(REQUEST_CACHE_PATH);
        }
        
        private void save(final Path path) throws IOException {
            final List<Entry> entryList = new ArrayList<>(entries);
            entryList.sort(null);
            MyFiles.write(path, entryList);
        }
        
        /**
         * saves this RequestCache as a {@value #SEP} separated .txt file at
         * {@link #REQUEST_CACHE_PATH}
         */
        public void save() {
            try {
                save(REQUEST_CACHE_PATH);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        
    }
    
    /**
     * a QueryParams that encodes the name=value pairs in a query string
     * 
     * extends {@link LinkedHashMap} to maintain insertion order
     * 
     * @author Khyber Sen
     */
    public static final class QueryParams extends LinkedHashMap<String, String> {
        
        private static final long serialVersionUID = 3055592436293901045L;
        
        public QueryParams() {}
        
        /**
         * calls {@link LinkedHashMap#put(Object, Object)}
         * except with the value URL encoded by {@link URLEncoder} in UTF-8
         * 
         * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
         */
        @Override
        public String put(String name, String value) {
            String oldValue;
            try {
                name = URLEncoder.encode(name, "UTF-8");
                value = URLEncoder.encode(value, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e); // shouldn't happen
            }
            oldValue = super.put(name, value);
            return oldValue;
        }
        
        /**
         * bypasses URL (percent) encoding
         * 
         * @param name the name
         * @param value the value
         * @return previous value, null if new name
         */
        public String putRaw(final String name, final String value) {
            return super.put(name, value);
        }
        
        /**
         * @return the encoded query string
         * 
         * @see java.util.AbstractMap#toString()
         */
        @Override
        public String toString() {
            final StringJoiner queryString = new StringJoiner("&", "?", "");
            entrySet().forEach(entry -> queryString.add(entry.getKey() + '=' + entry.getValue()));
            return queryString.toString();
        }
        
    }
    
    private static final class KeyManager {
        
        private int index = 0;
        private final List<String> keys = new ArrayList<>();
        
        public KeyManager() {}
        
        public boolean addKey(final String key) {
            if (keys.contains(key)) {
                return false;
            }
            keys.add(key);
            return true;
        }
        
        public String get() {
            return keys.get(index);
        }
        
        public String next() {
            index++;
            if (index > keys.size()) {
                index = 0;
            }
            return get();
        }
        
    }
    
    /**
     * the one and only {@link RequestCache} that caches all the requests made
     */
    private static final RequestCache requestCache = new RequestCache();
    
    /**
     * a Thread that runs {@link RequestCache#save()}
     * added as a JVM shutdown hook so that it is saved whenever the program
     * finishes
     */
    private static final Thread SAVE_ON_EXIT = new Thread(requestCache::save);
    static {
        // will save cache on exit as long as JVM shuts down w/o internal JVM error
        Runtime.getRuntime().addShutdownHook(SAVE_ON_EXIT);
    }
    
    private final QueryParams query = new QueryParams();
    
    private final KeyManager apiKeys = new KeyManager();
    
    private @Getter(AccessLevel.PROTECTED) String url;
    
    /**
     * @return the Class of type R of this request's response
     */
    protected abstract Class<? extends R> getResponseClass();
    
    /**
     * If getPojoClass does not work because of generics, return null.
     * Then override this and return the Type.
     * 
     * @return the Type (including generic type) of this request's response
     */
    protected Type getResponseType() {
        return TypeToken.of(getResponseClass()).getType();
    }
    
    private R response;
    
    /**
     * @return the name of the API key in the query string
     *         defaults to "key"
     */
    protected String getApiKeyQueryName() {
        return "key";
    }
    
    /**
     * @return the API key
     *         if "" (empty String) is returned, then no API key will be
     *         included in the query string
     */
    protected abstract String getApiKey();
    
    protected List<String> getApiKeys() {
        return Arrays.asList(getApiKey());
    }
    
    /**
     * @return the base URL of the request (everything before the query string)
     */
    protected abstract String getBaseUrl();
    
    /**
     * uses reflection to find all the {@link QueryField}s and adds them to the
     * query
     * {@link QueryParams}
     */
    protected void reflectQuery() {
        final List<Field> queryFields = Reflect.getInstanceVars(this);
        // filter out fields that are not QueryFields or have encode = false
        final Map<Field, QueryField> field2annotation = new HashMap<>();
        queryFields.removeIf(field -> {
            final QueryField queryField = AnnotationUtils.getAnnotation(field, QueryField.class);
            final boolean shouldRemove = queryField == null || !queryField.include();
            if (!shouldRemove) {
                field2annotation.put(field, queryField);
            }
            return shouldRemove;
        });
        final Map<Field, Object> queryEntries = Reflect.getFieldEntries(queryFields, this);
        for (final Entry<Field, Object> entry : queryEntries.entrySet()) {
            final Field queryField = entry.getKey();
            final String value = entry.getValue().toString();
            // if the value of this query is an empty String, then skip it
            if (!value.isEmpty()) {
                // if QueryField.name() has been overriden and returns something other than an empty String, 
                // then change the name of this query to QueryField.name()
                // otherwise use the name of the actual Field
                final QueryField annotation = field2annotation.get(queryField);
                String name = annotation.name();
                if (name.isEmpty()) {
                    name = queryField.getName();
                }
                if (annotation.encode()) {
                    query.put(name, value);
                } else {
                    query.putRaw(name, value);
                }
            }
        }
    }
    
    /**
     * allows a subclass to modify the final query before it's assembled into
     * the final url
     * without overriding, this method does nothing
     * 
     * @param query the existing query with the reflected query fields and API
     *            key added
     */
    protected void modifyQuery(final QueryParams query) {}
    
    /**
     * @return the relative Path of the directory in which this request should
     *         be cached. The absolute Path is {@value #CACHE_DIR} +
     *         {@link #getRelativeCachePath()}.
     */
    protected abstract Path getRelativeCachePath();
    
    /**
     * @return the file extension for the cachePath of this request
     */
    protected abstract String getFileExtension();
    
    /**
     * @return a complete URL that overrides all the other URL stuff
     *         null if no overriding URL
     */
    protected String getOverridingUrl() {
        return null;
    }
    
    private void setApiKey() {
        getApiKeys().forEach(apiKeys::addKey);
        final String apiKey = apiKeys.get();
        if (!apiKey.isEmpty()) {
            query.put(getApiKeyQueryName(), apiKey);
        }
    }
    
    private void setQuery() {
        reflectQuery();
        setApiKey();
        modifyQuery(query);
    }
    
    private void setUrl() {
        final String overridingUrl = getOverridingUrl();
        if (overridingUrl != null) {
            url = overridingUrl;
            return;
        }
        url = getBaseUrl() + query.toString();
    }
    
    /**
     * If a cached response for this request is more than
     * {@link #getRefreshDuration()} old, then a new request is made.
     * 
     * @return the Duration for how long a response is still valid
     *         defaults to 1 day
     */
    protected Duration getRefreshDuration() {
        return Duration.ofDays(1);
    }
    
    /**
     * @return true if the last cached request is expired (i.e. the time between
     *         now and the last cache is greater than
     *         {@link #getRefreshDuration()})
     */
    private boolean isExpired() {
        final Instant lastTime = requestCache.getTime(url);
        final Duration elapsed = Duration.between(lastTime, Instant.now());
        final Duration refreshDuration = getRefreshDuration();
        return refreshDuration.minus(elapsed).isNegative();
    }
    
    /**
     * @return true if the response has been cached and is recent enough
     */
    private boolean isCached() {
        return requestCache.containsUrl(url) && !isExpired();
    }
    
    /**
     * called when the request has been cached
     * 
     * @param path the Path of the file to be deserialized
     * @return the cached response
     * @throws IOException if there's an exception reading the file
     */
    protected abstract R deserializeFromFile(Path path) throws IOException;
    
    /**
     * called when the request hasn't been cached before
     * 
     * @param url the URL of the HTTP request whose response will be
     *            deserialized
     * @return the response
     * @throws IOException if there's an HTTP error
     */
    protected abstract R deserializeFromUrl(String url) throws IOException;
    
    /**
     * called when the request hasn't been cached before and after
     * {@link #deserializeFromUrl(String)} has been called
     * 
     * @param path the Path to save the serialized file at
     * @param response the response to cache
     * @throws IOException if there's an exception caching the serialized file
     */
    protected abstract void cache(Path path, R response) throws IOException;
    
    private void setNonCachedResponse() {
        try {
            if (isCached()) {
                final Path cachePath = requestCache.getPath(url);
                response = deserializeFromFile(cachePath);
            } else {
                // if new request, use next apiKey
                apiKeys.next();
                setApiKey();
                setUrl();
                response = deserializeFromUrl(url);
                requestCache.put(this);
                final Path cachePath = requestCache.getPath(url);
                cache(cachePath, response);
                //requestCache.serializeTypeToken(cachePath);
            }
        } catch (final IOException e) {
            throw new RuntimeIOException(e);
        }
    }
    
    /**
     * wraps an IOException into a RuntimeException
     * 
     * @author Khyber Sen
     */
    private static class RuntimeIOException extends RuntimeException {
        
        private static final long serialVersionUID = -7522220261809796580L;
        
        private final @Getter IOException cause;
        
        public RuntimeIOException(final IOException cause) {
            super(cause);
            this.cause = cause;
        }
        
    }
    
    protected void clearResponse() {
        response = null;
    }
    
    /**
     * gets the response for this request
     * If this request has already been made, then it deserializes R from the
     * cached file. If this request hasn't been made before, then it
     * deserializes R from the
     * response to the HTTP request and then caches it.
     * 
     * @return the response for this request
     * @throws IOException if anything goes wrong in
     *             {@link #cache(Path, Object)},
     *             {@link #deserializeFromFile(Path)},
     *             or {@link #deserializeFromUrl(String)}
     */
    @Override
    public R getResponse() throws IOException {
        if (response == null) {
            setQuery();
            setUrl();
            final Thread setNonCachedResponse = new Thread(this::setNonCachedResponse);
            try {
                setNonCachedResponse.run();
            } catch (final RuntimeIOException e) {
                throw e.getCause();
            }
        }
        return response;
    }
    
}
