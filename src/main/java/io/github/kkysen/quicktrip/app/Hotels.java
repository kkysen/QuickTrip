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
    private final int budget;
    
    private final List<Hotel> hotels;
    
    private Hotel prevHotel;
    private int prevIndex;
    
    public Hotels(final List<Destination> dests, final int budget) {
        this.budget = budget;
        size = dests.size();
        pools = new ArrayList<>(size);
        for (final Destination dest : dests) {
            pools.add(dest.possibleHotels());
        }
        hotels = new ArrayList<>(size);
        for (final List<Hotel> pool : pools) {
            hotels.add(pool.get(random.nextInt(pool.size())));
        }
    }
    
    private Hotels(final Hotels other) {
        size = other.size;
        pools = other.pools;
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
    
    @Override
    public double energy() {
        // TODO Auto-generated method stub
        return 0;
        // FIXME
    }
    
    @Override
    public AnnealingState clone() {
        return new Hotels(this);
    }
    
    public List<Hotel> getHotels() {
        return new ArrayList<>(hotels);
    }
    
}