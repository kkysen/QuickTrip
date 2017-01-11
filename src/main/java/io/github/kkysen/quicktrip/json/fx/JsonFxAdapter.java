package io.github.kkysen.quicktrip.json.fx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javafx.scene.Node;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class JsonFxAdapter extends TypeAdapter<JsonFx> {
    
    private static final Gson GSON = new Gson();
    
    private JsonReader in;
    
    @Override
    public void write(final JsonWriter out, final JsonFx value) throws IOException {
        // not used
    }
    
    private Map<String, String> importClasses() throws IOException {
        if (!in.nextName().equals("imports")) {
            throw new IllegalArgumentException("no imports");
        }
        
        in.beginArray();
        
        final Map<String, String> imports = new HashMap<>();
        while (in.hasNext()) {
            final String fullImport = in.nextString();
            final String[] packageParts = fullImport.split("\\.");
            final String localName = packageParts[packageParts.length - 1];
            imports.put(localName, fullImport);
        }
        
        in.endArray();
        return imports;
    }
    
    @Override
    public JsonFx read(final JsonReader in) throws IOException {
        this.in = in;
        final JsonFx jsonFx = new JsonFx();
        in.beginObject();
        
        final Map<String, String> imports = importClasses();
        
        while (in.hasNext()) {
            final String[] typedId = in.nextName().split("\\s+");
            final String typeAsStr = typedId[0];
            final String id = typedId[1];
            System.out.println(typeAsStr + " " + id);
            try {
                final Class<?> klass = Class.forName(imports.get(typeAsStr));
                if (Node.class.isAssignableFrom(klass)) {
                    @SuppressWarnings("unchecked")
                    final TypeToken<? extends Node> typeToken = (TypeToken<? extends Node>) TypeToken.get(klass);
                    final TypeAdapter<? extends Node> nodeAdapter = GSON.getAdapter(typeToken);
                    final Node node = nodeAdapter.read(in);
                    jsonFx.add(id, node);
                } else {
                    in.skipValue();
                }
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
                in.skipValue();
            }
        }
        
        in.endObject();
        return jsonFx;
    }
    
    public static void main(final String[] args) throws IOException {
        final JsonReader in = GSON
                .newJsonReader(Files.newBufferedReader(Paths.get("JsonFxTest.json")));
        final JsonFx jsonFx = new JsonFxAdapter().read(in);
        System.out.println("JsonFx");
        jsonFx.getNodes().forEach(System.out::println);
    }
    
}
