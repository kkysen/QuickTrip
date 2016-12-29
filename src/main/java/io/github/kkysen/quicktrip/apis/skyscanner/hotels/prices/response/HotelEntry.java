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
public class HotelEntry {
    
    @SerializedName("name")
    private String nName;
    
    @SerializedName("hotel_id")
    private int mId;
    
    @SerializedName("address")
    private String mAddress;
    
    @SerializedName("popularity")
    private int mPopularity;
    
    @SerializedName("amenities")
    private List<Integer> mAmenities;
    
    @SerializedName("latitude")
    private double mLatitude;
    
    @SerializedName("longitude")
    private double mLongitude;
    
    @SerializedName("star_rating")
    private double mStars;
    
    @SerializedName("distance_from_search")
    private double mDistanceFromSearch;
    
}
