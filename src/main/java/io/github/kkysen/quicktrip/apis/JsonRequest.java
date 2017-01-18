package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.json.PostProcessableFactory;
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
    
    private final List<Pair<Type, TypeAdapter<?>>> typedTypeAdapters = new ArrayList<>();
    private final List<Pair<Class<?>, TypeAdapter<?>>> classedTypeAdapters = new ArrayList<>();
    private final List<TypeAdapterFactory> typeAdapterFactories = new ArrayList<>();
    
    protected void addTypeAdapters(final List<Pair<Type, TypeAdapter<?>>> adapters) {}
    
    protected <T> void addClassAdapters(
            final List<Pair<Class<?>, TypeAdapter<?>>> adapters) {}
    
    protected void addTypeAdapterFactories(final List<TypeAdapterFactory> factories) {}
    
    private Gson buildGson() {
        final GsonBuilder builder = new GsonBuilder();
        typeAdapterFactories.add(new PostProcessableFactory());
        addTypeAdapters(typedTypeAdapters);
        addClassAdapters(classedTypeAdapters);
        addTypeAdapterFactories(typeAdapterFactories);
        for (final Pair<Type, TypeAdapter<?>> typedTypeAdater : typedTypeAdapters) {
            final Type type = typedTypeAdater.getKey();
            final TypeAdapter<?> adapter = typedTypeAdater.getValue().nullSafe();
            builder.registerTypeAdapter(type, adapter);
        }
        for (final Pair<Class<?>, TypeAdapter<?>> classedTypeAdapter : classedTypeAdapters) {
            final Type type = TypeToken.get(classedTypeAdapter.getKey()).getType();
            final TypeAdapter<?> adapter = classedTypeAdapter.getValue().nullSafe();
            builder.registerTypeAdapter(type, adapter);
        }
        typeAdapterFactories.forEach(builder::registerTypeAdapterFactory);
        return builder.create();
    }
    
    protected final Gson gson = buildGson();
    
    protected R deserializeFromReader(final Reader reader) {
        return gson.fromJson(reader, getResponseType());
    }
    
    @Override
    protected R deserializeFromUrl(final String url) throws IOException {
        // for debugging
        //        final String json = Internet.getString(url);
        //        System.out.println(json);
        //        return gson.fromJson(json, getResponseType());
        return deserializeFromReader(Internet.getJsonInputStreamReader(url));
    }
    
}
