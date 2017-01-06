package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.web.Internet;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of JSON POJO representing the API request response
 */
public abstract class JsonRequest<R> extends AbstractJsonRequest<R> {
    
    private final List<Pair<Type, ? extends TypeAdapter<?>>> typedTypeAdapters = new ArrayList<>();
    private final List<Pair<Class<?>, ? extends TypeAdapter<?>>> classedTypeAdapters = new ArrayList<>();
    private final List<? extends TypeAdapterFactory> typeAdapterFactories = new ArrayList<>();
    
    protected void addTypeAdapters(final List<Pair<Type, ? extends TypeAdapter<?>>> adapters) {}
    
    protected <T> void addClassAdapters(
            final List<Pair<Class<?>, ? extends TypeAdapter<?>>> adapters) {}
    
    protected void addTypeAdapterFactories(final List<? extends TypeAdapterFactory> factories) {}
    
    private Gson buildGson() {
        final GsonBuilder builder = new GsonBuilder();
        addTypeAdapters(typedTypeAdapters);
        addClassAdapters(classedTypeAdapters);
        addTypeAdapterFactories(typeAdapterFactories);
        for (final Pair<Type, ? extends TypeAdapter<?>> typedTypeAdater : typedTypeAdapters) {
            final Type type = typedTypeAdater.getKey();
            final TypeAdapter<?> adapter = typedTypeAdater.getValue().nullSafe();
            builder.registerTypeAdapter(type, adapter);
        }
        for (final Pair<Class<?>, ? extends TypeAdapter<?>> classedTypeAdapter : classedTypeAdapters) {
            final Type type = TypeToken.get(classedTypeAdapter.getKey()).getType();
            final TypeAdapter<?> adapter = classedTypeAdapter.getValue().nullSafe();
            builder.registerTypeAdapter(type, adapter);
        }
        typeAdapterFactories.forEach(builder::registerTypeAdapterFactory);
        return builder.create();
    }
    
    private final Gson gson = buildGson();
    
    protected R deserializeFromReader(final Reader reader) {
        return gson.fromJson(reader, getResponseType());
    }
    
    @Override
    protected R deserializeFromUrl(final String url) throws IOException {
        return deserializeFromReader(Internet.getJsonInputStreamReader(url));
    }
    
}
