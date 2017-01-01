package io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response;

import io.github.kkysen.quicktrip.apis.Json;

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
public class HotelResponse {
    
    @SerializedName("hotels_prices")
    private List<HotelPrice> priceList;
    
    @SerializedName("hotels")
    private List<HotelEntry> hotelList;
    
    @SerializedName("amenities")
    private List<Amenity> amenities;
    
    @SerializedName("places")
    private List<Place> places;
    
    @SerializedName("urls")
    private HotelUrl hotelUrl;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("total_hotels")
    private int hotelTotal;
    
    @SerializedName("total_available_hotels")
    private int hotelAvailable;
}
