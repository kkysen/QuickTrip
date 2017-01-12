package io.github.kkysen.quicktrip.app.javafx;

import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;

import lombok.Getter;

import javafx.scene.Scene;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ScreenController {
    
    private final Map<Class<? extends Screen>, Screen> screens = new HashMap<>();
    private final Scene scene;
    private @Getter Class<? extends Screen> currentScreen;
    
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
    
    @SuppressWarnings("unchecked")
    public <T extends Screen> T get(final Class<T> screenClass) {
        return (T) screens.get(screenClass);
    }
    
    public void load(final Class<? extends Screen> screenClass) {
        scene.setRoot(get(screenClass).getPane());
        currentScreen = screenClass;
    }
    
}
