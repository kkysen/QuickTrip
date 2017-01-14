package io.github.kkysen.quicktrip.data.airports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Getter
@RequiredArgsConstructor
public class Airport {
    
    /**
     * 
     * 
     * @author Khyber Sen
     */
    @Deprecated
    public static enum Type {
        SMALL,
        MEDIUM,
        LARGE
    }
        
    private final String name;
    private final String latitude;
    private final String longitude;
    private final String elevation;
    private final String continent;
    private final String country;
    private final String region;
    private final String municipality;
    private final String gpsCode;
    private final String iataCode;
    private final String localCode;
    
    public Airport(final String[] splitCsv) {
        this(splitCsv[0], splitCsv[1], splitCsv[2], splitCsv[3], splitCsv[4], splitCsv[5],
                splitCsv[6], splitCsv[7], splitCsv[8], splitCsv[9], splitCsv[10]);
    }
    
    private static String[] splitQuotedCsv(final String csv) {
        final List<String> parts = new ArrayList<>();
        int lastIndex = 0;
        boolean inQuote = false;
        for (int i = 0; i < csv.length(); i++) {
            final char c = csv.charAt(i);
            if (c == '"') {
                inQuote = !inQuote;
            } else {
                if (!inQuote && c == ',') {
                    parts.add(csv.substring(lastIndex, i));
                    lastIndex = i + 1;
                }
            }
        }
        parts.add(csv.substring(lastIndex));
        return parts.toArray(new String[parts.size()]);
    }
    
    public Airport(final String csvLine) {
        this(splitQuotedCsv(csvLine));
    }
    
    public static Airport fromIataCode(final String iataCode) {
        if (!iataCode.matches("[A-Z][A-Z][A-Z]")) {
            throw new IllegalArgumentException();
        }
        return new Airport(",,,,,,,,," + iataCode + ",");
    }
    
    public static void main(final String[] args) {
        final String s = "\"\"\"Der Dingel\"\" Airfield\",51.536,9.3805,718,EU,DE,DE-HE,Hofgeismar,,,";
        System.out.println(splitQuotedCsv(s).length);
        System.out.println(Arrays.toString(splitQuotedCsv(s)));
    } 
    
    
}
