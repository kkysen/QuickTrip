package io.github.kkysen.quicktrip.apis.google.flights;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class GoogleFlightsApiRequestAdapter extends TypeAdapter<GoogleFlightsApiRequest> {
    
    @Override
    public void write(final JsonWriter out, final GoogleFlightsApiRequest value)
            throws IOException {
        // @formatter:off
        out.beginObject()
            .name("request")
                .beginObject()
                    .name("passengers")
                        .beginObject()
                            .name("adultCount").value(value.getNumPeople())
                        .endObject()
                    .name("slice")
                        .beginArray()
                            .beginObject()
                                .name("origin").value(value.getOrigin())
                                .name("destination").value(value.getDestination())
                                .name("date").value(value.getDate().toString())
                            .endObject()
                        .endArray()
                    .name("solutions").value(value.getNumSolutions())
                .endObject()
        .endObject();
        // @formatter:on
    }
    
    @Override
    public GoogleFlightsApiRequest read(final JsonReader in) throws IOException {
        return null; // not used
    }
    
}
