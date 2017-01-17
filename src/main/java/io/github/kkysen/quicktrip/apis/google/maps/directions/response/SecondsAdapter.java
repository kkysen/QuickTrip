package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.json.TypeAdapterReaderHelper;

import java.io.IOException;
import java.time.Duration;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


/**
 * 
 * 
 * @author Khyber Sen
 */
public class SecondsAdapter extends TypeAdapterReaderHelper<Duration> {

    
    @Override
    public void write(final JsonWriter out, final Duration value) throws IOException {
        out.beginObject();
        out.name("duration").value(value.getSeconds());
        out.endObject();
    }

    
    @Override
    public Duration read(final JsonReader in) throws IOException {
        this.in = in;
        in.beginObject();
        readUntilName("value");
        final Duration seconds = Duration.ofSeconds(in.nextLong());
        readUntilEnd();
        in.endObject();
        return seconds;
    }
    
}
