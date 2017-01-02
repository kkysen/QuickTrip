package io.github.kkysen.quicktrip.apis.hotels;

import io.github.kkysen.quicktrip.apis.HtmlRequest;
import io.github.kkysen.quicktrip.app.Destination;
import io.github.kkysen.quicktrip.app.Hotel;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;

import com.google.gson.reflect.TypeToken;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class HotelsHotelsScrapeRequest extends HtmlRequest<List<Hotel>> {
    
    private static final String BASE_URL = "https://www.hotels.com/search.do";
    
    private final HotelsHotelsQuery hotelsQuery;
    
    public HotelsHotelsScrapeRequest(final HotelsHotelsQuery hotelsQuery) {
        this.hotelsQuery = hotelsQuery;
    }
    
    public HotelsHotelsScrapeRequest(final Destination dest) {
        this(new HotelsHotelsQuery(dest));
    }
    
    @Override
    protected void modifyQuery(final QueryEncoder query) {
        super.modifyQuery(query);
        query.putAll(hotelsQuery.getQuery());
    }
    
    @Override
    protected Class<? extends List<Hotel>> getPojoClass() {
        return null;
    }
    
    @Override
    protected Type getPojoType() {
        return new TypeToken<List<ScrapedHotelsHotel>>(){}.getType();
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
    protected List<Hotel> parseHtml(final Document doc) {
        return doc.getElementsByClass("hotel-wrap")
                .parallelStream()
                .map(hotelWrapElem -> {
                    try {
                        return new ScrapedHotelsHotel(hotelWrapElem);
                    } catch (final MissingHotelInformationException e) {
                        return null;
                    }
                })
                .filter(hotel -> hotel != null)
                .collect(Collectors.toList());
    }
    
}
