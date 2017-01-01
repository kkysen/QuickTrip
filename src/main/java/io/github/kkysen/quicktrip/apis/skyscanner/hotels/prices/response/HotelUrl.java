package io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response;

import io.github.kkysen.quicktrip.apis.Json;

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
public class HotelUrl {
    
    @SerializedName("hotel_details")
    private String details;
    
}
