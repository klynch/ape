package edu.drexel.cs.serg.ape.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import edu.drexel.cs.serg.ape.ComparableNumber;

/**
 * Provides the environment for the evolved population
 * 
 * @author klynch
 * @since 0.1
 */
public class Environment<T, F extends ComparableNumber> implements Iterable<Organism<T, F>> {
	public static final int offspringCount = 2;

	private final Random rand;
	private final Designer<T, F> god;
	private final int size;
	private final List<Organism<T, F>> population;
	private final List<Organism<T, F>> offspring;
	private final double[] roulette;
	private int generation;
	private int best;

	public Environment(final Designer<T, F> god, int size) {
		this(god, size, System.currentTimeMillis() ^ Thread.currentThread().getId());
	}

	public Environment(final Designer<T, F> god, int size, long seed) {
		if (god == null)
			throw new NullPointerException();
		if (size <= 0)
			throw new IllegalArgumentException();

		this.god = god;
		this.size = size;
		this.rand = new Random(seed);
		this.population = new ArrayList<Organism<T, F>>(size);
		this.offspring = new LinkedList<Organism<T, F>>();
		this.roulette = new double[size];
	}

	public final int getGeneration() {
		return generation;
	}

	public final int getSize() {
		return size;
	}

	/**
	 * Return the best organism out of the enitre population
	 * 
	 * @return
	 */
	public Organism<T, F> getBestOrganism() {
		return population.get(best);
	}

	@Override
	public Iterator<Organism<T, F>> iterator() {
		return Collections.unmodifiableList(population).iterator();
	}

	public void initialize() {
		population.clear();
		offspring.clear();

		population.addAll(god.createSeeds());
		for (int i = population.size(); i < size; ++i)
			population.add(god.create());

		best = findBestIndex();
	}

	/**
	 * Replaces the worst organism with the new one
	 * 
	 * @param org
	 */
	public void migrate(final Organism<T, F> org) {
		if (population.size() == 0)
			return;

		int index = 0;
		Organism<T, F> worst = population.get(0);

		for (int i = 1; i < population.size(); ++i) {
			final Organism<T, F> curr = population.get(i);
			if (curr.compareTo(worst) < 0) {
				index = i;
				worst = curr;
			}
		}

		population.set(index, org);
	}

	/**
	 * On each simulation, generate the offspring (with elitism). Offpsring
	 * parents are selected with bias towards fitness.
	 * 
	 * The next generation population (including elite) is selected randomly
	 * (with bias) from offspring.
	 * 
	 * @return
	 */
	public Organism<T, F> simulate() {
		offspring.clear();

		// find elite
		Collections.swap(population, 0, best);

		// total fitness of population
		double total = 0;
		for (Organism<T, F> org : population) {
			total += org.getFitness().doubleValue();
		}

		// randomly select population with bias towards more fit individuals.
		// Each individual's probability on the roulette increases the previous
		// probability. This is normalized by the total probability
		roulette[0] = population.get(0).getFitness().doubleValue() / total;
		for (int i = 1; i < population.size(); ++i) {
			roulette[i] = population.get(i).getFitness().doubleValue() / total + roulette[i - 1];
		}

		// elitism
		offspring.add(population.get(0));

		// generate offspring
		while (offspring.size() < offspringCount * size) {
			Organism<T, F> first, second;

			first = selectFromPopulation(population, roulette);

			// make sure second parent is not the first
			int loop = 0;
			do {
				second = selectFromPopulation(population, roulette);
			} while (loop < 1000 && second == first);

			// add new child to offspring
			final Organism<T, F> child = god.breed(first, second);
			god.evaluate(child);
			offspring.add(child);
		}

		// clear prior generation
		population.clear();

		final Iterator<Organism<T, F>> iter = offspring.iterator();

		// add elite
		population.add(iter.next());

		// randomly select next
		for (int index = 1; iter.hasNext(); ++index) {
			final Organism<T, F> child = iter.next();
			if (population.size() < size
					&& (rand.nextDouble() < child.getFitness().doubleValue() / total || population.size()
							+ offspring.size() - index <= size)) {
				population.add(child);
			}
		}

		// recalculate the best
		best = findBestIndex();
		++generation;

		return getBestOrganism();
	}

	/**
	 * Select an individual randomly, biasing towards the more fit individual.
	 * 
	 * @param population
	 * @param roulette
	 * @return
	 */
	private Organism<T, F> selectFromPopulation(List<Organism<T, F>> population, double[] roulette) {
		final double p = rand.nextDouble();
		int i;
		for (i = 0; i < size - 1 && roulette[i + 1] < p; ++i) {
		}
		return population.get(i);
	}

	/**
	 * Finds the most fit organism
	 * 
	 * @return
	 */
	protected final int findBestIndex() {
		if (population.size() == 0)
			return -1;

		int index = 0;
		Organism<T, F> org = population.get(0);

		for (int i = 1; i < population.size(); ++i) {
			final Organism<T, F> curr = population.get(i);
			if (curr.getFitness().compareTo(org.getFitness()) > 0) {
				index = i;
				org = curr;
			}
		}

		return index;
	}
}
