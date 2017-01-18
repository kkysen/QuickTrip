package io.github.kkysen.quicktrip.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PostProcessableFactory implements TypeAdapterFactory {
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        
        return new TypeAdapter<T>() {
            
            @Override
            public void write(final JsonWriter out, final T value) throws IOException {
                if (value instanceof PostProcessable) {
                    ((PostProcessable) value).preSerialize();
                }
                delegate.write(out, value);
            }
            
            @Override
            public T read(final JsonReader in) throws IOException {
                final T obj = delegate.read(in);
                if (obj instanceof PostProcessable) {
                    ((PostProcessable) obj).postDeserialize();
                }
                return obj;
            }
            
        };
    }
    
}
