public abstract class JsonApiRequest<T> extends ApiRequest<T> {
    
    private static final Gson gson = new Gson();
    
    protected T parseRequest(Reader reader) {
        return fromJson(reader);
    }
    
}
