package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.json.Json;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Getter
@Setter
public class SearchArgs {
    
    private transient TextField originInput;
    private transient DatePicker dateInput;
    private transient List<DestField> dest
    
    private String origin;
    private LocalDate date; // FIXME turn into joda Date
    private List<NoDateDestination> destinations;
    private int budget;
    private int numPeople;
    
    public void serialize() throws InputError {
        origin = serializeOrigin();
        date = serializeStartDate();
        destinations = serializeDests();
        numPeople = serializeNumPeople();
        budget = serializeBudget();
        final String json = QuickTripConstants.GSON.toJson(this);
        try {
            MyFiles.write(Paths.get(QuickTripConstants.SEARCH_ARGS_PATH), json);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
