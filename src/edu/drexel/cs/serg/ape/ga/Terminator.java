package edu.drexel.cs.serg.ape.ga;

import edu.drexel.cs.serg.ape.ComparableNumber;

/**
 * Determines conditions to terminate the experiment
 * 
 * @author klynch
 * @since 0.1
 */
public interface Terminator<T, F extends ComparableNumber> {
	Organism<T, F> getBestOrganism();

	boolean analyze(Environment<T, F> env);
}
