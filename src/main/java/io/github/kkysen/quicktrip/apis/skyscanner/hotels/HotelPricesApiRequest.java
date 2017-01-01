package io.github.kkysen.quicktrip.apis.skyscanner.hotels;

import io.github.kkysen.quicktrip.apis.skyscanner.SkyscannerApiRequest;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelResponse;
import lombok.Getter;

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
    
    private final String mMarket;
    private final String mCurrency;
    private final String mLocale;
    private final String mEntityId;		//Note: can use lat long w/ this
    private final String mCheckIn;		//yyyy-mm-dd
    private final String mCheckOut;		//yyyy-mm-dd
    private final String mGuests;
    private final String mRooms;
    
    /**
     * Input parameters, of which most will be formatted for the request url
     * */
    public HotelPricesApiRequest(final String mMarket, final Currency mCurrency, final Locale mLocale, final double lat,
            final double lon, final ZonedDateTime mCheckIn, final ZonedDateTime mCheckOut, final int mGuests, final int mRooms) {
        this.mMarket = mMarket;
        this.mCurrency = mCurrency.getCurrencyCode();
        this.mLocale = localeToString(mLocale);
        this.mEntityId = formatLatLon(lat, lon);
        this.mCheckIn = zonedDateTimeToString(mCheckIn);
        this.mCheckOut = zonedDateTimeToString(mCheckOut);
        this.mGuests = "" + mGuests;
        this.mRooms = "" + mRooms;
    }
    
    public HotelPricesApiRequest(final String mMarket, final Locale mLocale, final double lat, final double lon,
    		final ZonedDateTime mCheckIn, final ZonedDateTime mCheckOut, final int mGuests, final int mRooms) {
        this.mMarket = mMarket;
        this.mCurrency = Currency.getInstance(mLocale).getCurrencyCode();
        this.mLocale = localeToString(mLocale);
        this.mEntityId = formatLatLon(lat, lon);
        this.mCheckIn = zonedDateTimeToString(mCheckIn);
        this.mCheckOut = zonedDateTimeToString(mCheckOut);
        this.mGuests = "" + mGuests;
        this.mRooms = "" + mRooms;
    }
    
    /**
     * Converts ZonedDateTime to a string usable with the api
     * @return String in format of yyyy-mm-dd
     * */
    public static String zonedDateTimeToString(ZonedDateTime t) {
    	return t.getYear() + "-" + 
    String.format("%1$tm-%2$02d", t.getMonth(), t.getDayOfMonth());
    }
    
    /**
     * Converts a {@link Locale} to a string usuable with the api
     * @return String in format of (ISO 639-1 language code)-(ISO 3166-1 alpha-2 country code)
     * */
    public static String localeToString(Locale l) {
    	return l.getLanguage() + "-" + l.getCountry();
    }
    
    public static String formatLatLon(final double lat, final double lon) {
    	return lat + "," + lon + "-latlong";
    }
    
    public HotelResponse call() throws IOException {
    	return parseFromUrl(getBaseUrl()+"?apikey="+getApiKey());
    }
    
    @Override
    protected List<String> getUrlPathParts() {
        final List<String> res = new ArrayList<>();
        res.add("hotels");
        res.add("liveprices");
        res.add("v2");
        res.add(mMarket);
        res.add(mCurrency);
        res.add(mLocale);
        res.add(mEntityId);
        res.add(mCheckIn);
        res.add(mCheckOut);
        res.add(mGuests);
        res.add(mRooms);
        
        return res;
    }
    
    @Override
    protected Class<? extends HotelResponse> getPojoClass() {
        return HotelResponse.class;
    }
    
    public static void main(String[] args) throws IOException {
    	HotelPricesApiRequest h = new HotelPricesApiRequest(
    			"US",
    			Locale.US,
    			40.71, -74.00,
    			new GregorianCalendar(2017, 0, 5).toZonedDateTime(),
    			new GregorianCalendar(2017, 0, 6).toZonedDateTime(),
    			2,
    			1);
    	
    	HotelResponse r = h.call();
    	System.out.println(h.getBaseUrl()+"?apikey="+h.getApiKey());
    	String details = r.getMHotelUrl().getMDetails();
    	int hid = r.getMHotelList().get(0).getMId();
    	System.out.println(details);
    	System.out.println(hid);
    	
    }
}
