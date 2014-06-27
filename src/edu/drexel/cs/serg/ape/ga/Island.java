package edu.drexel.cs.serg.ape.ga;

import java.util.Random;
import java.util.concurrent.Semaphore;

import edu.drexel.cs.serg.ape.ComparableNumber;

/**
 * 
 * Breeding and evaluation are carried out in isolation on each island.
 * 
 * @author klynch
 * @since 0.1
 */
public class Island<T, F extends ComparableNumber> implements Runnable {
	private final Random rand;
	protected final int id;
	protected final Environment<T, F> environ;
	protected final Transport<T, F> transport;
	protected final Terminator<T, F> stats;
	protected double migrationRate = 0;
	private final Semaphore startup;

	public Island(int id, final Semaphore startup, final Environment<T, F> env, final Transport<T, F> transport,
			final Terminator<T, F> stats) {
		this(id, startup, env, transport, stats, System.nanoTime() ^ Thread.currentThread().getId());
	}

	public Island(int id, final Semaphore startup, final Environment<T, F> env, final Transport<T, F> transport,
			final Terminator<T, F> stats, long seed) {
		this.id = id;
		this.environ = env;
		this.transport = transport;
		this.stats = stats;
		this.rand = new Random(seed);
		this.startup = startup;
	}

	public final int getGeneration() {
		return environ.getGeneration();
	}

	public final double getMigrationRate() {
		return migrationRate;
	}

	public final void setMigrationRate(double migrationRate) {
		this.migrationRate = migrationRate;
	}

	public Organism<T, F> getBestOrganism() {
		return environ.getBestOrganism();
	}

	@Override
	public void run() {
		environ.initialize();
		startup.release();

		Organism<T, F> org;
		do {
			// simulate and get best
			org = environ.simulate();

			Thread.yield();

			// pick up a migrant
			if (rand.nextDouble() < migrationRate) {
				final Organism<T, F> alien = transport.get(id);
				if (alien != null) {
					environ.migrate(alien);
				}
			}

			// migrate our best
			if (rand.nextDouble() < migrationRate)
				transport.put(id, org);
		} while (stats.analyze(environ));

		Thread.currentThread().interrupt();
	}
}
