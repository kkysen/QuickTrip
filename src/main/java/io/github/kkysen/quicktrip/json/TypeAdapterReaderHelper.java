package io.github.kkysen.quicktrip.json;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;


/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class TypeAdapterReaderHelper<T> extends TypeAdapter<T> {
    
    protected JsonReader in;
    
    protected final void readUntilName(final String name) throws IOException {
        while (in.hasNext()) {
            if (in.nextName().equals(name)) {
                break;
            } else {
                in.skipValue();
            }
        }
    }
    
    protected final void readUntilEnd() throws IOException {
        readUntilName(null);
    }
    
    protected final String nextStringNamed(final String name) throws IOException {
        readUntilName(name);
        return in.nextString();
    }
    
    protected final String nextString() throws IOException {
        in.nextName();
        return in.nextString();
    }
    
}
