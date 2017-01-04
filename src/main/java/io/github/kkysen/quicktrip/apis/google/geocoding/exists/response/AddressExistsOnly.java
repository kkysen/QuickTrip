package io.github.kkysen.quicktrip.apis.google.geocoding.exists.response;

import io.github.kkysen.quicktrip.json.Json;

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
public class AddressExistsOnly {
    
    @SerializedName("status")
    private String status;
    
    public boolean exists() {
        return !status.equals("ZERO_RESULTS");
    }
    
}
