package edu.drexel.cs.serg.ape.grammar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An expression to be evaluated. Contains a list of children
 * 
 * @author klynch
 * @since 0.1
 */
public abstract class Expr implements Iterable<Expr>, Cloneable {
	protected Expr parent;
	protected int index;
	protected List<Expr> children;

	protected Expr(final Expr parent, int index) {
		children = new ArrayList<Expr>();
	}

	@Override
	public Iterator<Expr> iterator() {
		return Collections.unmodifiableList(children).iterator();
	}

	public final Expr getParent() {
		return parent;
	}

	private final void setParent(final Expr parent) {
		this.parent = parent;
	}

	public final int getIndex() {
		return index;
	}

	public final void setIndex(int index) {
		this.index = index;
	}

	public final int getArity() {
		return children.size();
	}

	public final Expr getChild(int index) {
		return children.get(index);
	}

	public final void setChild(int index, final Expr node) {
		children.set(index, node);
	}

	public void addChild(final Expr node) {
		children.add(node);
	}

	public void removeChild(int index) {
		children.remove(index);
	}

	public void removeChild(final Expr expr) {
		children.remove(expr);
	}

	protected Expr clone() {
		try {
			return (Expr) super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Clone the expression recursively, setting the parent of the expression to
	 * the one provided.
	 * 
	 * @param parent
	 * @return
	 */
	public Expr clone(final Expr parent) {
		final Expr expr = clone();
		expr.setParent(parent);
		expr.children = new ArrayList<Expr>();

		for (Expr child : this)
			expr.addChild(child.clone(expr));

		return expr;
	}

	public abstract void visit(NodeVisitor visitor);

	public abstract boolean evaluate(Map<String, Double> bindings);
}
