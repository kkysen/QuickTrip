package io.github.kkysen.quicktrip.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.AllArgsConstructor;


/**
 * @author Stanley Lin
 */
public class CSVIterator implements Iterator<List<String>> {
	private final int lineSize;		//Maybe we want arrays later?
	
	private String line;
	private BufferedReader reader;
	
	/**
	 * This constructor may be used later to generate arrays instead of lists
	 * 
	 * @param path The path of the CSV file
	 * @param size	The amount of data in one line of the CSV
	 * @param charset The charset to use
	 * 
	 * @throws <code>IllegalArgumentException</code> 	If the Path does not lead to a file
	 * 													ending in .csv
	 */
	public CSVIterator(final Path path,
			final int size,
			final Charset charset) {
		if (!path.toString().endsWith(".csv")) 
			throw new IllegalArgumentException("Not a CSV (please append .csv)");
		
		try {
			reader = Files.newBufferedReader(path, charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		lineSize = size;
	}
	
	/**
	 * 
	 * @param path		The path of the CSV file
	 * @param charset	The Charset to use
	 */
	public CSVIterator(final Path path,
			final Charset charset) {
		this(path, -1, charset);
	}
	
	@Override
	public boolean hasNext() {
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line != null;
	}
	
	/**
	 * @return A <code>List</code> containing a line split from commas
	 */
	@Override
	public List<String> next() {
		//lineArray = new ArrayList<>();
		return Arrays.asList(line.split(","));
	}
	
	public static void main(String[] args) {
		CSVIterator c = new CSVIterator(
				Paths.get("C:\\Users\\Stanley\\Documents\\GitHub\\QuickTrip\\src\\main\\resources\\airports\\largeAirports.csv"),
				11,
				StandardCharsets.UTF_8);
		while (c.hasNext()) {
			System.out.println(c.next());
		}
	}
}
