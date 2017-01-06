package io.github.kkysen.quicktrip.app;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class WelcomeScreen implements Screen {
	private static final String VIEW_FILE = "view/WelcomeScreenView.fxml";
	
    private FXMLLoader loader;
	
    @FXML private VBox vBox;
    @FXML private Text welcomeMsg;
    @FXML private Button startBtn;
    
    private void setupVBox() {
        //vBox.setAlignment(Pos.CENTER);
        //vBox.setPadding(new Insets(25, 25, 25, 25));
    }
    
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
        
        //startBtn.setOnAction(event -> QuickTrip.SCREENS.load(SearchScreen.class));
        
        //vBox = new VBox(300, welcomeMsg, startBtn);
        //setupVBox();
    }
    
    @FXML public void onButtonClick(ActionEvent event) {
    	QuickTrip.SCREENS.load(SearchScreen.class);
    }
    
    @Override
    public Pane getPane() {
    	loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource(VIEW_FILE));
    	
    	try {
			vBox = loader.load();	//might cause problems cuz @FXML already used
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	((Text)vBox.lookup("#welcomeMsg")).setText("Welcome to " + QuickTrip.TITLE);
    	//welcomeMsg = new Text("Welcome to " + QuickTrip.TITLE);
    	//welcomeMsg.setFont(Font.font("Tahoma", FontWeight.NORMAL, 72));
    	//welcomeMsg.setText(welcomeMsg.getText() + QuickTrip.TITLE);
    	//vBox.getChildren().remove(0);
    	//vBox.getChildren().add(0, welcomeMsg);
		
    	
        return vBox;
    }
    
}
