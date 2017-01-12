package io.github.kkysen.quicktrip.apis.hotels.secret;

import io.github.kkysen.quicktrip.apis.AbstractJsonRequest;
import io.github.kkysen.quicktrip.apis.hotels.HotelsHotelsQuery;
import io.github.kkysen.quicktrip.app.data.Destination;
import io.github.kkysen.quicktrip.app.data.Hotel;
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
public class HotelsHotelsSecretRequest extends AbstractJsonRequest<List<Hotel>> {
    
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
        lastPageNum = (int) Math.ceil((double) numHotels / 10);
    }
    
    public HotelsHotelsSecretRequest(final Destination dest, final int numHotels) {
        this(new HotelsHotelsQuery(dest), numHotels);
    }
    
    public HotelsHotelsSecretRequest(final Destination dest) {
        this(dest, 100);
    }
    
    @Override
    protected Class<? extends List<Hotel>> getResponseClass() {
        return null;
    }
    
    @Override
    protected Type getResponseType() {
        return pojoType;
    }
    
    @Override
    protected List<String> getApiKeys() {
        return null;
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
    protected void modifyQuery(final QueryParams query) {
        super.modifyQuery(query);
        query.putAll(hotelsQuery.getQuery());
        query.put("pn", String.valueOf(curPageNum));
    }
    
    @Override
    protected List<Hotel> deserializeFromUrl(final String url) throws IOException {
        final StringBuilder rawJson = Internet.getStringBuilder(url);
        // check for json error message
        if (rawJson.length() < 600) {
            return new ArrayList<>();
        }
        //System.out.println(rawJson);
        rawJson.delete(rawJson.length() * 2 / 3, rawJson.length()); // delete last third, unnecessary info
        final String startStr = "\"results\":";
        final int startIndex = rawJson.indexOf(startStr) + startStr.length();
        final int endIndex = rawJson.lastIndexOf(",\"debugInfo\":");
        final String jsonStr = rawJson.substring(startIndex, endIndex);
        return GSON.fromJson(jsonStr, pojoType);
    }
    
    @Override
    public List<Hotel> getResponse() throws IOException {
        final List<Hotel> hotels = new ArrayList<>(curPageNum * 10);
        while (curPageNum++ < lastPageNum + 1) {
            final List<Hotel> hotelsOnOnePage = super.getResponse();
            clearResponse();
            System.out.println(hotelsOnOnePage.size());
            hotels.addAll(hotelsOnOnePage);
        }
        System.out.println(hotels.size());
        return hotels;
    }
    
}
