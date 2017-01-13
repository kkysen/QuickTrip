package io.github.kkysen.quicktrip.misc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class Misc {
    
    public static void main(final String[] args) throws IOException {
        final Map<String, List<String>> airportsByType = //
                Files.lines(Paths.get("src", "main", "resources", "airport-codes.csv"))
                        .map(line -> line.substring(line.indexOf(',') + 1))
                        .filter(line -> line.startsWith("small")
                                || line.startsWith("medium")
                                || line.startsWith("large"))
                        .collect(Collectors.groupingByConcurrent(
                                        line -> line.substring(0, line.indexOf('_'))));
        airportsByType.values().forEach(airports -> {
            airports.replaceAll(line -> line.substring(line.indexOf(',') + 1));
        });
        airportsByType.get("large").stream().filter(line -> line.contains(",JFK,")).forEach(System.out::println);
    }
    
}
