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
    private List<HotelPrice> mPriceList;
    
    @SerializedName("hotels")
    private List<HotelEntry> mHotelList;
    
    @SerializedName("amenities")
    private List<Amenity> mAmenities;
    
    @SerializedName("places")
    private List<Place> mPlaces;
    
    @SerializedName("urls")
    private HotelUrl mHotelUrl;
    
    @SerializedName("status")
    private String mStatus;
    
    @SerializedName("total_hotels")
    private int mHotelTotal;
    
    @SerializedName("total_available_hotels")
    private int mHotelAvailable;
}
