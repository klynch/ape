package edu.drexel.cs.serg.ape.grammar;

import java.util.Map;

/**
 * A comparison expression compares a single variable to a value
 * 
 * @author klynch
 * @since 0.1
 */
public class CompareExpr extends Expr {
	private CompareOper oper;
	private String identifier;
	private double value;

	public CompareExpr(final Expr parent, int index) {
		super(parent, index);
	}

	public final CompareOper getOper() {
		return oper;
	}

	public final void setOper(CompareOper oper) {
		this.oper = oper;
	}

	public final String getIdentifier() {
		return identifier;
	}

	public final void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public final double getValue() {
		return value;
	}

	public final void setValue(double value) {
		this.value = value;
	}

	@Override
	public void addChild(final Expr node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeChild(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(final NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append(identifier);
		buf.append(' ');
		buf.append(oper);
		buf.append(' ');
		buf.append(value);
		return buf.toString();
	}

	@Override
	public CompareExpr clone(final Expr parent) {
		return (CompareExpr) super.clone(parent);
	}

	@Override
	public boolean evaluate(final Map<String, Double> bindings) {
		if (bindings.containsKey(identifier))
			return oper.evaluate(bindings.get(identifier), value);
		else
			return true;
	}
}
