public abstract class ApiRequest<T> {
    
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
        
    }
    
    private boolean isCached() {
        
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
