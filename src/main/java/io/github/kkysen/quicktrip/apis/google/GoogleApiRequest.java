public abstract class GoogleMapsApiRequest<T> extends JsonApiRequest<T> {
    
    private static final String API_KEY = "AIzaSyBrc16mMFU7w8Hyo7nFD6ny5SjeOapkY9Q";
    
    private static final String URL = "https://maps.googleapis.com/maps/api/$REQUEST_TYPE/json";
    
    protected String getApiKey() {
        return API_KEY;
    }
    
    protected String getBaseUrl() {
        return URL.replace("$REQUEST_TYPE", getRequestType());
    }
    
    protected abstract String getRequestType();
    
    
    
}
