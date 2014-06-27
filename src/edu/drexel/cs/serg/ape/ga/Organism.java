package edu.drexel.cs.serg.ape.ga;

import edu.drexel.cs.serg.ape.ComparableNumber;

/**
 * An organism is comprised of a mutatable chromosome and a fitness measure.
 * 
 * @author klynch
 * @since 0.1
 */
public class Organism<T, F extends ComparableNumber> implements Comparable<Organism<T, F>> {
	private T chromosome;
	private F fitness;

	public static final <V, G extends ComparableNumber> Organism<V, G> from(final V dna, final G fit) {
		return new Organism<V, G>(dna, fit);
	}

	public Organism() {
		this(null, null);
	}

	public Organism(final T dna) {
		this(dna, null);
	}

	public Organism(final T dna, final F fit) {
		chromosome = dna;
		fitness = fit;
	}

	public final T getChromosome() {
		return chromosome;
	}

	public final void setChromosome(T chromosome) {
		this.chromosome = chromosome;
	}

	public final F getFitness() {
		return fitness;
	}

	public final void setFitness(F fitness) {
		this.fitness = fitness;
	}

	@Override
	public int compareTo(final Organism<T, F> other) {
		return fitness.compareTo(other.getFitness());
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		else if (obj == null || !(obj instanceof Organism<?, ?>))
			return false;

		final Organism<?, ?> org = (Organism<?, ?>) obj;
		return chromosome == org.chromosome || (chromosome != null && chromosome.equals(org.chromosome));
	}

	@Override
	public int hashCode() {
		if (chromosome == null)
			return 0;
		else
			return chromosome.hashCode();
	}
}
