package edu.drexel.cs.serg.ape.grammar;

/**
 * The set of comparison operations in the grammar: < > <= >=
 * 
 * @author klynch
 * @since 0.1
 */
public enum CompareOper {
	LT("<") {
		@Override
		public boolean evaluate(double a, double b) {
			return (a < b);
		}
	},

	GT(">") {
		@Override
		public boolean evaluate(double a, double b) {
			return (a > b);
		}
	},

	LE("<=") {
		@Override
		public boolean evaluate(double a, double b) {
			return (a <= b);
		}
	},

	GE(">=") {
		@Override
		public boolean evaluate(double a, double b) {
			return (a >= b);
		}
	};

	private final String symbol;

	private CompareOper(final String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}

	public abstract boolean evaluate(double a, double b);
}
