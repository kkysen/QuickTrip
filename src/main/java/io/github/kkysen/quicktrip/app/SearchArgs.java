package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.Json;

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
    private String date; // FIXME turn into joda Date
    private List<Destination> destinations;
    private int budget;
    private int numPeople;
    
}
