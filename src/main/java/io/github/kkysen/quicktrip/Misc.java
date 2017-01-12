package io.github.kkysen.quicktrip;

import io.github.kkysen.quicktrip.app.javafx.WholeNumberField;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

import javafx.scene.Node;

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
        System.out.println(Node.class.isAssignableFrom(WholeNumberField.class));
        System.out.println(WholeNumberField.class.isInstance(Node.class));
    }
    
}
