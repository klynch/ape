package edu.drexel.cs.serg.ape;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Stores all of the data for the given experiment
 *
 * @author klynch
 * @since 0.1
 */
public class DataOracle {
	// hardcoded metrics from Tomcat
	public static final String[] simpleFieldsTomcat = new String[] { "cpu_time_maximum", "cpu_time_average",
			"threads_daemons", "classes_loaded", "classes_loaded_total", "memory_heap", "memory_non_heap",
			"tcp_accept_sockets_closed", "tcp_accept_sockets_open", "tcp_accept_sockets_opened", "tcp_bytes_read",
			"tcp_bytes_written", "tcp_client_sockets_closed", "tcp_client_sockets_open", "tcp_client_sockets_opened",
			"tcp_server_sockets_closed", "tcp_server_sockets_opened", "stacks_average", "stacks_maximum",
			"threads_total", "classes_unloaded_total", "usr_time_maximum", "usr_time_average" };

	// hardcoded metrics from Jigsaw
	public static final String[] simpleFieldsJigsaw = new String[] { "cpu_time_maximum", "cpu_time_average",
			"threads_daemons", "classes_loaded", "classes_loaded_total", "memory_heap", "memory_non_heap",
			"tcp_accept_sockets_closed", "tcp_accept_sockets_open", "tcp_accept_sockets_opened", "tcp_bytes_read",
			"tcp_bytes_written", "tcp_client_sockets_opened", "tcp_server_sockets_closed", "tcp_server_sockets_opened",
			"stacks_average", "stacks_maximum", "threads_total", "usr_time_maximum", "usr_time_average" };

	// Use the fields from Jigsaw
	public static final String[] simpleFields = simpleFieldsJigsaw;

	// stores the fields and the delta fields
	public static final String[] fields;

	static {
		fields = new String[2 * simpleFields.length];

		int index = 0;
		for (String f : simpleFields) {
			fields[index] = f;
			fields[index + 1] = "delta_" + f;

			index += 2;
		}
	}

	private static DataOracle instance;

	// stores the clean data
	private final Trace cleanTrace;

	// stores the dirty data
	private final Trace dirtyTrace;

	public static final DataOracle getInstance() {
		return instance;
	}

	public static final void initialize(final Properties props) throws IOException {
		instance = new DataOracle(props);
	}

	private DataOracle(final Properties props) throws IOException {
		cleanTrace = loadTrace(new File(props.getProperty("clean.file")));
		dirtyTrace = loadTrace(new File(props.getProperty("dirty.file")));
	}

	public final Trace getCleanTrace() {
		return cleanTrace;
	}

	public final Trace getDirtyTrace() {
		return dirtyTrace;
	}

	/**
	 * Loads a tab-separated-value file of data
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	protected Trace loadTrace(final File path) throws IOException {
		final Trace trace = new Trace();
		final List<String> labels = new ArrayList<String>();
		final BufferedReader in = new BufferedReader(new FileReader(path));

		for (String lbl : in.readLine().split("\t"))
			labels.add(lbl);

		String line;
		while ((line = in.readLine()) != null) {
			final Map<String, Double> bindings = new TreeMap<String, Double>();

			int index = 0;
			for (String value : line.split("\t")) {
				bindings.put(labels.get(index), Double.valueOf(value));
				++index;
			}

			trace.add(bindings);
		}

		return trace;
	}
}
