package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.reflect.Reflect;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Json
@Getter
public class Trip {
    
    private String requestId;
    //private Data data;
    
    @SerializedName("tripOption")
    private List<GoogleFlight> tripOption;
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
}
