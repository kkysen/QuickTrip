package io.github.kkysen.quicktrip.csv;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.AllArgsConstructor;

/*TODO sort things by existing index order, option to not include query string in
result list

TODO also find closest airport to a given set of coords
*/

//@AllArgsConstructor
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
	 * Search through a CSV file and get all rows containing a certain bit of data
	 * 
	 * @param path 			The file path of the CSV
	 * @param targetIndex	The index of the value to search
	 * @param targetValue	The value to match
	 * @param otherIndices	Any other data that should be added from each matching line
	 * 						of the CSV
	 * @return A thing lol containing all matching values
	 */
	public static List<List<String>> findAll(final Path path,
			final int targetIndex,
			final String targetValue,
			int... otherIndices) {
		List<List<String>> result = new ArrayList<>();
		Iterator<List<String>> it = getIterator(path);
		
		List<String> line;
		List<String> temp = new ArrayList<>();
		int hitIndex = -1;
		while (it.hasNext()) {
			temp.clear();
			
			line = it.next();
			if ((hitIndex = line.indexOf(targetValue)) != -1) {
				temp.add(line.get(hitIndex));
				
				//Add the other optional data
				for(int data : otherIndices) {
					temp.add(line.get(data));
				}
				
				result.add(new ArrayList<>(temp));
				System.out.println(temp);
			}
		}
		
		return result;
	}
	
	public static List<List<String>> findRadius(final Path path,
			final double lat,
			final double lon,
			final double radius
			int)
	
	public static void main(String[] args) {
		Path path = Paths.get("C:\\Users\\Stanley\\Documents\\GitHub\\QuickTrip\\src\\main\\resources\\airports\\largeAirports.csv");
		
		CSVSearcher.findAll(path, -1, "New York", 0, 1, 2);
	}
}
