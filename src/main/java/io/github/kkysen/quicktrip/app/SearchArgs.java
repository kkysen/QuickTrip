package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.app.SearchScreen.DestField;
import io.github.kkysen.quicktrip.json.Json;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
@NoArgsConstructor
@Getter
@Setter
public class SearchArgs implements Model {
    
    private static final Path PATH = Paths.get("searchArgs.json");
    
    //    private transient TextField originInput;
    //    private transient DatePicker dateInput;
    //    private transient WholeNumberField numDestinations;
    //    private transient List<DestField> destinationInputs;
    //    private transient WholeNumberField numPeopleInput;
    //    private transient WholeNumberField budgetInput;
    
    private transient String originInput;
    private transient LocalDate startDateInput;
    private transient String numDestinationsInput;
    private transient List<DestField> destinationInputs;
    private transient String numPeopleInput;
    private transient String budgetInput;
    
    private String origin;
    private LocalDate startDate;
    private int numDestinations;
    private List<NoDateDestination> destinations;
    private long budget;
    private int numPeople;
    
    public SearchArgs(final String originInput, final LocalDate startDateInput,
            final String numDestinationsInput, final List<DestField> destinationInputs,
            final String numPeopleInput, final String budgetInput) {
        this.originInput = originInput;
        this.startDateInput = startDateInput;
        this.numDestinationsInput = numDestinationsInput;
        this.destinationInputs = destinationInputs;
        this.numPeopleInput = numPeopleInput;
        this.budgetInput = budgetInput;
    }
    
    public SearchArgs(final String origin, final LocalDate date, final int numDestinations,
            final List<NoDateDestination> destinations, final int budget, final int numPeople) {
        this.origin = origin;
        this.startDate = date;
        this.numDestinations = numDestinations;
        this.destinations = destinations;
        this.budget = budget;
        this.numPeople = numPeople;
    }
    
    @Validation
    public boolean validateOrigin() throws AddressInputError {
        final boolean validated = AddressInputError.validate(originInput);
        origin = originInput;
        return validated;
    }
    
    private static class DateInputError extends InputError {
        
        private static final long serialVersionUID = 8483271465870800912L;
        
        public DateInputError(final String error, final String msg) {
            super(error, msg);
        }
        
    }
    
    @Validation
    public boolean validateStartDate() throws InputError {
        if (startDateInput.isBefore(LocalDate.now())) {
            throw new DateInputError("Invalid Date", "Start date must be today or in the future");
        }
        this.startDate = startDateInput;
        return true;
    }
    
    @Validation
    public boolean validateNumDestinations() throws WholeNumberInputError {
        numDestinations = (int) WholeNumberInputError.validate(numDestinationsInput,
                "Number of Destinations", 23);
        return true;
    }
    
    @Validation
    public boolean validateDestinations() throws InputError {
        boolean validated = true;
        final List<NoDateDestination> destinations = new ArrayList<>();
        for (final DestField destinationInput : destinationInputs) {
            validated = validated && destinationInput.validate();
            destinations.add(destinationInput.toNoDateDestination());
        }
        this.destinations = destinations;
        return true;
    }
    
    @Validation
    public boolean validateNumPeople() throws WholeNumberInputError {
        numPeople = (int) WholeNumberInputError.validate(numPeopleInput, "Number of People",
                Integer.MAX_VALUE);
        return true;
    }
    
    @Validation
    public boolean validateBudget() throws InputError {
        budget = WholeNumberInputError.validate(budgetInput, "Budget", Long.MAX_VALUE);
        return true;
    }
    
    public void serialize() {
        try {
            serialize(PATH);
        } catch (final IOException e) {
            throw new RuntimeException(e); // shouldn't happen
        }
    }
    
    public static SearchArgs deserialize() {
        try {
            return Model.deserialize(PATH, SearchArgs.class);
        } catch (final IOException e) {
            throw new RuntimeException(e); // shouldn't happen
        }
    }
    
}
