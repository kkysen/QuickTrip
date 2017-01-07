package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.app.SearchScreen.DestField;
import io.github.kkysen.quicktrip.io.MyFiles;
import io.github.kkysen.quicktrip.json.Json;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
public class SearchArgs {
    
    private static final Path PATH = Paths.get("searchArgs.json");
    
    private transient TextField originInput;
    private transient DatePicker dateInput;
    private transient WholeNumberField numDestinations;
    private transient List<DestField> destinationInputs;
    private transient WholeNumberField numPeopleInput;
    private transient WholeNumberField budgetInput;
    
    private String origin;
    private LocalDate date;
    private List<NoDateDestination> destinations;
    private int budget;
    private int numPeople;
    
    public SearchArgs(final String origin, final LocalDate date,
            final List<NoDateDestination> destinations, final int budget, final int numPeople) {
        this.origin = origin;
        this.date = date;
        this.destinations = destinations;
        this.budget = budget;
        this.numPeople = numPeople;
    }
    
    public void serialize() {
        final String json = QuickTripConstants.GSON.toJson(this);
        try {
            MyFiles.write(PATH, json);
        } catch (final IOException e) {
            throw new RuntimeException(e); // shouldn't happen
        }
    }
    
    public static SearchArgs deserialize() {
        Reader reader;
        try {
            reader = Files.newBufferedReader(PATH, Constants.CHARSET);
        } catch (final IOException e) {
            throw new RuntimeException(e); // shouldn't happen
        }
        return QuickTripConstants.GSON.fromJson(reader, SearchArgs.class);
    }
    
}
