package io.github.kkysen.quicktrip.apis.skyscanner.hotels;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import io.github.kkysen.quicktrip.apis.skyscanner.SkyscannerApiRequest;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelResponse;

public class HotelPricesApiRequest extends SkyscannerApiRequest<HotelResponse> {
	private String mMarket;
	private Currency mCurrency;
	private String mLocale;
	private String mEntityId;	//Note: can use lat long w/ this
	private ZonedDateTime mCheckIn;
	private ZonedDateTime mCheckOut;
	private int mGuests;
	private int mRooms;
	
	public HotelPricesApiRequest(String mMarket, String mCurrency, String mLocale, String mEntityId,
			ZonedDateTime mCheckIn, ZonedDateTime mCheckOut, int mGuests, int mRooms) {
		super();
		this.mMarket = mMarket;
		this.mCurrency = mCurrency;
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
		List<String> res = new ArrayList<>();
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
