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
    
    private static String removeFirstCsv(final String csv) {
        return csv.substring(csv.indexOf(',') + 1);
    }
    
    public static void filter() throws IOException {
        final List<String> airports = Files.lines(ORIGINAL_PATH)
                .map(Airports::removeFirstCsv)
                .filter(line -> line.startsWith("small")
                        || line.startsWith("medium")
                        || line.startsWith("large"))
                .map(line -> {
                    final int startIndex = line.indexOf('_');
                    final int endIndex = line.indexOf(',');
                    return line.substring(0, startIndex) + line.substring(endIndex);
                })
                .sorted()
                .collect(Collectors.toList());
        MyFiles.write(DIR.resolve("airports.csv"), airports);
    }
    
    private final Map<String, List<Airport>> airportsByCountry;
    
    public Airports() throws IOException {
        airportsByCountry = Files.lines(PATH, Constants.CHARSET)
                .map(Airport::new)
                .collect(Collectors.groupingBy(Airport::getCountry));
    }
    
    /**
     * @param location geolocation to search from
     * @param radius radius in meters
     * 
     * @return a Stream of Airports within the radius, sorted by prominence
     *         (largeness) and distance
     */
    public Stream<Airport> inRadius(final Geolocation location, final int radius) {
        final LatLng latLng = location.getLocation();
        return airportsByCountry.get(location.getCountry())
                .stream()
                .filter(airport -> latLng.approximateInRadius(airport.getLocation(), radius))
                .filter(airport -> {
                    final double distanceTo = latLng.distanceTo(airport.getLocation());
                    airport.setDistanceTo(distanceTo);
                    return distanceTo < radius;
                })
                .sorted((airport1, airport2) -> {
                    return (int) (airport1.getDistanceTo() - airport2.getDistanceTo());
                });
    }
    
}
