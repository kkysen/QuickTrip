package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import java.io.IOException;
import java.time.Duration;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


/**
 * 
 * 
 * @author Khyber Sen
 */
public class SecondsAdapter extends TypeAdapter<Duration> {

    
    @Override
    public void write(final JsonWriter out, final Duration value) throws IOException {
        out.name("duration").value(value.getSeconds());
    }

    
    @Override
    public Duration read(final JsonReader in) throws IOException {
        return Duration.ofSeconds(in.nextLong());
    }
    
}
