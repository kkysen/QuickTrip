package io.github.kkysen.quicktrip.app;

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
    
    /**
     * 
     * 
     * @author Khyber Sen
     */
    public class DestField implements Model {
        
        private static final int MAX_NUM_DAYS = 365;
        
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
            numDays = new WholeNumberField(MAX_NUM_DAYS);
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
         * @throws InputError if the address doesn't exist
         */
        @Validation
        private boolean validateAddress() throws AddressInputError {
            return AddressInputError.validate(address.getText());
        }
        
        @Validation
        private boolean validateNumDays() throws WholeNumberInputError {
            WholeNumberInputError.validate(numDays.getText(), "Number of Days", MAX_NUM_DAYS);
            return true;
        }
        
        /**
         * serializes this into a Json Pojo
         * should be called after {@link #validate()}
         * 
         * @see #validate()
         * 
         * @return Json Pojo NoDateDestination for serialization
         */
        public NoDateDestination toNoDateDestination() {
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
    
    private void serializeSearchArgs() throws InputError {
        final SearchArgs searchArgs = new SearchArgs(
                origin.getText(),
                startDate.getValue(),
                String.valueOf(destFields.size()), // FIXME
                destFields,
                numPeople.getText(),
                budget.getText()
        );
        searchArgs.validate();
        searchArgs.serialize();
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
