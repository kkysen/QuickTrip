package io.github.kkysen.quicktrip.data.airports;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class AirportsByName {
    
    private static final Path DIR = Paths.get("src", "main", "resources", "airports");
    private static final Path ORIGINAL_PATH = DIR.resolve("airport-codes.csv");
    private static final Path PATH = DIR.resolve("airports.csv");
    
    private static String removeFirstCsv(final String csv) {
        return csv.substring(csv.indexOf(',') + 1);
    }
    
    public static void filter() throws IOException {
        final List<String> airports = Files.lines(ORIGINAL_PATH)
                .map(AirportsByName::removeFirstCsv)
                .filter(line -> line.startsWith("small")
                        || line.startsWith("medium")
                        || line.startsWith("large"))
                .map(AirportsByName::removeFirstCsv)
                .sorted()
                .collect(Collectors.toList());
        MyFiles.write(DIR.resolve("airports.csv"), airports);
    }
    
    private final Map<String, Airport> airportsByName;
    
    public AirportsByName() throws IOException {
        airportsByName = Files.lines(PATH, Constants.CHARSET)
                .map(Airport::new)
                .collect(Collectors.toMap(Airport::getName, Function.identity()));
    }
    
    public Airport get(final String name) {
        return airportsByName.get(name);
    }
    
    public static void main(final String[] args) throws IOException {
        filter();
    }
    
}
