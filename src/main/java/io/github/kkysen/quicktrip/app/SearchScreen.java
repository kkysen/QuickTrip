package io.github.kkysen.quicktrip.app;

import static io.github.kkysen.quicktrip.app.QuickTrip.SCREENS;

import io.github.kkysen.quicktrip.apis.google.geocoding.exists.AddressExistsRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class SearchScreen implements Screen {
    
    private final GridPane grid = new GridPane();
    
    private int rowIndex = 0;
    
    private final TextField origin;
    private final TextField startDate;
    private final TextField dest;
    private final TextField numDests;
    private final List<TextField> dests = new ArrayList<>();
    private final TextField numPeople;
    private final TextField budget;
    private final Button searchBtn;
    private final Button backBtn;
    
    private void setupGrid() {
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
    }
    
    private TextField addTextField(final int columnIndex) {
        final TextField text = new TextField();
        grid.add(text, columnIndex, rowIndex);
        return text;
    }
    
    private TextField addLabeledInputField(final String name) {
        final Label label = new Label(name);
        grid.add(label, 0, rowIndex);
        final TextField text = addTextField(1);
        rowIndex++;
        return text;
    }
    
    private Button addButton(final String name, final int columnIndex,
            final EventHandler<ActionEvent> onAction) {
        final Button btn = new Button(name);
        btn.setOnAction(onAction);
        grid.add(btn, columnIndex, rowIndex);
        return btn;
    }
    
    private TextField addButtonedInputField(final String name,
            final EventHandler<ActionEvent> onAction) {
        addButton(name, 0, onAction);
        final TextField text = addTextField(1);
        rowIndex++;
        return text;
    }
    
    private void addDests(final int numDests) {
        for (int destNum = 0; destNum < numDests; destNum++) {
            dests.add(addLabeledInputField("Destination " + (destNum + 1)));
        }
    }
    
    private void errorDialog(final String error, final String msg) {
        final Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(error);
        alert.setHeaderText(msg);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    private void numDestsError() {
        final String error = "Invalid Number of Destinations";
        final String msg = "You must enter a whole number less than or equal to 100";
        errorDialog(error, msg);
    }
    
    private void makeMoreDests() {
        final String numDestsStr = numDests.getText();
        if (numDestsStr.isEmpty()) {
            return;
        }
        
        int numDests;
        try {
            numDests = Integer.parseInt(numDestsStr);
        } catch (final NumberFormatException e) {
            numDestsError();
            return;
        }
        
        if (numDests == 1) {
            if (dests.size() == 1) {
                return; // nothing changes
            }
            dests.remove(0);
            grid.getChildren().removeAll(dests);
            dests.add(dest);
        }
        
        if (numDests < 1 || numDests > 100) {
            numDestsError();
            return;
        }
        
        final int oldRowIndex = rowIndex;
        rowIndex = GridPane.getRowIndex(dests.get(0));
        final int numRowsAfterDests = oldRowIndex - rowIndex;
        
        grid.getChildren().remove(dest);
        dests.clear();
        addDests(numDests);
        
        rowIndex += numRowsAfterDests;
    }
    
    private void nonExistentAddressError(final String address) {
        final String error = "Nonexistent Address";
        final String msg = "\"" + address + "\" does not exist";
        errorDialog(error, msg);
    }
    
    /**
     * @param addressField TextField containing the address
     * @return address String if it exists, null if it does not
     */
    private String validateAddress(final TextField addressField) {
        final String address = addressField.getText();
        try {
            if (AddressExistsRequest.exists(address)) {
                return address;
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        nonExistentAddressError(address);
        return null;
    }
    
    private void serializeSearchArgs() {
        final String originAddress = validateAddress(origin);
        if (originAddress == null) {
            return;
        }
        
        final List<Destination> validatedDests = new ArrayList<>();
        for (final TextField dest : dests) {
            final String destA
        }
    }
    
    public SearchScreen() {
        setupGrid();
        
        origin = addLabeledInputField("Origin");
        rowIndex++;
        
        startDate = addLabeledInputField("Start Date");
        rowIndex++;
        
        dest = addLabeledInputField("Destination");
        dests.add(dest);
        rowIndex++;
        
        numDests = addButtonedInputField("Number of Destinations", event -> makeMoreDests());
        
        numPeople = addLabeledInputField("Number of People");
        
        budget = addLabeledInputField("Budget");
        
        searchBtn = addButton("Search", 0, event -> SCREENS.load(ItineraryScreen.class));
        rowIndex++;
        
        backBtn = addButton("Back", 0, event -> SCREENS.load(WelcomeScreen.class));
    }
    
    @Override
    public Pane getPane() {
        return grid;
    }
    
}
