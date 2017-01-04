package io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response;

import io.github.kkysen.quicktrip.json.Json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Note: only HotelEntry from the price list call will have a
 * distance_from_search
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
    private String name;
    
    @SerializedName("hotel_id")
    private int id;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("address")
    private String address;
    
    @SerializedName("popularity")
    private int popularity;
    
    @SerializedName("amenities")
    private List<Integer> amenities;
    
    @SerializedName("latitude")
    private double latitude;
    
    @SerializedName("longitude")
    private double longitude;
    
    @SerializedName("star_rating")
    private double stars;
    
    @SerializedName("distance_from_search")
    private double distanceFromSearch;
    
}
