package io.github.kkysen.quicktrip.apis.hotels.scrape;

import io.github.kkysen.quicktrip.apis.HtmlRequest;
import io.github.kkysen.quicktrip.apis.hotels.HotelsHotelsQuery;
import io.github.kkysen.quicktrip.apis.hotels.MissingHotelInformationException;
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
    protected void modifyQuery(final QueryParams query) {
        super.modifyQuery(query);
        query.putAll(hotelsQuery.getQuery());
    }
    
    @Override
    protected Class<? extends List<Hotel>> getResponseClass() {
        return null;
    }
    
    @Override
    protected Type getResponseType() {
        return new TypeToken<List<ScrapedHotelsHotel>>(){}.getType();
    }
    
    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return Paths.get("hotels", "scrape");
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
