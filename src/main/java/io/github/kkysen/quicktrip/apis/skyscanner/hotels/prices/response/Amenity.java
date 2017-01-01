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
public class Amenity {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("key")
    private String key;
    
}
