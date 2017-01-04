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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HotelDetailsApiRequest extends SkyscannerApiRequest<HotelDetailsResponse> {
    
    private final String restOfUrl;
    private final String hotelIds;
    
    public HotelDetailsApiRequest(final HotelResponse hotels) {
        this(hotels.getHotelUrl().getDetails(),
                hotels.getHotelList().stream()
                        .map(HotelEntry::getId)
                        .map(Object::toString)
                        .collect(Collectors.joining(",")));
    }
    
    @Override
    protected List<String> getUrlPathParts() {
        return new ArrayList<>();
    }
    
    @Override
    protected String getOverridingUrl() {
        return getBaseUrl() + restOfUrl.substring(restOfUrl.indexOf('h')) + "&hotelIds=" + hotelIds;
    }
    
    @Override
    protected Class<? extends HotelDetailsResponse> getResponseClass() {
        return HotelDetailsResponse.class;
    }
    
    public static void main(final String[] args) throws InterruptedException, IOException {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final HotelResponse hotelResponse = new HotelPricesApiRequest(
                "US",
                Locale.US,
                40.71, -74.00,
                new GregorianCalendar(2017, 0, 19).toZonedDateTime(),
                new GregorianCalendar(2017, 0, 22).toZonedDateTime(),
                2,
                1).getResponse();
        final HotelDetailsApiRequest request = new HotelDetailsApiRequest(hotelResponse);
        final HotelDetailsResponse hotelDetails = request.getResponse();
        System.out.println(gson.toJson(hotelDetails));
    }
    
}
