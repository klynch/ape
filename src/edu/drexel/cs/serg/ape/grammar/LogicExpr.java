package edu.drexel.cs.serg.ape.grammar;

import java.util.Iterator;
import java.util.Map;

/**
 * Performs a logical operation between all of the children
 * 
 * @author klynch
 * @since 0.1
 */
public class LogicExpr extends Expr {
	private LogicOper oper;

	public LogicExpr(final Expr parent, int index) {
		super(parent, index);
	}

	public final LogicOper getOper() {
		return oper;
	}

	public final void setOper(LogicOper oper) {
		this.oper = oper;
	}

	@Override
	public void visit(final NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		final Iterator<Expr> iter = iterator();
		boolean conjunct = false;

		buf.append('(');
		while (iter.hasNext()) {
			if (conjunct) {
				buf.append(' ');
				buf.append(oper.toString());
				buf.append(' ');
			}

			buf.append(iter.next().toString());
			conjunct = true;
		}
		buf.append(')');

		return buf.toString();
	}

	@Override
	public LogicExpr clone(final Expr parent) {
		return (LogicExpr) super.clone(parent);
	}

	@Override
	public boolean evaluate(final Map<String, Double> bindings) {
		boolean result = true;

		final Iterator<Expr> iter = iterator();
		if (iter.hasNext())
			result = iter.next().evaluate(bindings);

		while (iter.hasNext())
			result = oper.evaluate(result, iter.next().evaluate(bindings));

		return result;
	}
}
