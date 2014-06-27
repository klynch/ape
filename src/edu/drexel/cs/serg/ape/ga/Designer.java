package edu.drexel.cs.serg.ape.ga;

import java.util.Collection;

import edu.drexel.cs.serg.ape.ComparableNumber;

/**
 * A Designer provides algorithms for generating, evaluating, and breeding
 * organisms
 * 
 * @author klynch
 * @since 0.1
 */
public abstract class Designer<T, F extends ComparableNumber> {
	public final Organism<T, F> create() {
		final Organism<T, F> org = new Organism<T, F>(build());
		evaluate(org);
		return org;
	}

	/**
	 * Evaluate the fitness of an organism
	 * 
	 * @param org
	 * @return
	 */
	public final F evaluate(final Organism<T, F> org) {
		org.setFitness(evaluate(org.getChromosome()));
		return org.getFitness();
	}

	/**
	 * Create the initial population
	 * 
	 * @return
	 */
	public abstract Collection<Organism<T, F>> createSeeds();

	/**
	 * Evaluate the fitness of a chromosome
	 * 
	 * @param dna
	 * @return
	 */
	public abstract F evaluate(final T dna);

	/**
	 * Breed two organisms
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public abstract Organism<T, F> breed(final Organism<T, F> a, final Organism<T, F> b);

	/**
	 * Build a new chromosome
	 * 
	 * @return
	 */
	protected abstract T build();
}
