package edu.drexel.cs.serg.ape;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import edu.drexel.cs.serg.ape.ga.ConcurrentTransport;
import edu.drexel.cs.serg.ape.ga.Environment;
import edu.drexel.cs.serg.ape.ga.Island;
import edu.drexel.cs.serg.ape.ga.Organism;
import edu.drexel.cs.serg.ape.ga.Transport;
import edu.drexel.cs.serg.ape.grammar.Expr;

/**
 * Main entry point to the experiment. Takes in a properties file with the
 * following properties:
 * 
 * <ul>
 * <li>clean.file - string - A tab separated values file containing the "good"
 * data to fit against. Data should be normalized to 0 -> 1</li>
 * <li>dirty.file - string - A tab separated values file containing the "bad"
 * data to fit against. Data should be normalized to 0 -> 1</li>
 * <li>report.rate - integer - Report run statistics every x seconds</li>
 * <li>thread.count - integer - number of workers/islands</li>
 * <li>population.size - integer - The size of the island's population</li>
 * <li>max.generations - integer - The number of generations to simulate</li>
 * <li>mutation.rate - double - The probability of mutating an organism</li>
 * <li>migration.rate - double - The probability of migrating an organism to a
 * different island</li>
 * <li>random.generation - boolean - If true, All seeds are randomly generated.
 * Otherwise, a hyperbox is added.</li>
 * <li>generate.multivariate - boolean - multivariate nonterminals are used in
 * addition to single variable</li>
 * <li>expr.depth - integer - the maximum dept of an evolved predicate</li>
 * </ul>
 * 
 * @author klynch
 * @since 0.1
 */
public class Evolve implements Runnable {
	private final Properties props;
	private final Random rand;
	private final List<Island<Expr, FMeasure>> islands = new LinkedList<Island<Expr, FMeasure>>();
	private final Transport<Expr, FMeasure> transport = new ConcurrentTransport<Expr, FMeasure>();
	private final Chronos chronos;
	private final int workers;
	private final int size;
	private final int reportRate;
	private final double migrationRate;
	private Semaphore startup;
	private Semaphore finished;

	public Evolve(final Properties props) throws IOException {
		this.props = props;

		rand = new Random(System.currentTimeMillis() ^ Thread.currentThread().getId());
		workers = Integer.parseInt(props.getProperty("thread.count"));
		size = Integer.parseInt(props.getProperty("population.size"));
		migrationRate = Double.parseDouble(props.getProperty("migration.rate"));

		final double mutationRate = Double.parseDouble(props.getProperty("mutation.rate"));
		final int depth = Integer.parseInt(props.getProperty("expr.depth"));
		final int generations = Integer.parseInt(props.getProperty("max.generations"));

		startup = new Semaphore(-workers + 1);
		finished = new Semaphore(-1);

		// time keeper
		chronos = new Chronos(props, generations, finished);

		reportRate = Integer.parseInt(props.getProperty("report.rate"));

		System.err
				.printf("islands=%d; population=%d; max_generations=%d; max_depth=%d; migration_rate=%.2f; mutation_rate=%.2f%n",
						workers, size, generations, depth, migrationRate, mutationRate);

		DataOracle.initialize(props);
	}

	@Override
	public void run() {
		// generate the islands and environments
		for (int i = 0; i < workers; ++i)
			islands.add(createIsland(i, createEnvironment()));

		final ExecutorService threads = Executors.newFixedThreadPool(workers);
		for (Runnable t : islands)
			threads.execute(t);

		final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

		try {
			// wait for all islands to start
			startup.acquire();

			System.err.println("Simulating evolution.");

			// schedule report of best organism
			timer.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					final Organism<Expr, FMeasure> best = chronos.getBestOrganism();
					final int gen = chronos.getCurrentGeneration();

					if (best != null) {
						System.err.printf("Best (%d) %s: (%s) %s%n", gen, best.getFitness(), best.getChromosome()
								.getClass().getSimpleName(), best.getChromosome().toString());
					}
				}
			}, reportRate, reportRate, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		// wait until chronos tells us to stop
		try {
			finished.acquire();
			threads.shutdownNow();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		// report the best organism
		final Organism<Expr, FMeasure> best = chronos.getBestOrganism();
		if (best != null) {
			System.out.println("Fitness: " + best.getFitness().toString());
			System.out.println("Predicate: " + best.getChromosome());
		}

		timer.shutdown();
	}

	protected Island<Expr, FMeasure> createIsland(int id, final Environment<Expr, FMeasure> env) {
		final Island<Expr, FMeasure> island = new Island<Expr, FMeasure>(id, startup, env, transport, chronos);
		island.setMigrationRate(migrationRate);
		return island;
	}

	protected Environment<Expr, FMeasure> createEnvironment() {
		return new Environment<Expr, FMeasure>(new PredicateDesigner(props, new Random(rand.nextLong())), size,
				rand.nextLong());
	}

	/**
	 * Main entry point. Arguments are read from properties file passed in as
	 * arg.
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		if (args.length == 0) {
			System.err.println("Invalid arguments.");
			printUsage(System.err);
			System.exit(1);
		}

		final Properties props = new Properties();

		try {
			props.load(new FileReader(args[0]));
		} catch (IOException ex) {
			System.err.println("Invalid configuration file.");
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			System.exit(2);
		}

		try {
			final Evolve app = new Evolve(props);
			app.run();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			System.exit(4);
		}
	}

	private static final void printUsage(final PrintStream out) {
		out.printf("Usage: java %s config-file%n", Evolve.class.getName());
	}
}
