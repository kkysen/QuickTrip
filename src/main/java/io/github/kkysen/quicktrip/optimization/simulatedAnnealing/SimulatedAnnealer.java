package io.github.kkysen.quicktrip.optimization.simulatedAnnealing;

import java.util.Random;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class SimulatedAnnealer<T extends AnnealingState> {
    
    private static Random random = new Random();
    private final AnnealingState state;
    private double energy;
    public AnnealingState minState;
    public @Getter double minEnergy;
    private double nextEnergy;
    private double temperature;
    private final double decayRate;
    
    public SimulatedAnnealer(final T initState, final double initTemp, final double decayRate) {
        state = initState;
        energy = initState.energy();
        minState = state.clone();
        minEnergy = energy;
        temperature = initTemp;
        this.decayRate = decayRate;
    }
    
    public SimulatedAnnealer(final T initState) {
        this(initState, 1, .99999);
    }
    
    private boolean metropolis() {
        return random.nextDouble() < Math.exp((energy - nextEnergy) / temperature);
    }
    
    public SimulatedAnnealer<T> search(final int numIters) {
        for (int i = 0; i < numIters; i++) {
            //if (i % 100000 == 0) {System.out.println(minEnergy + "\t" + energy);
            state.perturb();
            nextEnergy = state.energy();
            if (nextEnergy <= energy || metropolis()) {
                energy = nextEnergy;
                if (nextEnergy < minEnergy) {
                    minState = state.clone();
                    minEnergy = nextEnergy;
                }
            } else {
                state.undo();
            }
            temperature *= decayRate;
        }
        return this;
    }
    
    public SimulatedAnnealer<T> search() {
        return search(1000000);
    }
    
    @Override
    public String toString() {
        return "SimulatedAnnealer [minState=" + minState + ", minEnergy=" + minEnergy + "]";
    }

    public SimulatedAnnealer<T> search(final int base, final int index) {
        return search((int) Math.pow(base, index));
    }
    
    @SuppressWarnings("unchecked")
    public T getMinState() {
        return (T) minState;
    }
    
    public static void main(final String[] args) {
//        final SimulatedAnnealer annealer = new SimulatedAnnealer(new Rastrigin(1000, 1000));
//        for (int i = 0; i < 10; i++) {
//            System.out.println(annealer.search());
//        }
    }
    
}