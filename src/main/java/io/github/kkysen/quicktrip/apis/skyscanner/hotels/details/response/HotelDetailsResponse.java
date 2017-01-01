package io.github.kkysen.quicktrip.apis.skyscanner.hotels.details.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import io.github.kkysen.quicktrip.apis.Json;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.Amenity;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelEntry;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelPrice;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelUrl;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * 
 * @author Stanley Lin
 */
@Json
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HotelDetailsResponse {
	@SerializedName("hotels")
	private List<HotelEntry> mHotels;
}
