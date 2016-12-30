package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.optimization.simulatedAnnealing.AnnealingState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hotels implements AnnealingState {
    
    private static final Random random = new Random();
    
    // these never change, so not cloned
    private final int size;
    private final List<List<Hotel>> pools;
    private final List<Integer> numDays;
    private final double maxRating;
    private final int budget;
    
    private final List<Hotel> hotels;
    
    private Hotel prevHotel;
    private int prevIndex;
    
    public Hotels(final List<Destination> dests, final int budget) {
        this.budget = budget;
        size = dests.size();
        pools = new ArrayList<>(size);
        numDays = new ArrayList<>(size);
        int totalDays = 0;
        // getPossibleHotels takes a while, so did it parallel
        dests.parallelStream().map(Destination::getPossibleHotels).forEach(pools::add);
        for (final Destination dest : dests) {
            final int singleNumDays = dest.getNumDays();
            numDays.add(singleNumDays);
            totalDays += singleNumDays;
        }
        maxRating = totalDays * 5;
        hotels = new ArrayList<>(size);
        for (final List<Hotel> pool : pools) {
            hotels.add(pool.get(random.nextInt(pool.size())));
        }
    }
    
    private Hotels(final Hotels other) {
        size = other.size;
        pools = other.pools;
        numDays = other.numDays;
        maxRating = other.maxRating;
        budget = other.budget;
        hotels = new ArrayList<>(other.hotels);
        prevHotel = other.prevHotel;
        prevIndex = other.prevIndex;
    }
    
    @Override
    public void perturb() {
        prevIndex = random.nextInt(size);
        final List<Hotel> randPool = pools.get(prevIndex);
        prevHotel = hotels.set(prevIndex, randPool.get(random.nextInt(randPool.size())));
    }
    
    @Override
    public void undo() {
        hotels.set(prevIndex, prevHotel);
        prevHotel = null; // free mem
    }
    
    public int totalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < size; i++) {
            totalPrice += hotels.get(i).getPrice() * numDays.get(i);
        }
        return totalPrice;
    }
    
    private double priceEnergy() {
        double priceDiff = totalPrice() - budget;
        if (priceDiff > 0) {
            priceDiff = Math.exp(priceDiff);
        }
        return priceDiff;
    }
    
    public double totalRating() {
        int totalRating = 0;
        for (int i = 0; i < size; i++) {
            totalRating += hotels.get(i).getRating() * numDays.get(i);
        }
        return totalRating;
    }
    
    private double ratingEnergy() {
        final double ratingDiff = totalRating() - maxRating;
        return - Math.exp(- ratingDiff);
    }
    
    @Override
    public double energy() {
        return priceEnergy() * ratingEnergy();
        // FIXME I have no idea if this is good
    }
    
    @Override
    public AnnealingState clone() {
        return new Hotels(this);
    }
    
    public List<Hotel> getHotels() {
        return new ArrayList<>(hotels);
    }
    
}