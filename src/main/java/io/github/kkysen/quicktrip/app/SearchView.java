package io.github.kkysen.quicktrip.app;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

//TODO dynamic pane edit to fxgraph

/**
 * 
 * 
 * @author Khyber Sen
 */
@Getter
public class SearchView {
    
    @FXML private GridPane grid = new GridPane();
    private final GridRows rows = new GridRows(grid);
    
    @FXML private TextField origin;
    @FXML private DatePicker startDate;
    private DestField dest;
    @FXML private Button moreDestsBtn;
    @FXML private WholeNumberField numDests;
    private final List<DestField> destFields = new ArrayList<>();
    @FXML private WholeNumberField numPeople;
    @FXML private WholeNumberField budget;
    @FXML private Button searchBtn;
    @FXML private Button backBtn;
    @FXML private Button resetBtn;
    @FXML private Button lastSearchBtn;
    
    private FXMLLoader loader;
    
    //@FXML private final String now = LocalDate.now().toString();
    
    /**
     * model and view
     * 
     * @author Khyber Sen
     */
    public class DestField implements Model, Nodes {
        
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
        
        @Override
        public Node[] toNodeArray() {
            return new Node[] {label, address, numDays};
        }
        
        /**
         * makes an error dialog if the address does not exist
         * @throws EmptyInputError if there's no address
         * 
         * @throws InputError if the address doesn't exist
         */
        @Validation
        private boolean validateAddress() throws AddressInputError, EmptyInputError {
            return AddressInputError.validate(address.getText());
        }
        
        @Validation
        private boolean validateNumDays() throws WholeNumberInputError, EmptyInputError {
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
    
    private Button createButton(final String name, final int columnIndex) {
        final Button btn = new Button(name);
        return btn;
    }
    
    /**
     * Removes all {@link DestField}s, then adds in the specified amount
     * 
     * @param numDests The number of total destinations
     * */
    public void setNumDestinations(final int numDests) {
//        final int numToAdd = numDests - destFields.size();
//        if (numToAdd == 0) {
//            return;
//        }
//        final boolean adding = numToAdd > 0;
//        
//        final DestField lastDest = destFields.get(destFields.size() - 1);
//        final int fromRowIndex = GridPane.getRowIndex(lastDest.address);
//        if (adding) {
//            for (int i = 0; i < numToAdd; i++) {
//                rows.addNodes(i + fromRowIndex, destFields.get(i));
//            }
//        } else {
//            final int numToRemove = - numToAdd;
//            rows.removeRange(fromRowIndex - numToRemove, fromRowIndex);
//        }
        
        //      final int numToRemove = destFields.size() - numDests;
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
            //final List<Node[]> destFieldNodes = new ArrayList<>(numDests);
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
    	origin = new TextField();
        moreDestsBtn = new Button();
        numDests = new WholeNumberField();
        numPeople = new WholeNumberField();
        budget = new WholeNumberField();
        searchBtn = new Button();
        backBtn = new Button();
        resetBtn = new Button();
        lastSearchBtn = new Button();
    	
        //rows.clear();
        
        try {
			grid = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        ((DatePicker)grid.lookup("#startDate")).setValue(LocalDate.now());
        ((WholeNumberField)grid.lookup("#numDests")).setMax(23);
        ((WholeNumberField)grid.lookup("#numPeople")).setMax(Long.MAX_VALUE);
        ((WholeNumberField)grid.lookup("#budget")).setMax(Long.MAX_VALUE);
        
        //origin = addLabeledInputField("Origin");
        
        //        startDate = addLabeledInputField("Start Date");
        //        rowIndex++;
        //final Label startDateLabel = new Label("Start Date");
        //startDate = new DatePicker(LocalDate.now());
        //rows.add(startDateLabel, startDate);
        
        dest = new DestField(0);
        grid.addRow(2, dest.toNodeArray());
        //dest.addToGrid();
        
        /*moreDestsBtn = new Button("Number of Destinations");
        numDests = new WholeNumberField(23);
        rows.add(moreDestsBtn, numDests);*/
        
        //numDests = addButtonedInputField("Number of Destinations", event -> makeMoreDests());
        
        /*numPeople = addWholeNumberField("Number of People");
        
        budget = addWholeNumberField("Budget");
        
        searchBtn = new Button("Search");
        rows.add(searchBtn);
        
        resetBtn = new Button("Reset");
        rows.add(resetBtn);
        
        backBtn = new Button("Back");
        rows.add(backBtn);
        
        lastSearchBtn = new Button("Last Search");
        rows.add(lastSearchBtn);*/
        
        
    }
    
    public SearchView() {
    	loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("view/SearchScreenView.fxml"));
    	loader.setController(SearchController.class);
    	
    	/*try {
			grid = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    	
    	
        //setupGrid();
        reset();
    }
    
}
