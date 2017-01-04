package io.github.kkysen.quicktrip.apis.skyscanner.hotels.details.response;

import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.Amenity;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelEntry;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelPrice;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.HotelUrl;
import io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response.Place;

import java.util.List;

import com.google.gson.annotations.SerializedName;

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
    private List<HotelEntry> hotels;
    
    @SerializedName("hotels_prices")
    private List<HotelPrice> priceList;
    
    @SerializedName("amenities")
    private List<Amenity> amenities;
    
    @SerializedName("places")
    private List<Place> places;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("total_hotels")
    private int hotelTotal;
    
    @SerializedName("total_available_hotels")
    private int hotelAvailable;
    
}
