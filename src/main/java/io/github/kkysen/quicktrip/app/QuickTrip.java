package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.app.javafx.ScreenController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class QuickTrip extends Application {
    
    public static final String TITLE = "Quick Trip";
    
    private Stage stage;
    
    private static final Pane ROOT = new Pane();
    private static final Scene SCENE = new Scene(ROOT);
    static final ScreenController SCREENS = new ScreenController(SCENE);
    
    @Override
    public void start(final Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle(TITLE);
        stage.setScene(SCENE);
        stage.setMaximized(true);
        SCREENS.load(WelcomeScreen.class);
        stage.show();
    }
    
    @Override
    public void stop() throws Exception {
        SCREENS.get(SearchController.class).serializeModels();
        super.stop();
    }
    
    public static void main(final String[] args) {
        launch(args);
    }
    
}
