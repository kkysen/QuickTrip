package io.github.kkysen.quicktrip.app;

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
    
    public static final String SEARCH_ARGS_PATH = "searchArgs.json";
    
}
