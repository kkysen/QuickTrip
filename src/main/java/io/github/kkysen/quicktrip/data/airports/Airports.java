package io.github.kkysen.quicktrip.data.airports;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.apis.google.geocoding.Geolocation;
import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Airports {
    
    private static final Path DIR = Paths.get("src", "main", "resources", "airports");
    private static final Path ORIGINAL_PATH = DIR.resolve("airport-codes.csv");
    private static final Path PATH = DIR.resolve("airports.csv");
    
    public static final int NUM_NEAR = 3;
    
    private static String removeFirstCsv(final String csv) {
        return csv.substring(csv.indexOf(',') + 1);
    }
    
    public static void filter() throws IOException {
        MyFiles.write(DIR.resolve("airports.csv"),
                Files.lines(ORIGINAL_PATH)
                        .map(Airports::removeFirstCsv)
                        .filter(line -> line.startsWith("small")
                                || line.startsWith("medium")
                                || line.startsWith("large"))
                        .map(line -> {
                            final int startIndex = line.indexOf('_');
                            final int endIndex = line.indexOf(',');
                            return line.substring(0, startIndex) + line.substring(endIndex);
                        })
                        .sorted()::iterator);
    }
    
    private final List<Airport> airports;
    private final Map<String, List<Airport>> airportsByCountry;
    
    public Airports() throws IOException {
        airports = Files.lines(PATH, Constants.CHARSET)
                .map(Airport::new)
                .filter(airport -> !airport.getIataCode().isEmpty())
                .collect(Collectors.toList());
        airportsByCountry = airports.parallelStream()
                .collect(Collectors.groupingBy(Airport::getCountry));
    }
    
    public Stream<Airport> stream() {
        return airports.stream();
    }
    
    public List<Airport> inCountry(final String country) {
        return airportsByCountry.get(country);
    }
    
    /**
     * @param latLng geolocation to search from
     * @param radius radius in kilometers
     * 
     * @return a Stream of Airports within the radius, sorted by prominence
     *         (largeness) and distance
     */
    public Stream<Airport> inRadius(final Geolocation location, final double radius) {
        final LatLng latLng = location.getLocation();
        return inCountry(location.getCountry())
                .stream()
                //.peek(System.out::println)
                .filter(airport -> latLng.approximateInRadius(airport.getLocation(), radius))
                .filter(airport -> {
                    final double distanceTo = latLng.distanceTo(airport.getLocation());
                    airport.setDistanceTo(distanceTo);
                    return distanceTo < radius;
                })
                .sorted((airport1, airport2) -> {
                    // sort by distance
                    return (int) (airport1.getDistanceTo() - airport2.getDistanceTo());
                })
                .sorted((airport1, airport2) -> {
                    // sort by prominence (type: large, medium, or small)
                    return airport1.getType().toString().compareTo(airport2.getType().toString());
                });
    }
    
    public List<Airport> near(final Geolocation location) {
        double radius = 25;
        List<Airport> airports;
        do {
            airports = inRadius(location, radius).collect(Collectors.toList());
            radius += 10;
        } while (airports.size() < NUM_NEAR);
        return airports;
    }
    
    public static void main(final String[] args) throws IOException {
        //filter();
        final Airports airports = new Airports();
//        airports.inRadius(Geolocation.createDummy(LatLng.NYC, "US"), 20)
//                .forEach(System.out::println);
        airports.near(Geolocation.createDummy(LatLng.NYC, "US")).forEach(System.out::println);
    }
    
}
