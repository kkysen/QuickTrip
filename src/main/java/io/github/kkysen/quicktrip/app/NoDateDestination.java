package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.apis.Json;

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
public class NoDateDestination {
    
    private String address;
    private int numDays;
    
}
