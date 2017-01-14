package io.github.kkysen.quicktrip.data.airports;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.data.airports.Airport.Type;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Deprecated
public class Airports {
    
    private static final Path DIR = Paths.get("src", "main", "resources", "airports");
    
    private static String removeFirstCsv(final String csv) {
        return csv.substring(csv.indexOf(',') + 1);
    }
    
    public static void filter() throws IOException {
        final Map<String, List<String>> airportsByType = //
                Files.lines(DIR.resolve("airport-codes.csv"))
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
                MyFiles.write(DIR.resolve(type + "Airports.csv"), airports);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        airportsByType.get("large").stream().filter(line -> line.contains(",US,"))
                .forEach(System.out::println);
    }
    
    private static final List<Type> TYPES = new ArrayList<>(
            Arrays.asList(Type.values()));
    private static final List<Function<Airport, String>> CLASSIFIERS = new ArrayList<>();
    static {
        CLASSIFIERS.add(Airport::getName);
        CLASSIFIERS.add(Airport::getLatitude);
        CLASSIFIERS.add(Airport::getLongitude);
        CLASSIFIERS.add(Airport::getElevation);
        CLASSIFIERS.add(Airport::getContinent);
        CLASSIFIERS.add(Airport::getCountry);
        CLASSIFIERS.add(Airport::getRegion);
        CLASSIFIERS.add(Airport::getMunicipality);
        CLASSIFIERS.add(Airport::getGpsCode);
        CLASSIFIERS.add(Airport::getIataCode);
        CLASSIFIERS.add(Airport::getLocalCode);
    }
    
    private final Map<Type, Map<Function<Airport, String>, Map<String, List<Airport>>>> airports;
    
    private static Map<Function<Airport, String>, Map<String, List<Airport>>> loadAirports(
            final Path path) throws IOException {
        final List<Airport> airports = Files.lines(path, Constants.CHARSET)
                //.peek(System.out::println)
                .map(Airport::new)
                .collect(Collectors.toList());
        final Map<Function<Airport, String>, Map<String, List<Airport>>> classifiedAirports = new HashMap<>();
        CLASSIFIERS.stream()
                .forEach(classifier -> {
                    final Map<String, List<Airport>> classification = airports.stream()
                            .collect(Collectors.groupingBy(classifier));
                    classifiedAirports.put(classifier, classification);
                });
        return classifiedAirports;
    }
    
    public Airports() {
        airports = new ConcurrentHashMap<>(TYPES.size());
        TYPES.parallelStream()
                .forEach(type -> {
                    final Path path = DIR.resolve(type.toString().toLowerCase() + "Airports.csv");
                    Map<Function<Airport, String>, Map<String, List<Airport>>> loadedAirports;
                    try {
                        loadedAirports = loadAirports(path);
                    } catch (final IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    airports.put(type, loadedAirports);
                });
    }
    
    @Override
    public String toString() {
        return airports.toString();
    }
    
    public static void main(final String[] args) throws IOException {
        final Airports airports = new Airports();
        System.out.println(airports);
    }
    
}
