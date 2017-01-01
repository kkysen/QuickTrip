package io.github.kkysen.quicktrip.apis.skyscanner.hotels;

import io.github.kkysen.quicktrip.apis.skyscanner.SkyscannerApiRequest;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelResponse;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 
 * @author Stanley Lin
 */
public class HotelPricesApiRequest extends SkyscannerApiRequest<HotelResponse> {
    
    private final String market;
    private final String currency;
    private final String locale;
    private final String entityId;		//Note: can use lat long w/ this
    private final String checkIn;		//yyyy-mm-dd
    private final String checkOut;		//yyyy-mm-dd
    private final String guests;
    private final String rooms;
    
    /**
     * Input parameters, of which most will be formatted for the request url
     */
    public HotelPricesApiRequest(final String market, final Currency currency,
            final Locale locale, final double lat, final double lon, final ZonedDateTime checkIn,
            final ZonedDateTime checkOut, final int guests, final int rooms) {
        this.market = market;
        this.currency = currency.getCurrencyCode();
        this.locale = localeToString(locale);
        this.entityId = formatLatLon(lat, lon);
        this.checkIn = zonedDateTimeToString(checkIn);
        this.checkOut = zonedDateTimeToString(checkOut);
        this.guests = "" + guests;
        this.rooms = "" + rooms;
    }
    
    public HotelPricesApiRequest(final String market, final Locale locale, final double lat,
            final double lon, final ZonedDateTime checkIn, final ZonedDateTime checkOut,
            final int guests, final int rooms) {
        this.market = market;
        this.currency = Currency.getInstance(locale).getCurrencyCode();
        this.locale = localeToString(locale);
        this.entityId = formatLatLon(lat, lon);
        this.checkIn = zonedDateTimeToString(checkIn);
        this.checkOut = zonedDateTimeToString(checkOut);
        this.guests = "" + guests;
        this.rooms = "" + rooms;
    }
    
    /**
     * Converts ZonedDateTime to a string usable with the api
     * 
     * @return String in format of yyyy-mm-dd
     */
    public static String zonedDateTimeToString(final ZonedDateTime time) {
        return time.getYear() + "-" +
                String.format("%1$tm-%2$02d", time.getMonth(), time.getDayOfMonth());
    }
    
    /**
     * Converts a {@link Locale} to a string usuable with the api
     * 
     * @return String in format of (ISO 639-1 language code)-(ISO 3166-1 alpha-2
     *         country code)
     */
    public static String localeToString(final Locale locale) {
        return locale.getLanguage() + "-" + locale.getCountry();
    }
    
    public static String formatLatLon(final double lat, final double lon) {
        return lat + "," + lon + "-latlong";
    }
    
    public HotelResponse call() throws IOException {
        return parseFromUrl(getBaseUrl() + "?apikey=" + getApiKey());
    }
    
    @Override
    protected List<String> getUrlPathParts() {
        final List<String> res = new ArrayList<>();
        res.add("hotels");
        res.add("liveprices");
        res.add("v2");
        res.add(market);
        res.add(currency);
        res.add(locale);
        res.add(entityId);
        res.add(checkIn);
        res.add(checkOut);
        res.add(guests);
        res.add(rooms);
        
        return res;
    }
    
    @Override
    protected Class<? extends HotelResponse> getPojoClass() {
        return HotelResponse.class;
    }
    
    public static void main(final String[] args) throws IOException {
        final HotelPricesApiRequest h = new HotelPricesApiRequest(
                "US",
                Locale.US,
                40.71, -74.00,
                new GregorianCalendar(2017, 0, 5).toZonedDateTime(),
                new GregorianCalendar(2017, 0, 6).toZonedDateTime(),
                2,
                1);
        
        final HotelResponse r = h.call();
        System.out.println(h.getBaseUrl() + "?apikey=" + h.getApiKey());
        final String details = r.getHotelUrl().getDetails();
        final int hid = r.getHotelList().get(0).getId();
        System.out.println(details);
        System.out.println(hid);
        
    }
}
