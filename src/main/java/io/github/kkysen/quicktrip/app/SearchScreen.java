package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.google.geocoding.exists.AddressExistsRequest;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
    private final GridRows rows = new GridRows(grid);
    
    private TextField origin;
    private DatePicker startDate;
    private DestField dest;
    private WholeNumberField numDests;
    private final List<DestField> destFields = new ArrayList<>();
    private WholeNumberField numPeople;
    private WholeNumberField budget;
    private Button searchBtn;
    private Button backBtn;
    private Button resetBtn;
    private Button lastSearchBtn;
    
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
        private final WholeNumberField numDays;
        
        public DestField(final int destNum) {
            this.destNum = destNum;
            
            String labelText = "Destination";
            // if destNum = 0, don't add destNum to label
            if (destNum != 0) {
                labelText += " " + destNum;
            }
            
            label = new Label(labelText);
            
            address = new TextField();
            
            numDays = new WholeNumberField(365);
            
            destFields.add(this);
        }
        
        public void addToGrid() {
            rows.add(label, address, numDays);
        }
        
        public Node[] toNodeArray() {
            return new Node[] {label, address, numDays};
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
         * @return Json Pojo NoDateDestination for serialization
         */
        public NoDateDestination serialize() {
            return new NoDateDestination(address.getText(), Integer.parseInt(numDays.getText()));
        }

        @Override
        public String toString() {
            return "DestField [destNum=" + destNum + ", label=" + label + ", address=" + address
                    + ", numDays=" + numDays + "]";
        }
        
    }
    
    private void setupGrid() {
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
    }
    
    private TextField addLabeledInputField(final String name) {
        final Label label = new Label(name);
        final TextField text = new TextField();
        rows.add(label, text);
        return text;
    }
    
    private Button createButton(final String name, final int columnIndex,
            final EventHandler<ActionEvent> onAction) {
        final Button btn = new Button(name);
        btn.setOnAction(onAction);
        return btn;
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
        
        if (numDests < 1 || numDests > 100) {
            numDestsError();
            return;
        }
                
        if (numDests == destFields.size()) {
            return; // nothing changes
        }
        
        //        final int numToRemove = destFields.size() - numDests;
        //        if (numToRemove > 0) {
        //            final int fromRowIndex = GridPane
        //                    .getRowIndex(destFields.get(destFields.size() - numToRemove).address);
        //            rows.removeRange(fromRowIndex, fromRowIndex + numToRemove);
        //            if (destFields.size() == 1) {
        //                destFields.set(0, dest);
        //                rows.add(fromRowIndex, dest.toNodeArray());
        //            }
        //            return;
        //        }
        //        
        //        final DestField lastDest = destFields.get(destFields.size() - 1);
        //        final int lastDestNum = lastDest.destNum;
        //        final int fromRowIndex = GridPane.getRowIndex(lastDest.address);
        //        int numToAdd = -numToRemove;
        //        if (destFields.size() == 1) {
        //            destFields.remove(0);
        //            numToAdd++;
        //        }
        //        final List<Node[]> destFieldsToAdd = new ArrayList<>(numToAdd);
        //        for (int i = 0; i < numToAdd; i++) {
        //            destFieldsToAdd.add(new DestField(i + lastDestNum).toNodeArray());
        //        }
        //        rows.addAll(fromRowIndex, destFieldsToAdd);
        
        // simplified but slower
        final int fromRowIndex = GridPane.getRowIndex(destFields.get(0).address);
        rows.removeRange(fromRowIndex, fromRowIndex + destFields.size());
        destFields.clear();
        if (numDests == 1) {
            rows.add(fromRowIndex, dest.toNodeArray());
            destFields.add(dest);
        } else {
            final List<Node[]> destFieldNodes = new ArrayList<>(numDests);
            for (int i = 0; i < numDests; i++) {
                rows.add(fromRowIndex + i, new DestField(i + 1).toNodeArray());
                //destFieldNodes.add(new DestField(i + 1).toNodeArray());
            }
            //rows.addAll(fromRowIndex, destFieldNodes);
        }
    }
    
    private void alert(final Object o) {
        final Alert alert = new Alert(AlertType.ERROR, o.toString());
        alert.setResizable(true);
        alert.showAndWait();
    }
    
    private void alert() {
        alert("");
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
    
    private LocalDate serializeStartDate() {
        // FIXME
        //return startDate.getText();
        return startDate.getValue();
    }
    
    private List<NoDateDestination> serializeDests() throws InputError {
        for (final DestField destField : destFields) {
            destField.validate();
        }
        final List<NoDateDestination> serializedDests = new ArrayList<>();
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
        final LocalDate startDate = serializeStartDate();
        final List<NoDateDestination> dests = serializeDests();
        final int numPeople = serializeNumPeople();
        final int budget = serializeBudget();
        final SearchArgs searchArgs = new SearchArgs(origin, startDate, dests, budget, numPeople);
        final String json = QuickTripConstants.GSON.toJson(searchArgs);
        try {
            MyFiles.write(Paths.get(QuickTripConstants.SEARCH_ARGS_PATH), json);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void loadItineraryScreen() {
        final ItineraryScreenController itineraryScreen = //
                (ItineraryScreenController) QuickTrip.SCREENS.get(ItineraryScreenController.class);
        itineraryScreen.load();
        // when ItineraryScreen is finished loading, switch to it
        QuickTrip.SCREENS.load(ItineraryScreenController.class);
    }
    
    public void noSerializeSearch() {
        // switch to SearchingScreen while ItineraryScreen loads
        QuickTrip.SCREENS.load(SearchingScreen.class);
        new Thread(this::loadItineraryScreen).run();
    }
    
    public void search() {
        try {
            serializeSearchArgs();
        } catch (final InputError e) {
            e.getErrorDialog().showAndWait();
            return; // stop serialization
        }
        noSerializeSearch();
    }
    
    private WholeNumberField addWholeNumberField(final String name, final long max) {
        final Label label = new Label(name);
        final WholeNumberField input = new WholeNumberField(max);
        rows.add(label, input);
        return input;
    }
    
    private WholeNumberField addWholeNumberField(final String name) {
        return addWholeNumberField(name, Long.MAX_VALUE);
    }
    
    public void reset() {
        rows.clear();
        
        origin = addLabeledInputField("Origin");
        
        //        startDate = addLabeledInputField("Start Date");
        //        rowIndex++;
        final Label startDateLabel = new Label("Start Date");
        startDate = new DatePicker(LocalDate.now());
        rows.add(startDateLabel, startDate);
        
        dest = new DestField(0);
        dest.addToGrid();
        
        final Button moreDestsBtn = new Button("Number of Destinations");
        moreDestsBtn.setOnAction(event -> makeMoreDests());
        numDests = new WholeNumberField(23);
        rows.add(moreDestsBtn, numDests);
        
        //numDests = addButtonedInputField("Number of Destinations", event -> makeMoreDests());
        
        numPeople = addWholeNumberField("Number of People");
        
        budget = addWholeNumberField("Budget");
        
        searchBtn = createButton("Search", 0, event -> search());
        rows.add(searchBtn);
        
        resetBtn = createButton("Reset", 0, event -> reset());
        rows.add(resetBtn);
        
        backBtn = createButton("Back", 0, event -> {
            QuickTrip.SCREENS.load(WelcomeScreen.class);
            reset();
        });
        rows.add(backBtn);
        
        lastSearchBtn = createButton("Last Search", 0, event -> noSerializeSearch());
        rows.add(lastSearchBtn);
    }
    
    public SearchScreen() {
        setupGrid();
        reset();
    }
    
    @Override
    public Pane getPane() {
        return grid;
    }
    
}
