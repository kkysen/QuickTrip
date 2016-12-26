package io.github.kkysen.quicktrip.app;

import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;

import javafx.scene.Scene;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ScreenController {
    
    private final Map<Class<? extends Screen>, Screen> screens = new HashMap<>();
    private final Scene scene;
    
    private void addAllScenesInAppPackage() {
        final Reflections reflections = new Reflections("io.github.kkysen.quicktrip.app");
        reflections.getSubTypesOf(Screen.class).forEach(this::add);
    }
    
    public ScreenController(final Scene scene) {
        this.scene = scene;
        addAllScenesInAppPackage();
    }
    
    public void add(final Class<? extends Screen> screenClass) {
        Screen screen;
        try {
            screen = screenClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e); // shouldn't happen
        }
        screens.put(screenClass, screen);
    }
    
    public void load(final Class<? extends Screen> screenClass) {
        final Screen screen = screens.get(screenClass);
        if (screen != null) {
            scene.setRoot(screen.getPane());
        }
    }
    
}
