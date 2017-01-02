package io.github.kkysen.quicktrip.apis.hotels.secret.adapters;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PhoneNumberAdapter extends TypeAdapter<String> {
    
    /**
     * @see com.google.gson.TypeAdapter#read(com.google.gson.stream.JsonReader)
     */
    @Override
    public String read(final JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see com.google.gson.TypeAdapter#write(com.google.gson.stream.JsonWriter,
     *      java.lang.Object)
     */
    @Override
    public void write(final JsonWriter out, final String value) throws IOException {
        // TODO Auto-generated method stub
        
    }
    
}
