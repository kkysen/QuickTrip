package io.github.kkysen.quicktrip.csv;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * TODO sort things by existing index order, option to not include query string
 * in
 * result list
 * 
 * TODO also find closest airport to a given set of coords
 */

// @AllArgsConstructor
public class CSVSearcher /*implements Iterable<List<String>>*/ {
    /*private final Path path;
    private final int targetIndex;
    private final String targetValue;*/
    
    /*@Override
    public Iterator<List<String>> iterator() {
    	return new CSVIterator(path, StandardCharsets.UTF_8);
    }*/
    
    private static Iterator<List<String>> getIterator(final Path path) {
        return new CSVIterator(path, StandardCharsets.UTF_8);
    }
    
    /**
     * Search through a CSV file and get all rows containing a certain bit of
     * data
     * 
     * @param path The file path of the CSV
     * @param targetIndex The index of the value to search
     * @param targetValue The value to match
     * @param otherIndices Any other data that should be added from each
     *            matching line
     *            of the CSV
     * @return A <code>List</code> of <code>String[]</code> containing all
     *         matching values
     */
    public static List<String[]> findAll(final Path path,
            final int targetIndex,
            final String targetValue,
            final int... otherIndices) {
        final List<String[]> result = new ArrayList<>();
        final Iterator<List<String>> it = getIterator(path);
        
        List<String> line;
        String[] temp;
        
        int hitIndex = -1;
        while (it.hasNext()) {
            temp = new String[11];
            
            line = it.next();
            if ((hitIndex = line.indexOf(targetValue)) != -1) {
                temp[hitIndex] = line.get(hitIndex);
                
                //Add the other optional data
                for (final int data : otherIndices) {
                    temp[data] = line.get(data);
                }
                
                result.add(temp);
            }
        }
        
        return result;
    }
    
    public static List<List<String>> findRadius(final Path path,
            final double lat,
            final double lon,
            final double radius,
            final int... otherIndices) {
        return null;
    }
    
    public static void main(final String[] args) {
        final Path path = Paths.get(
                "C:\\Users\\Stanley\\Documents\\GitHub\\QuickTrip\\src\\main\\resources\\airports\\largeAirports.csv");
        
        final List<String[]> l = CSVSearcher.findAll(path, -1, "New York", 0, 1, 2);
        
        for (final String[] s : l) {
            for (final String t : s) {
                System.out.print(t + " ");
            }
            System.out.println();
        }
    }
}
