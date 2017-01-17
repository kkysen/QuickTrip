package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.app.SearchView.DestField;
import io.github.kkysen.quicktrip.app.data.NoDateDestination;
import io.github.kkysen.quicktrip.app.input.AddressInputError;
import io.github.kkysen.quicktrip.app.input.EmptyInputError;
import io.github.kkysen.quicktrip.app.input.InputError;
import io.github.kkysen.quicktrip.app.input.Model;
import io.github.kkysen.quicktrip.app.input.Validation;
import io.github.kkysen.quicktrip.app.input.WholeNumberInputError;
import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.reflect.Reflect;

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
public class SearchModel implements Model {
    
    private transient @Setter @Validation String originInput;
    private transient @Setter @Validation LocalDate startDateInput;
    private transient @Setter @Validation String numDestinationsInput;
    private transient @Setter @Validation List<DestField> destinationInputs;
    private transient @Setter @Validation String numPeopleInput;
    private transient @Setter @Validation String budgetInput;
    
    private transient @Getter int numDestinations;
    
    private @Getter Geolocation origin;
    private @Getter LocalDate startDate;
    private @Getter List<NoDateDestination> destinations;
    private @Getter long budget;
    private @Getter int numPeople;
    
    public SearchModel(final String originInput, final LocalDate startDateInput,
            final String numDestinationsInput, final List<DestField> destinationInputs,
            final String numPeopleInput, final String budgetInput) {
        this.originInput = originInput;
        this.startDateInput = startDateInput;
        this.numDestinationsInput = numDestinationsInput;
        this.destinationInputs = destinationInputs;
        this.numPeopleInput = numPeopleInput;
        this.budgetInput = budgetInput;
    }
    
    public SearchModel(final Geolocation origin, final LocalDate date, final int numDestinations,
            final List<NoDateDestination> destinations, final int budget, final int numPeople) {
        this.origin = origin;
        this.startDate = date;
        this.numDestinations = numDestinations;
        this.destinations = destinations;
        this.budget = budget;
        this.numPeople = numPeople;
    }
    
    @Validation
    public boolean validateOrigin() throws AddressInputError, EmptyInputError {
        origin = AddressInputError.validate(originInput);
        return true;
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
    
    //@Validation
    public boolean validateNumDestinations() throws WholeNumberInputError, EmptyInputError {
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
    public boolean validateNumPeople() throws WholeNumberInputError, EmptyInputError {
        numPeople = (int) WholeNumberInputError.validate(numPeopleInput, "Number of People",
                Integer.MAX_VALUE);
        return true;
    }
    
    @Validation
    public boolean validateBudget() throws InputError {
        budget = WholeNumberInputError.validate(budgetInput, "Budget", Long.MAX_VALUE);
        return true;
    }
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
}
