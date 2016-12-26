package io.github.kkysen.quicktrip.app;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ScreenController {
    
    private final HashMap<String, Pane> screenMap = new HashMap<>();
    private final Scene scene;
    
    public ScreenController(final Scene scene) {
        this.scene = scene;
    }
    
    public void addScreen(final String name, final Pane pane) {
        screenMap.put(name, pane);
    }
    
    public void removeScreen(final String name) {
        screenMap.remove(name);
    }
    
    public void loadScreen(final String name) {
        final Pane pane = screenMap.get(name);
        if (pane != null) {
            scene.setRoot(pane);
        }
    }
    
}
