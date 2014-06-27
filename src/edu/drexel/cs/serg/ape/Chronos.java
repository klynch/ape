package edu.drexel.cs.serg.ape;

import java.util.Properties;
import java.util.concurrent.Semaphore;

import edu.drexel.cs.serg.ape.ga.Environment;
import edu.drexel.cs.serg.ape.ga.Organism;
import edu.drexel.cs.serg.ape.ga.Terminator;
import edu.drexel.cs.serg.ape.grammar.Expr;
import edu.drexel.cs.serg.ape.grammar.LogicExpr;

/**
 * Keeps track of the generations of the experiment and halts when terminal
 * conditions are met
 * 
 * @author klynch
 * @since 0.1
 */
public class Chronos implements Terminator<Expr, FMeasure> {
	private final Semaphore lock;
	private final int generations;
	private int currentGeneration = 0;
	private Organism<Expr, FMeasure> best = null;

	public Chronos(final Properties props, int generations, final Semaphore lock) {
		this.generations = generations;
		this.lock = lock;
	}

	@Override
	public Organism<Expr, FMeasure> getBestOrganism() {
		return best;
	}

	public final int getGenerations() {
		return generations;
	}

	public final int getCurrentGeneration() {
		return currentGeneration;
	}

	@Override
	public boolean analyze(final Environment<Expr, FMeasure> env) {
		final Organism<Expr, FMeasure> org = env.getBestOrganism();

		if (org.getChromosome() instanceof LogicExpr && org.getChromosome().getArity() == 0)
			return true;

		synchronized (this) {
			if (best == null || org.getFitness().compareTo(best.getFitness()) > 0)
				best = org;
			currentGeneration = Math.max(currentGeneration, env.getGeneration());
		}

		// stop when we get a perfectly fit individual, or we exceed the # of
		// generations
		if (Double.compare(org.getFitness().doubleValue(), 1) == 0 || env.getGeneration() >= generations) {
			lock.release();
			return false;
		}

		// Calculate the average fitness
		double avg = 0;
		for (Organism<Expr, FMeasure> curr : env)
			avg += curr.getFitness().doubleValue();

		avg /= env.getSize();

		// Calculate the stddev fitness
		double dev = 0;
		for (Organism<Expr, FMeasure> curr : env)
			dev += Math.pow(curr.getFitness().doubleValue() - avg, 2);

		dev /= env.getSize();
		dev = Math.sqrt(dev);

		// Stop the experiment if the stddev approaches 0
		final boolean result = dev > Float.MIN_VALUE;

		if (!result)
			lock.release();

		return result;
	}
}
