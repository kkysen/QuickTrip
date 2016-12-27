package io.github.kkysen.quicktrip.app;

import static io.github.kkysen.quicktrip.app.QuickTrip.SCREENS;

import io.github.kkysen.quicktrip.apis.google.geocoding.exists.AddressExistsRequest;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private final DestField dest;
    private final TextField numDests;
    private final List<DestField> destFields = new ArrayList<>();
    private final TextField numPeople;
    private final TextField budget;
    private final Button searchBtn;
    private final Button backBtn;
    
    private static String quote(final Object o) {
        return '"' + o.toString() + '"';
    }
    
    private void nonExistentAddressError(final String address) throws InputError {
        final String error = "Nonexistent Address";
        final String msg = quote(address) + " does not exist.";
        throw new InputError(error, msg);
    }
    
    private void validateAddress(final TextField address) throws InputError {
        final String addressText = address.getText();
        if (addressText.isEmpty()) {
            throw new InputError("No Address", "Please enter an address");
        }
        try {
            if (!AddressExistsRequest.exists(addressText)) {
                nonExistentAddressError(addressText);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 
     * 
     * @author Khyber Sen
     */
    private class DestField {
        
        private final int destNum;
        private final Label label;
        private final TextField address;
        private final TextField numDays;
        
        private void addToGrid() {
            grid.add(label, 0, rowIndex);
            grid.add(address, 1, rowIndex);
            grid.add(numDays, 2, rowIndex);
            rowIndex++;
        }
        
        public DestField(final int destNum) {
            this.destNum = destNum;
            
            String labelText = "Destination";
            // if destNum = 0, don't add destNum to label
            if (destNum != 0) {
                labelText += " " + destNum;
            }
            
            label = new Label(labelText);
            
            address = new TextField();
            
            numDays = new TextField();
            
            destFields.add(this);
            addToGrid();
        }
        
        /**
         * makes an error dialog if the address does not exist
         * 
         * @throws InputError
         */
        private void validateAddress() throws InputError {
            SearchScreen.this.validateAddress(address);
        }
        
        private void invalidNumDaysError(final String numDays) throws InputError {
            final String error = "Invalid Number of Days";
            final String msg = quote(numDays) + " is not a valid number of days. "
                    + "It must be a whole number.";
            throw new InputError(error, msg);
        }
        
        private void validateNumDays() throws InputError {
            final String numDaysText = numDays.getText();
            int numDays;
            try {
                numDays = Integer.parseInt(numDaysText);
            } catch (final NumberFormatException e) {
                invalidNumDaysError(numDaysText);
                return;
            }
            if (numDays < 1) {
                invalidNumDaysError(numDaysText);
            }
        }
        
        /**
         * validates input (i.e. error dialog if invalid)
         * should be called before {@link #serialize()}
         * 
         * @throws InputError
         * 
         * @see #serialize()
         */
        public void validate() throws InputError {
            validateAddress();
            validateNumDays();
        }
        
        /**
         * serializes this into a Json Pojo
         * should be called after {@link #validate()}
         * 
         * @see #validate()
         * 
         * @return Json Pojo Destination for serialization
         */
        public Destination serialize() {
            return new Destination(address.getText(), Integer.parseInt(numDays.getText()));
        }
        
    }
    
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
            destFields.add(new DestField(destNum + 1));
        }
    }
    
    private void numDestsError() throws InputError {
        final String error = "Invalid Number of Destinations";
        final String msg = "You must enter a whole number less than or equal to 100";
        throw new InputError(error, msg);
    }
    
    private void tryMakeMoreDests() throws InputError {
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
            if (destFields.size() == 1) {
                return; // nothing changes
            }
            destFields.remove(0);
            grid.getChildren().removeAll(destFields);
            destFields.add(dest);
        }
        
        if (numDests < 1 || numDests > 100) {
            numDestsError();
            return;
        }
        
        final int oldRowIndex = rowIndex;
        rowIndex = GridPane.getRowIndex(destFields.get(0).address);
        final int numRowsAfterDests = oldRowIndex - rowIndex;
        
        grid.getChildren().remove(dest);
        destFields.clear();
        addDests(numDests);
        
        rowIndex += numRowsAfterDests;
    }
    
    private void makeMoreDests() {
        try {
            tryMakeMoreDests();
        } catch (final InputError e) {
            e.getErrorDialog().showAndWait();
            return;
        }
    }
    
    private String serializeOrigin() throws InputError {
        validateAddress(origin);
        return origin.getText();
    }
    
    private String serializeStartDate() {
        // FIXME
        return startDate.getText();
    }
    
    private List<Destination> serializeDests() throws InputError {
        for (final DestField destField : destFields) {
            destField.validate();
        }
        final List<Destination> serializedDests = new ArrayList<>();
        destFields.forEach(dest -> serializedDests.add(dest.serialize()));
        return serializedDests;
    }
    
    private void invalidNumPeopleError() throws InputError {
        final String error = "Invalid Number of People";
        final String msg = "You must enter a whole number.";
        throw new InputError(error, msg);
    }
    
    private int serializeNumPeople() throws InputError {
        int numPeople = 0;
        try {
            numPeople = Integer.parseInt(this.numPeople.getText());
        } catch (final NumberFormatException e) {
            invalidNumPeopleError();
        }
        if (numPeople < 1) {
            invalidNumPeopleError();
        }
        return numPeople;
    }
    
    private void invalidBudgetError() throws InputError {
        final String error = "Invalid Budget";
        final String msg = "You must enter a whole number.";
        throw new InputError(error, msg);
    }
    
    private int serializeBudget() throws InputError {
        int budget = 0;
        try {
            budget = Integer.parseInt(this.budget.getText());
        } catch (final NumberFormatException e) {
            invalidBudgetError();
        }
        if (budget < 1) {
            invalidBudgetError();
        }
        return budget;
    }
    
    private void serializeSearchArgs() throws InputError {
        final String origin = serializeOrigin();
        final String startDate = serializeStartDate();
        final List<Destination> dests = serializeDests();
        final int numPeople = serializeNumPeople();
        final int budget = serializeBudget();
        final SearchArgs searchArgs = new SearchArgs(origin, startDate, dests, budget, numPeople);
        final String json = new Gson().toJson(searchArgs);
        try {
            MyFiles.write(Paths.get(QuickTrip.SEARCH_ARGS_PATH), json);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void search() {
        try {
            serializeSearchArgs();
        } catch (final InputError e) {
            e.getErrorDialog().showAndWait();
            return; // stop serialization
        }
        // switch to SearchingScreen while ItineraryScreen loads
        SCREENS.load(SearchingScreen.class);
        final ItineraryScreen itineraryScreen = (ItineraryScreen) SCREENS
                .get(ItineraryScreen.class);
        itineraryScreen.load();
        // when ItineraryScreen is finished loading, switch to it
        SCREENS.load(ItineraryScreen.class);
    }
    
    public SearchScreen() {
        setupGrid();
        
        origin = addLabeledInputField("Origin");
        rowIndex++;
        
        startDate = addLabeledInputField("Start Date");
        rowIndex++;
        
        dest = new DestField(0);
        rowIndex++;
        
        numDests = addButtonedInputField("Number of Destinations", event -> makeMoreDests());
        
        numPeople = addLabeledInputField("Number of People");
        
        budget = addLabeledInputField("Budget");
        
        searchBtn = addButton("Search", 0, event -> search());
        rowIndex++;
        
        backBtn = addButton("Back", 0, event -> SCREENS.load(WelcomeScreen.class));
    }
    
    @Override
    public Pane getPane() {
        return grid;
    }
    
}
