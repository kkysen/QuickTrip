package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.reflect.Reflect;

import java.util.List;

import lombok.Getter;

@Json
@Getter
public class Slice {
    
    private int duration;
    private List<Segment> segment;
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
}
