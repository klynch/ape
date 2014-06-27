package edu.drexel.cs.serg.ape.grammar;

import java.util.Map;

/**
 * Allows for comparisons of two variables
 * 
 * @author klynch
 * @since 0.1
 */
public class MultivariateCompareExpr extends Expr {
	private CompareOper oper;
	private String left;
	private String right;

	public MultivariateCompareExpr(final Expr parent, int index) {
		super(parent, index);
	}

	public final CompareOper getOper() {
		return oper;
	}

	public final void setOper(CompareOper oper) {
		this.oper = oper;
	}

	public final String getLeftIdentifier() {
		return left;
	}

	public final void setLeftIdentifier(final String left) {
		this.left = left;
	}

	public final String getRightIdentifier() {
		return right;
	}

	public final void setRightIdentifier(final String right) {
		this.right = right;
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
		buf.append(left);
		buf.append(' ');
		buf.append(oper);
		buf.append(' ');
		buf.append(right);
		return buf.toString();
	}

	@Override
	public MultivariateCompareExpr clone(final Expr parent) {
		return (MultivariateCompareExpr) super.clone(parent);
	}

	@Override
	public boolean evaluate(final Map<String, Double> bindings) {
		if (bindings.containsKey(left) && bindings.containsKey(right))
			return oper.evaluate(bindings.get(left), bindings.get(right));
		else
			return true;
	}
}
