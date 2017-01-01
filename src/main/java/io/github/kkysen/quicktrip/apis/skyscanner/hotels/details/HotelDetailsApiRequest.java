package io.github.kkysen.quicktrip.apis.skyscanner.hotels.details;

import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.skyscanner.SkyscannerApiRequest;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.HotelPricesApiRequest;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.details.response.HotelDetailsResponse;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelEntry;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// @AllArgsConstructor
public class HotelDetailsApiRequest extends SkyscannerApiRequest<HotelDetailsResponse> {
    
    private final String sessionKey;
    // I thought maybe apiKey has to come before hotelIds, but still doesn't work
    //private final @QueryField(name = "apiKey") String encryptedApiKey;
    private final String encryptedApiKey;
    private final @QueryField String hotelIds;
    
    private HotelDetailsApiRequest(final String restOfUrl, final List<String> ids) {
        final String[] urlParts = restOfUrl.split("/");
        final String[] sessionKeyAndApiKey = urlParts[urlParts.length - 1].split("\\?apikey=");
        sessionKey = sessionKeyAndApiKey[0];
        encryptedApiKey = sessionKeyAndApiKey[1];
        System.out.println(sessionKey);
        System.out.println(encryptedApiKey);
        hotelIds = String.join(",", ids);
    }
    
    public HotelDetailsApiRequest(final HotelResponse hotels) {
        this(hotels.getHotelUrl().getDetails(),
                hotels.getHotelList().stream()
                .map(HotelEntry::getId)
                .map(Object::toString)
                .collect(Collectors.toList()));
    }
    
    @Override
    protected List<String> getUrlPathParts() {
        return Arrays.asList(
                "hotels",
                "livedetails",
                "v2",
                "details",
                sessionKey
                );
    }
    
    @Override
    protected String getApiKey() {
        //return "";
        return encryptedApiKey;
    }
    
    @Override
    protected Class<? extends HotelDetailsResponse> getPojoClass() {
        return HotelDetailsResponse.class;
    }
    
    public static void main(final String[] args) throws IOException {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        final HotelResponse hotelResponse = new HotelPricesApiRequest(
                "US",
                Locale.US,
                40.71, -74.00,
                new GregorianCalendar(2017, 0, 5).toZonedDateTime(),
                new GregorianCalendar(2017, 0, 6).toZonedDateTime(),
                2,
                1).getResponse();
        //System.out.println(gson.toJson(hotelResponse));
        
        final List<String> ids = hotelResponse.getHotelList()
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
        final HotelDetailsApiRequest request = new HotelDetailsApiRequest(
                hotelResponse.getHotelUrl().getDetails(),
                ids);
        //System.out.println(ids.get(0));
        //System.out.println(r.mHotelIds.get(0));
        final HotelDetailsResponse response = request.getResponse();
        System.out.println(gson.toJson(response));
    }
    
}
