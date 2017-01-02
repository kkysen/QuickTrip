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
        //System.out.println(sessionKey);
        //System.out.println(encryptedApiKey);
        hotelIds = String.join(",", ids);
    }
    
    public HotelDetailsApiRequest(final HotelResponse hotels) {
        this(hotels.getHotelUrl().getDetails(),
                hotels.getHotelList().stream()
                        .map(HotelEntry::getId)
                        .map(Object::toString)
                        .collect(Collectors.toList()));
    }
    
    /**
     * Query format:
     * http://partners.api.skyscanner.net/apiservices/hotels/livedetails/v2/details/
     * (session key)?apikey=(encrypted key)
     * &hotelIds=(comma seperated ids)
     * 
     * @return full URL
     */
    public String assembleUrl() {
        return getBaseUrl() + "?apikey=" + encryptedApiKey + "&hotelIds=" + hotelIds;
    }
    
    @Override
    protected List<String> getUrlPathParts() {
        return Arrays.asList(
                "hotels",
                "livedetails",
                "v2",
                "details",
                sessionKey);
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
    
    public static void main(final String[] args) throws InterruptedException, IOException {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        HotelResponse hotelResponse = null;
        final HotelPricesApiRequest temp = new HotelPricesApiRequest(
                "US",
                Locale.US,
                40.71, -74.00,
                new GregorianCalendar(2017, 0, 16).toZonedDateTime(),
                new GregorianCalendar(2017, 0, 17).toZonedDateTime(),
                2,
                1);
        try {
            hotelResponse = temp.getResponse();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        //System.out.println(gson.toJson(hotelResponse));
        
        final List<String> ids = hotelResponse.getHotelList()
                .stream()
                .map(HotelEntry::getId)
                .map(Object::toString)
                .collect(Collectors.toList());
        
        final String rest = hotelResponse.getHotelUrl().getDetails();
        System.out.println(rest);
        
        //String complete = "http://partners.api.skyscanner.net" +
        //		rest /*+ "&hotelIds=" + ids.get(0)*/;
        //System.out.println(complete);
        
        final HotelDetailsApiRequest request = new HotelDetailsApiRequest(
                hotelResponse.getHotelUrl().getDetails(),
                ids);
        
        //Thread.sleep(5000);
        final String req = request.assembleUrl();
        //System.out.println(req);
        int counter = 0;
        HotelDetailsResponse h = null;
        while (counter < 5) {
            try {
                h = request.deserializeFromUrl(req);
                break;
            } catch (final IOException e) {
                System.out.println("trying again");
                //if (counter == 4) 
                //e.printStackTrace();
                Thread.sleep(1000);
            }
            counter++;
        }
        
        System.out.println(gson.toJson(h));
    }
    
}
