package io.github.kkysen.quicktrip.json;

/**
 * 
 * 
 * @author Khyber Sen
 */
/**
 * runs right before and after Gson (de)serialization in JsonRequest
 * 
 * @author Khyber Sen
 */
public interface PostProcessable {
    
    public default void postDeserialize() {}
    
    public default void postSerialize() {}
    
}
