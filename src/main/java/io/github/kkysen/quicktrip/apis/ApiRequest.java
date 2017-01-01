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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
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
public abstract class ApiRequest<R> {
    
    private static final String CACHE_DIR = "src/main/resources/apiCache/";
    
    public static final class RequestCache {
        
        private static final Path REQUEST_CACHE_PATH = Paths.get(CACHE_DIR, "requestCache.txt");
        
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
        
        private static String hashToBase64(final String s) {
            sha128.update(s.getBytes(Constants.CHARSET));
            final byte[] hashed = sha128.digest();
            sha128.reset();
            return fileNameEncoder.encodeToString(hashed);
        }
        
        @RequiredArgsConstructor
        @Getter
        private static class Entry implements Comparable<Entry> {
            
            private final String url;
            private final String id;
            private final Path path;
            private final Instant time;
            
            @Override
            public String toString() {
                return String.join(SEP, time.toString(), url, id, path.toString());
            }
            
            @Override
            public int compareTo(final Entry other) {
                return time.compareTo(other.time);
            }
            
        }
        
        private final Map<String, String> url2id = new ConcurrentHashMap<>();
        private final Map<String, Path> id2path = new ConcurrentHashMap<>();
        
        private final Map<Path, String> path2id = new ConcurrentHashMap<>();
        private final Map<String, String> id2url = new ConcurrentHashMap<>();
        
        private final Map<String, Instant> url2time = new ConcurrentSkipListMap<>();
        
        private final Set<Entry> entries = ConcurrentHashMap.newKeySet();
        
        private final Map<String, TypeToken<?>> id2typeToken = new ConcurrentHashMap<>();
        
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
        
        public void put(final ApiRequest<?> request) {
            final String url = request.url;
            final String id = hashToBase64(url);
            final String fileName = id + "." + request.getFileExtension();
            final Path path = Paths.get(CACHE_DIR, request.getRelativeCachePath().toString(),
                    fileName);
            final TypeToken<?> typeToken;
            if (request.pojoClass == null) {
                typeToken = TypeToken.of(request.pojoType);
            } else {
                typeToken = TypeToken.of(request.pojoClass);
            }
            System.out.println(typeToken.toString());
            put(Instant.now(), url, id, path, typeToken);
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
                final String id = url2id.get(url);
                if (id == null) {
                    return null;
                }
                return id2path.get(id);
            }
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
        
        public RequestCache() {
            this(REQUEST_CACHE_PATH);
        }
        
        private void save(final Path path) throws IOException {
            final List<Entry> entryList = new ArrayList<>(entries);
            entryList.sort(null);
            MyFiles.write(path, entryList);
            //id2path.keySet().forEach(id -> System.out.println(getType(id)));
        }
        
        public void save() {
            try {
                save(REQUEST_CACHE_PATH);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        
    }
    
    private static final class QueryEncoder extends LinkedHashMap<String, String> {
        
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
            entrySet().forEach(entry -> queryString.add(entry.getKey() + '=' + entry.getValue()));
            return queryString.toString();
        }
        
    }
    
    private static final RequestCache requestCache = new RequestCache();
    private static final Thread SAVE_ON_EXIT = new Thread(requestCache::save);
    static {
        // will save cache on exit as long as JVM shuts down w/o internal JVM error
        Runtime.getRuntime().addShutdownHook(SAVE_ON_EXIT);
    }
    
    private final Map<String, String> query = new QueryEncoder();
    
    private @Getter(AccessLevel.PROTECTED) String url;
    
    protected abstract Class<? extends R> getPojoClass();
    
    /**
     * If getPojoClass does not work because of generics, return null.
     * Then override this and return the Type.
     * 
     * @return POJO Type
     */
    protected Type getPojoType() {
        return null;
    }
    
    protected final Class<? extends R> pojoClass;
    protected final Type pojoType;
    
    private R response;
    
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
        final Map<Field, QueryField> field2annotation = new HashMap<>();
        queryFields.removeIf(field -> {
            final QueryField queryField = AnnotationUtils.getAnnotation(field, QueryField.class);
            final boolean shouldRemove = queryField == null || !queryField.encode();
            if (!shouldRemove) {
                field2annotation.put(field, queryField);
            }
            return shouldRemove;
        });
        final Map<Field, Object> queryEntries = Reflect.getFieldEntries(queryFields, this);
        for (final Entry<Field, Object> entry : queryEntries.entrySet()) {
            final Field queryField = entry.getKey();
            final String queryValue = entry.getValue().toString();
            if (!queryValue.isEmpty()) {
                final QueryField annotation = field2annotation.get(queryField);
                String name = annotation.name();
                if (name.isEmpty()) {
                    name = queryField.getName();
                }
                query.put(name, queryValue);
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
    
    protected abstract Path getRelativeCachePath();
    
    protected abstract String getFileExtension();
    
    private void addApiKey() {
        final String apiKey = getApiKey();
        if (!apiKey.isEmpty()) {
            query.put(getApiKeyQueryName(), apiKey);
        }
    }
    
    protected ApiRequest() {
        pojoClass = getPojoClass();
        pojoType = getPojoType();
    }
    
    /**
     * @return complete URL that overrides all the other url stuff
     */
    protected String getOverridingUrl() {
        return null;
    }
    
    private void setQueryAndUrl() {
        final String overridingUrl = getOverridingUrl();
        if (overridingUrl != null) {
            url = overridingUrl;
            return;
        }
        reflectQuery();
        addApiKey();
        modifyQuery(query);
        url = getBaseUrl() + query.toString();
    }
    
    private boolean isCached() {
        return requestCache.containsUrl(url);
    }
    
    protected abstract R parseFromFile(Path path) throws IOException;
    
    protected abstract R parseFromUrl(String url) throws IOException;
    
    protected abstract void cache(Path path, R response) throws IOException;
    
    public final R getResponse() throws IOException {
        if (url == null) {
            setQueryAndUrl();
        }
        if (response == null) {
            if (isCached()) {
                final Path cachePath = requestCache.getPath(url);
                response = parseFromFile(cachePath);
            } else {
                response = parseFromUrl(url);
                requestCache.put(this);
                final Path cachePath = requestCache.getPath(url);
                cache(cachePath, response);
                requestCache.serializeTypeToken(cachePath);
            }
        }
        return response;
    }
    
}
