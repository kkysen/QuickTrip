package io.github.kkysen.quicktrip.optimization.simulatedAnnealing;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface AnnealingState extends Cloneable {
    
    public void step();
    
    public void undo();
    
    public double energy();
    
    public AnnealingState clone();
    
    @Override
    public String toString();
    
    public default SimulatedAnnealer anneal(final int numIters) {
        return new SimulatedAnnealer(this).search(numIters);
    }
    
}
