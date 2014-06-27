package edu.drexel.cs.serg.ape.grammar;

/**
 * The set of logical operators defined in the grammar
 * 
 * @author klynch
 * @since 0.1
 */
public enum LogicOper {
	AND {
		@Override
		public boolean evaluate(boolean a, boolean b) {
			return a && b;
		}
	},

	OR {
		@Override
		public boolean evaluate(boolean a, boolean b) {
			return a || b;
		}
	},

	XOR {
		@Override
		public boolean evaluate(boolean a, boolean b) {
			return a ^ b;
		}
	},

	NAND {
		@Override
		public boolean evaluate(boolean a, boolean b) {
			return !(a && b);
		}
	},

	NOR {
		@Override
		public boolean evaluate(boolean a, boolean b) {
			return !(a || b);
		}
	};

	public abstract boolean evaluate(boolean a, boolean b);

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
