package io.github.kkysen.quicktrip.data.airports;

import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Airports {
    
    private static String removeFirstCsv(final String csv) {
        return csv.substring(csv.indexOf(',') + 1);
    }
    
    public static void filter() throws IOException {
        final Path dir = Paths.get("src", "main", "resources", "airports");
        final Map<String, List<String>> airportsByType = //
                Files.lines(dir.resolve("airport-codes.csv"))
                        .map(Airports::removeFirstCsv)
                        .filter(line -> line.startsWith("small")
                                || line.startsWith("medium")
                                || line.startsWith("large"))
                        .collect(Collectors.groupingByConcurrent(
                                line -> line.substring(0, line.indexOf('_'))));
        airportsByType.values().forEach(airports -> {
            airports.replaceAll(Airports::removeFirstCsv);
            airports.sort(null);
        });
        airportsByType.forEach((type, airports) -> {
            try {
                MyFiles.write(dir.resolve(type + "Airports.csv"), airports);
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        airportsByType.get("large").stream().filter(line -> line.contains(",US,"))
                .forEach(System.out::println);
    }
    
    public static void main(final String[] args) throws IOException {
        filter();
    }
    
}
