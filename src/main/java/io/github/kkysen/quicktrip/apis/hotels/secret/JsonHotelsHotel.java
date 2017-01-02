package io.github.kkysen.quicktrip.apis.hotels.secret;

import io.github.kkysen.quicktrip.apis.Json;
import io.github.kkysen.quicktrip.app.Hotel;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JsonHotelsHotel implements Hotel {
    
    /*
     * FIXME this is unfinished
     * these are the main fields I want, but they're not all simple fields, but objects
     */
    
    private String name;
    
    @JsonAdapter(PhoneNumberAdapter.class)
    @SerializedName("telephone")
    private String phoneNumber;
    
    @JsonAdapter(AddressAdapter.class)
    private String address;
    
    @SerializedName("thumbnailUrl")
    private String imgUrl;
    
    @SerializedName("starRating")
    private double rating;
    
    @JsonAdapter(DistanceAdapter.class)
    private double distance;
    
    @JsonAdapter(PriceAdapter.class)
    private int price;
    
}
