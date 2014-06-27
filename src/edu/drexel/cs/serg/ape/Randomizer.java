package edu.drexel.cs.serg.ape;

import java.util.Random;

import edu.drexel.cs.serg.ape.grammar.CompareOper;
import edu.drexel.cs.serg.ape.grammar.LogicOper;

/**
 * Random helper methods
 * 
 * @author klynch
 * @since 0.1
 */
public abstract class Randomizer {
	protected final Random rand;

	protected Randomizer() {
		this(new Random(System.nanoTime() ^ Thread.currentThread().getId()));
	}

	protected Randomizer(final Random rand) {
		this.rand = rand;
	}

	protected final int randInt(int n) {
		return rand.nextInt(n);
	}

	protected final int randInt(int min, int max) {
		return rand.nextInt(max - min) + min;
	}

	protected final boolean randBoolean() {
		return rand.nextBoolean();
	}

	protected final double randDouble() {
		return rand.nextDouble();
	}

	protected final <T> T randFromArray(final T[] array) {
		final int index = rand.nextInt(array.length);
		return array[index];
	}

	/**
	 * Select a random compare operator from the grammar
	 * 
	 * @return
	 */
	protected final CompareOper randCompareOper() {
		return randFromArray(CompareOper.values());
	}

	/**
	 * Select a random logic operator from the grammar
	 * 
	 * @return
	 */
	protected final LogicOper randLogicOper() {
		return randFromArray(LogicOper.values());
	}

	/**
	 * Select a random identifier from the available dataset
	 * 
	 * @return
	 */
	protected final String randIdentifier() {
		return randFromArray(DataOracle.fields);
	}
}
