package io.github.kkysen.quicktrip.apis.hotels.secret.adapters;

import io.github.kkysen.quicktrip.apis.hotels.secret.JsonHotelsHotel;

import java.io.IOException;
import java.util.List;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class JsonHotelsHotelAdapter extends TypeAdapter<List<JsonHotelsHotel>> {
    
    private JsonHotelsHotel readHotel(final JsonReader in) throws IOException {
        
    }
    
    /**
     * @see com.google.gson.TypeAdapter#read(com.google.gson.stream.JsonReader)
     */
    @Override
    public List<JsonHotelsHotel> read(final JsonReader in) throws IOException {
        in.beginObject(); // data
        
        in.nextName();
        in.beginObject(); // body
        
        in.nextName();
        in.skipValue(); // header
        
        in.nextName();
        in.skipValue(); // query
        
        in.nextName();
        in.beginObject(); // searchResults
        
        in.nextName();
        in.skipValue(); // totalCount
        
        in.nextName();
        in.skipValue(); // pagination
        
        in.nextName();
        in.beginArray();
        
        
    }
    
    /**
     * @see com.google.gson.TypeAdapter#write(com.google.gson.stream.JsonWriter,
     *      java.lang.Object)
     */
    @Override
    public void write(final JsonWriter out, final List<JsonHotelsHotel> value) throws IOException {
        // not used
    }
    
}
