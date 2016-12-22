public abstract class ApiRequest<T> {
    
    private static final String CACHE_DIRECTORY = "";
    private static final String ID_CACHE = CACHE_DIRECTORY + "IDs.txt";
    private static final String CACHE_SEP = ",";
    
    private static Map<String, String> getIdCache() {
        return Files.lines(ID_CACHE)
            .map(line -> line.split(CACHE_SEP))
            .collect(Collectors.toConcurrentMap(
                lineParts -> lineParts[0],
                lineParts -> lineParts[1]
            ));
    }
    
    private static final Map<String, String> idCache = getIdCache();
    
    private final String apiKey;
    private final String baseUrl;
    private final Properties query;
    private final String url;
    
    private String id;
    
    private T request;
    
    protected String getApiKeyQueryName() {
        return "key";
    }
    
    protected abstract String getApiKey();
    
    protected abstract String getBaseUrl();
    
    protected abstract Properties getQuery();
    
    protected abstract Class<?> getJsonClass();
    
    private void addApiKey() {
        if (!apiKey.isEmpty()) {
            query.setProperty(getApiKeyQueryName(), apiKey);
        }
    }
    
    private String assembleUrl() {
        StringJoiner url = new StringJoiner("&", baseUrl + '?', "");
        for (String name : query.keySet()) {
            url.add(name + '=' + query.getProperty(name));
        }
        return url.toString();
    }
    
    public ApiRequest() {
        apiKey = getApiKey();
        query = getQuery();
        addApiKey();
        url = assembleUrl();
    }
    
    protected abstract T parseRequest(Reader reader);
    
    private Reader getHttpRequestReader() {
        return Internet.getBufferedReader(url);
    }
    
    private Reader getCachedRequestReader() {
        return Files.newBufferedReader(Paths.get(idCache.get(id)));
    }
    
    private boolean isCached() {
        return idCache.containsKey(id);
    }
    
    public T get() {
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
