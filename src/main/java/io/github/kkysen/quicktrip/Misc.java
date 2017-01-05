package io.github.kkysen.quicktrip;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class Misc {
    
    private final String field;
    private final int i;
    
    public String json() {
        return new Gson().toJson(this);
    }
    
    public static void main(final String[] args) {
        System.out.println(new Misc("hello", 5).json());
    }
    
}
