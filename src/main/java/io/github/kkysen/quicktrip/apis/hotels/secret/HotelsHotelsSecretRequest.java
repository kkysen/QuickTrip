package io.github.kkysen.quicktrip.apis.hotels.secret;

import io.github.kkysen.quicktrip.apis.AbstractJsonRequest;
import io.github.kkysen.quicktrip.apis.hotels.HotelsHotelsQuery;
import io.github.kkysen.quicktrip.app.Destination;
import io.github.kkysen.quicktrip.web.Internet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class HotelsHotelsSecretRequest extends AbstractJsonRequest<List<JsonHotelsHotel>> {
    
    private static final String BASE_URL = "https://www.hotels.com/search/listings.json";
    
    private static final Type pojoType = new TypeToken<List<JsonHotelsHotel>>() {}.getType();
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(pojoType, new JsonHotelsHotelsAdapter().nullSafe())
            .create();
    
    private final HotelsHotelsQuery hotelsQuery;
    private final int lastPageNum;
    private int curPageNum = 1;
    
    public HotelsHotelsSecretRequest(final HotelsHotelsQuery hotelsQuery, final int numHotels) {
        this.hotelsQuery = hotelsQuery;
        lastPageNum = (int) Math.ceil((double) numHotels / 10) + 1;
    }
    
    public HotelsHotelsSecretRequest(final Destination dest, final int numHotels) {
        this(new HotelsHotelsQuery(dest), numHotels);
    }
    
    public HotelsHotelsSecretRequest(final Destination dest) {
        this(dest, 100);
    }
    
    @Override
    protected Class<? extends List<JsonHotelsHotel>> getPojoClass() {
        return null;
    }
    
    @Override
    protected Type getPojoType() {
        return pojoType;
    }
    
    @Override
    protected String getApiKey() {
        return "";
    }
    
    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return Paths.get("hotels", "secret");
    }
    
    @Override
    protected void modifyQuery(final QueryEncoder query) {
        super.modifyQuery(query);
        query.putAll(hotelsQuery.getQuery());
        query.put("pn", String.valueOf(curPageNum));
    }

    @Override
    protected List<JsonHotelsHotel> deserializeFromUrl(final String url) throws IOException {
        final StringBuilder rawJson = Internet.getStringBuilder(url);
        rawJson.delete(rawJson.length() / 3, rawJson.length()); // delete last third, unnecessary info
        final String startStr = "\"results\":";
        final int startIndex = rawJson.indexOf(startStr) + startStr.length();
        final int endIndex = rawJson.lastIndexOf(",\"debugInfo\":");
        final String jsonStr = rawJson.substring(startIndex, endIndex);
        return GSON.fromJson(jsonStr, pojoType);
    }
    
    @Override
    public List<JsonHotelsHotel> getResponse() throws IOException {
        final List<JsonHotelsHotel> hotels = new ArrayList<>(curPageNum * 10);
        while (curPageNum++ < lastPageNum) {
            hotels.addAll(super.getResponse());
        }
        return hotels;
    }
    
}
