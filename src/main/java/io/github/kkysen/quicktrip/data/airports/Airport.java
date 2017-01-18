package io.github.kkysen.quicktrip.data.airports;

import io.github.kkysen.quicktrip.apis.google.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
@Getter
@ToString
public class Airport {
    
    /**
     * 
     * 
     * @author Khyber Sen
     */
    public static enum Type {
        SMALL,
        MEDIUM,
        LARGE;
        
        public static Type get(final String type) {
            return valueOf(type.toUpperCase());
        }
        
    }
    
    private final Type type;
    private final String name;
    private final LatLng location;
    private final String elevation;
    private final String continent;
    private final String country;
    private final String region;
    private final String municipality;
    private final String gpsCode;
    private final String iataCode;
    private final String localCode;
    
    private transient @Setter double distanceTo;
    
    public Airport(final String[] splitCsv) {
        this(Type.get(splitCsv[0]), splitCsv[1], new LatLng(splitCsv[2], splitCsv[3]), splitCsv[4],
                splitCsv[5], splitCsv[6], splitCsv[7], splitCsv[8], splitCsv[9], splitCsv[10],
                splitCsv[11]);
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (location == null ? 0 : location.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Airport other = (Airport) obj;
        if (location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!location.equals(other.location)) {
            return false;
        }
        return true;
    }
    
    public static void main(final String[] args) {
        final String s = "\"\"\"Der Dingel\"\" Airfield\",51.536,9.3805,718,EU,DE,DE-HE,Hofgeismar,,,";
        System.out.println(splitQuotedCsv(s).length);
        System.out.println(Arrays.toString(splitQuotedCsv(s)));
    }
    
}
