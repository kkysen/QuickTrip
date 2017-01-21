package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.json.TypeAdapterReaderHelper;

import java.io.IOException;
import java.time.Duration;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class SecondsAdapter extends TypeAdapterReaderHelper<Duration> {
    
    @Override
    public void write(final JsonWriter out, final Duration value) throws IOException {
        out.value(value.getSeconds());
    }
    
    @Override
    public Duration read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NUMBER) {
            return Duration.ofSeconds(in.nextLong());
        }
        this.in = in;
        in.beginObject();
        readUntilName("value");
        final Duration seconds = Duration.ofSeconds(in.nextLong());
        readUntilEnd();
        System.out.println("test");
        in.endObject();
        return seconds;
    }
    
}
