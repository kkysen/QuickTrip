package io.github.kkysen.quicktrip.apis.skyscanner.hotels.details;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.github.kkysen.quicktrip.apis.skyscanner.SkyscannerApiRequest;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.HotelPricesApiRequest;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.details.response.HotelDetailsResponse;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelEntry;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelResponse;
import lombok.AllArgsConstructor;

//@AllArgsConstructor
public class HotelDetailsApiRequest extends SkyscannerApiRequest<HotelDetailsResponse>{
	private String mRestOfUrl;
	private List<String> mHotelIds;
	
	public HotelDetailsApiRequest(String rest, List<String> ids) {
		System.out.println("con: " + ids == null);
		mRestOfUrl = rest;
		mHotelIds = new ArrayList<>(ids);
	}
	
	//Ask how to use this thing
	@Override
	protected List<String> getUrlPathParts() {
		/*List<String> res = new ArrayList<>();
		res.add(mRestOfUrl);
		System.out.println(mHotelIds == null);
		res.addAll(mHotelIds);
		return res;*/
		return null;
	}

	@Override
	protected Class<? extends HotelDetailsResponse> getPojoClass() {
		return HotelDetailsResponse.class;
	}
	
	public static void main(String[] args) throws IOException {
		HotelResponse h = (new HotelPricesApiRequest(
    			"US",
    			Locale.US,
    			40.71, -74.00,
    			new GregorianCalendar(2017, 0, 5).toZonedDateTime(),
    			new GregorianCalendar(2017, 0, 6).toZonedDateTime(),
    			2,
    			1)).call();
		
		List<String> f = Arrays.asList(h.getMHotelList()
				.stream()
				.map(t -> ""+t.getMId())
				.toArray(String[]::new));
		/*HotelDetailsApiRequest r = new HotelDetailsApiRequest(
				h.getMHotelUrl().getMDetails(),
				Arrays.asList(h.getMHotelList()
						.stream()
						.map(t -> ""+t.getMId())
						.toArray(String[]::new))
				);*/
		HotelDetailsApiRequest r = new HotelDetailsApiRequest(
				h.getMHotelUrl().getMDetails(),
				f);
		System.out.println(f.get(0));
		//System.out.println(r.mHotelIds.get(0));
	}
}
