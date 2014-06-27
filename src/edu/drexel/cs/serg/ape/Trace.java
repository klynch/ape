package edu.drexel.cs.serg.ape;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A trace is an ordered list of measurement sets. Each measurement set is a
 * n-dimensional set of measurements
 * 
 * @author klynch
 * @since 0.1
 */
public class Trace implements Iterable<Map<String, Double>> {
	private List<Map<String, Double>> values;

	public Trace() {
		values = new LinkedList<Map<String, Double>>();
	}

	public void add(final Map<String, Double> data) {
		values.add(data);
	}

	@Override
	public Iterator<Map<String, Double>> iterator() {
		return Collections.unmodifiableList(values).iterator();
	}
}
