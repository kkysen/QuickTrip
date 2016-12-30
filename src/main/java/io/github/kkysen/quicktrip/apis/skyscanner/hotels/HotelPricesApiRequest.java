package io.github.kkysen.quicktrip.apis.skyscanner.hotels;

import io.github.kkysen.quicktrip.apis.skyscanner.SkyscannerApiRequest;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelResponse;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * 
 * 
 * @author Stanley Lin
 */
public class HotelPricesApiRequest extends SkyscannerApiRequest<HotelResponse> {
    
    private final String mMarket;
    private final String mCurrency;
    private final String mLocale;
    private final String mEntityId;	//Note: can use lat long w/ this
    private final ZonedDateTime mCheckIn;
    private final ZonedDateTime mCheckOut;
    private final int mGuests;
    private final int mRooms;
    
    /*
     *Currency.getInstance(Locale.US).getCurrencyCode() 
     * 
     * */
    public HotelPricesApiRequest(final String mMarket, final Currency mCurrency, final String mLocale, final String mEntityId,
            final ZonedDateTime mCheckIn, final ZonedDateTime mCheckOut, final int mGuests, final int mRooms) {
        this.mMarket = mMarket;
        this.mCurrency = mCurrency.getCurrencyCode();
        this.mLocale = mLocale;
        this.mEntityId = mEntityId;
        this.mCheckIn = mCheckIn;
        this.mCheckOut = mCheckOut;
        this.mGuests = mGuests;
        this.mRooms = mRooms;
    }
    
    //Need a second opinion
    @Override
    protected List<String> getUrlPathParts() {
        final List<String> res = new ArrayList<>();
        res.add("hotels");
        res.add("liveprices");
        res.add("v2");
        res.add(mMarket);
        res.add(mcu);
        res.add();
        res.add();
        res.add();
        res.add();
        res.add();
        res.add();
        
        return res;
    }
    
    @Override
    protected Class<? extends HotelResponse> getPojoClass() {
        return HotelResponse.class;
    }
    
}
