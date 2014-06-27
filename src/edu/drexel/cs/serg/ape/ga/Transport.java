package edu.drexel.cs.serg.ape.ga;

import edu.drexel.cs.serg.ape.ComparableNumber;

/**
 * Transports organisms between two {@link Island}.
 * 
 * @author klynch
 * @since 0.1
 */
public interface Transport<T, F extends ComparableNumber> {
	Organism<T, F> get(int id);

	void put(int id, Organism<T, F> org);
}
