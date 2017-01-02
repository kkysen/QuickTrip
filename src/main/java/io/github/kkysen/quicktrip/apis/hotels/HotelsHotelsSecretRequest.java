package io.github.kkysen.quicktrip.apis.hotels;

import io.github.kkysen.quicktrip.apis.AbstractJsonRequest;
import io.github.kkysen.quicktrip.app.Destination;
import io.github.kkysen.quicktrip.web.Internet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class HotelsHotelsSecretRequest extends AbstractJsonRequest<List<JsonHotelsHotel>> {
    
    private static final String BASE_URL = "https://www.hotels.com/search/listings.json";
    
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
        return new TypeToken<List<JsonHotelsHotel>>() {}.getType();
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
        return Paths.get("hotels");
    }
    
    @Override
    protected void modifyQuery(final QueryEncoder query) {
        super.modifyQuery(query);
        query.putAll(hotelsQuery.getQuery());
        query.put("pn", String.valueOf(curPageNum));
        query.put("callback", "dio.pages.sha.searchResultsCallback");
    }

    @Override
    protected List<JsonHotelsHotel> deserializeFromUrl(final String url) throws IOException {
        final StringBuilder rawJson = Internet.getStringBuilder(url);
        final String rawJsonStr = rawJson.substring(rawJson.indexOf("{b"), rawJson.lastIndexOf("}}"));
        final JsonObject data = new JsonParser().parse(rawJsonStr).getAsJsonObject();
        final JsonObject body = data.getAsJsonObject("body");
        final JsonObject searchResults = body.getAsJsonObject("searchResults");
        final JsonArray results = searchResults.get("results").getAsJsonArray();
        return GSON.fromJson(results, pojoType);
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
