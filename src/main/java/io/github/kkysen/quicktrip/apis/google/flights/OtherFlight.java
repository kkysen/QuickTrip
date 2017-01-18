package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.reflect.Reflect;

import lombok.Getter;

@Json
@Getter
public class OtherFlight {
    
    private String carrier;
    private String number;
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
}
