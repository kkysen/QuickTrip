public abstract class ApiRequest<T> {
    
    private static final Map<String, String> id2pathMap = new ConcurrentHashMap<>();
    static {
        
    }
    
    private String apiKey;
    private String baseUrl;
    private Properties query;
    private String url;
    
    private T request;
    
    private String id;
    
    protected abstract String getApiKey();
    
    protected abstract String getBaseUrl();
    
    protected abstract Properties getQuery();
    
    protected abstract Class<?> getJsonClass();
    
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
        query.setProperty("key", apiKey);
        
        url = assembleUrl();
    }
    
    protected abstract T parseRequest(Reader reader);
    
    private Reader getHttpRequestReader() {
        return Internet.getBufferedReader(url);
    }
    
    private Reader getCachedRequestReader() {
        return Files.newBufferedReader(Paths.get(id2pathMap.get(id)));
    }
    
    private boolean isCached() {
        return id2pathMap.containsKey(id);
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
