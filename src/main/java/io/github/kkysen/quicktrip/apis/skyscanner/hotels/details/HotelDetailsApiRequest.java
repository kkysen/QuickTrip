package io.github.kkysen.quicktrip.apis.skyscanner.hotels.details;

import io.github.kkysen.quicktrip.apis.skyscanner.SkyscannerApiRequest;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.HotelPricesApiRequest;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.details.response.HotelDetailsResponse;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelEntry;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

// @AllArgsConstructor
public class HotelDetailsApiRequest extends SkyscannerApiRequest<HotelDetailsResponse> {
    
    private final String restOfUrl;
    private final List<String> hotelIds;
    
    public HotelDetailsApiRequest(final String restOfUrl, final List<String> ids) {
        System.out.println("con: " + ids == null);
        this.restOfUrl = restOfUrl;
        hotelIds = new ArrayList<>(ids);
    }
    
    @Override
    protected List<String> getUrlPathParts() {
        return null; // not being used
    }
    
    @Override
    protected String getOverridingUrl() {
        return "http://partners.api.skyscanner.net/" + restOfUrl;
        // FIXME I'm not sure what you want this to be
    }
    
    @Override
    protected Class<? extends HotelDetailsResponse> getPojoClass() {
        return HotelDetailsResponse.class;
    }
    
    public static void main(final String[] args) throws IOException {
        final HotelResponse h = new HotelPricesApiRequest(
                "US",
                Locale.US,
                40.71, -74.00,
                new GregorianCalendar(2017, 0, 5).toZonedDateTime(),
                new GregorianCalendar(2017, 0, 6).toZonedDateTime(),
                2,
                1).call();
        
        final List<String> f = h.getHotelList()
                .stream()
                .map(HotelEntry::getId)
                .map(Object::toString)
                .collect(Collectors.toList());
        /*HotelDetailsApiRequest r = new HotelDetailsApiRequest(
        		h.getMHotelUrl().getMDetails(),
        		Arrays.asList(h.getMHotelList()
        				.stream()
        				.map(t -> ""+t.getMId())
        				.toArray(String[]::new))
        		);*/
        final HotelDetailsApiRequest r = new HotelDetailsApiRequest(
                h.getHotelUrl().getDetails(),
                f);
        System.out.println(f.get(0));
        //System.out.println(r.mHotelIds.get(0));
    }
    
}
