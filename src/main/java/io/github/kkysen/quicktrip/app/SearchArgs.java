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
    
    private String origin;
    private LocalDate date; // FIXME turn into joda Date
    private List<NoDateDestination> destinations;
    private int budget;
    private int numPeople;
    
}
