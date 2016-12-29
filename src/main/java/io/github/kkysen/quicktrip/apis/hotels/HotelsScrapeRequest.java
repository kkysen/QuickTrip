package io.github.kkysen.quicktrip.apis.hotels;

import io.github.kkysen.quicktrip.apis.RenderedHtmlRequest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class HotelsScrapeRequest extends RenderedHtmlRequest<List<Hotel>> {
    
    private static final String BASE_URL = "https://www.hotels.com/search.do";
    
    
    
    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends List<Hotel>> getPojoClass() {
        return (Class<List<Hotel>>) (Class<?>) List.class;
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
                .map(Hotel::new)
                .collect(Collectors.toList());
    }
    
}
