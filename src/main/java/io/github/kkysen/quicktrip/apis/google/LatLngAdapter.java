package io.github.kkysen.quicktrip.apis.google;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class LatLngAdapter extends TypeAdapter<LatLng> {
    
    @Override
    public void write(final JsonWriter out, final LatLng latLng) throws IOException {
        out.beginObject();
        out.name("lat").value(latLng.getLatitude());
        out.name("lng").value(latLng.getLongitude());
        out.endObject();
    }
    
    @Override
    public LatLng read(final JsonReader in) throws IOException {
        in.beginObject();
        in.nextName();
        final String lat = in.nextString();
        in.nextName();
        final String lng = in.nextString();
        while (in.hasNext()) {
            in.nextName();
            in.skipValue();
        }
        in.endObject();
        return new LatLng(lat, lng);
    }
    
}
