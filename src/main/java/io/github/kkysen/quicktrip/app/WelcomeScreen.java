package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.app.javafx.Screen;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 
 * 
 * @author Khyber Sen
 * @author Stanley Lin
 */
public class WelcomeScreen implements Screen {
    
    private static final String VIEW_FILE = "view/WelcomeScreenView.fxml";
    
    private FXMLLoader loader;
    
    @FXML
    private VBox vBox;
    @FXML
    private final Text welcomeMsg;
    @FXML
    private final Button startBtn;
    
    /*private void setupVBox() {
        //vBox.setAlignment(Pos.CENTER);
        //vBox.setPadding(new Insets(25, 25, 25, 25));
    }*/
    
    public WelcomeScreen() {
        vBox = new VBox();
        welcomeMsg = new Text();
        startBtn = new Button();
        
        //Font.getFontNames().forEach(System.out::println);
        //welcomeMsg = new Text("Welcome to " + QuickTrip.TITLE);
        //welcomeMsg.setFont(Font.font("Tahoma", FontWeight.NORMAL, 72));
        
        //startBtn = new Button("Start Planning Your Trip");
        //startBtn.setAlignment(Pos.CENTER);
        //startBtn.setFont(Font.font(48));
        
        //startBtn.setOnAction(event -> QuickTrip.SCREENS.load(SearchView.class));
        
        //vBox = new VBox(300, welcomeMsg, startBtn);
        //setupVBox();
    }
    
    @FXML
    public void onButtonClick(final ActionEvent event) {
        QuickTrip.SCREENS.load(SearchController.class);
    }
    
    @Override
    public Pane getPane() {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(VIEW_FILE));
        
        try {
            vBox = loader.load();	//might cause problems cuz @FXML already used
        } catch (final IOException e) {
            e.printStackTrace();
        }
        
        ((Text) vBox.lookup("#welcomeMsg")).setText("Welcome to " + QuickTrip.TITLE);
        //welcomeMsg = new Text("Welcome to " + QuickTrip.TITLE);
        //welcomeMsg.setFont(Font.font("Tahoma", FontWeight.NORMAL, 72));
        //welcomeMsg.setText(welcomeMsg.getText() + QuickTrip.TITLE);
        //vBox.getChildren().remove(0);
        //vBox.getChildren().add(0, welcomeMsg);
        
        return vBox;
    }
    
}
