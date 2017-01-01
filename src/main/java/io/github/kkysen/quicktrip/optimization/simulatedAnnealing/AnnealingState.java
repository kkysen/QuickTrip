package io.github.kkysen.quicktrip.optimization.simulatedAnnealing;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface AnnealingState extends Cloneable {
    
    public void perturb();
    
    public void undo();
    
    public double energy();
    
    public AnnealingState clone();
    
    @Override
    public String toString();
    
}
