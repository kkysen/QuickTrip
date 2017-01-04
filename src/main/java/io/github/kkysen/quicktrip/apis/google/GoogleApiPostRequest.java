
public abstract class GoogleApiPostRequest<P, R> extends GoogleApiRequest<R> {
    
    protected abstract P getRequest();
    
    protected abstract Class<? extends P> getRequestClass();
    
    protected Type getRequestType() {
        return new TypeToken.of(getRequestClass()).getType());
    }  
    
    protected R deserializeFromUrl(String url) {
        
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-type", "application/json");
        request.addHeader("Accept", "application/json");
        StringEntity params = new StringEntity(GSON.toJson(getRequest(), getRequestType()));
        request.setEntity(params);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
    }
    
}
