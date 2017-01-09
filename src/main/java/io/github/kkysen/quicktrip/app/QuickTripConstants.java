package io.github.kkysen.quicktrip.app;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class QuickTripConstants {
    
    public static final Gson GSON = Converters.registerLocalDate(new GsonBuilder())
            .setPrettyPrinting().create();
    
    public static final Path SEARCH_MODEL_PATH = Paths.get("src/main/resources/searchModels.json");
    
}
