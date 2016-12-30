package io.github.kkysen.quicktrip.apis.expedia;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.RenderedHtmlRequest;
import io.github.kkysen.quicktrip.app.Destination;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class ExpediaHotelsScrapeRequest extends RenderedHtmlRequest<List<ExpediaHotel>> {
    
    private static final String BASE_URL = "https://www.expedia.com/HotelsHotel-Search";
    
    private final @QueryField String destination;
    private final @QueryField String startDate;
    private final @QueryField String endDate;
    private final @QueryField int adults;
    
    private static String convertDate(final LocalDate date) {
        final String[] dateParts = date.toString().split("-");
        final String year = dateParts[0];
        dateParts[0] = dateParts[2];
        dateParts[2] = year;
        return String.join("/", dateParts);
    }
    
    public ExpediaHotelsScrapeRequest(final String destination, final LocalDate startDate,
            final LocalDate endDate, final int numPeople) {
        this(destination, convertDate(startDate), convertDate(endDate), numPeople);
    }
    
    public ExpediaHotelsScrapeRequest(final Destination dest) {
        this(dest.getAddress(), dest.getStartDate(), dest.getEndDate(), dest.getNumPeople());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends List<ExpediaHotel>> getPojoClass() {
        return (Class<List<ExpediaHotel>>) (Class<?>) List.class;
    }
    
    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return Paths.get("expedia", "hotels");
    }
    
    @Override
    protected List<ExpediaHotel> parseHtml(final Document doc) {
        return doc.getElementsByClass("flex-link-wrap")
                .parallelStream()
                .map(ExpediaHotel::new)
                .collect(Collectors.toList());
    }
    
}
