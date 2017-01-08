package io.github.kkysen.quicktrip.app;

import static org.junit.Assert.assertEquals;

import io.github.kkysen.quicktrip.io.MyFiles;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Test;


/**
 * 
 * 
 * @author Khyber Sen
 */
public class ItineraryScreenModelTest {
    
    private static final Random random = new Random();
    
    private static final List<String> cities;
    static {
        try {
            cities = MyFiles.readLines(Paths.get("C:/Users/kkyse/Downloads/Top5000Population.csv"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static String randomCity() {
        return cities.get(random.nextInt(cities.size()));
    }
    
    
    public void test() {
        final SearchModel searchArgs = new SearchModel();
        
        LocalDate date = LocalDate.now();
        date = date.plusDays(random.nextInt(100));
        searchArgs.setDate(date);
        
        searchArgs.setNumPeople(random.nextInt(5) + 1);
        
        searchArgs.setOrigin(randomCity());
        
        final int numDests = random.nextInt(23);
        int numDays = 0;
        final List<NoDateDestination> dests = new ArrayList<>(numDests);
        for (int i = 0; i < numDests; i++) {
            final int singleNumDays = random.nextInt(8) + 1;
            numDays += singleNumDays;
            dests.add(new NoDateDestination(randomCity(), singleNumDays));
        }
        searchArgs.setDestinations(dests);
        
        searchArgs.setBudget(numDays * 400);
        
        try {
            final String json = QuickTripConstants.GSON.toJson(searchArgs);
            MyFiles.write(Paths.get(QuickTripConstants.SEARCH_ARGS_PATH), json);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        
        final ItineraryModel model = new ItineraryModel();
        assertEquals(numDests, model.getDestinations().size());
    }
    
    @Test
    public void testMany() {
        IntStream.range(0, 100).forEach(i -> {
            System.out.println("\n\t#" + i + "\n");
            try {
                test();
            } catch (final NullPointerException e) {
                e.printStackTrace();
            }
        });
    }
    
}
