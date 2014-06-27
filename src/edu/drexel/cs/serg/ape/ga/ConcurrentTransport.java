package edu.drexel.cs.serg.ape.ga;

import java.util.LinkedList;
import java.util.Queue;

import edu.drexel.cs.serg.ape.ComparableNumber;

/**
 * Transports organisms between two environments.
 * 
 * @author klynch
 * @since 0.1
 */
public class ConcurrentTransport<T, F extends ComparableNumber> implements Transport<T, F> {
	private static final class Tag<U, G extends ComparableNumber> {
		public final int id;
		public final Organism<U, G> org;

		public Tag(int id, final Organism<U, G> org) {
			this.id = id;
			this.org = org;
		}

		public static <V, H extends ComparableNumber> Tag<V, H> of(int id, final Organism<V, H> org) {
			return new Tag<V, H>(id, org);
		}
	}

	private final Queue<Tag<T, F>> queue = new LinkedList<Tag<T, F>>();

	@Override
	public synchronized Organism<T, F> get(int id) {
		final Tag<T, F> next = queue.peek();
		if (next != null && next.id != id)
			return queue.poll().org;
		else
			return null;
	}

	@Override
	public synchronized void put(int id, final Organism<T, F> org) {
		queue.offer(Tag.of(id, org));
	}
}
