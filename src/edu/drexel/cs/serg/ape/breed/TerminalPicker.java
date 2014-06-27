package edu.drexel.cs.serg.ape.breed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.drexel.cs.serg.ape.grammar.CompareExpr;
import edu.drexel.cs.serg.ape.grammar.Expr;
import edu.drexel.cs.serg.ape.grammar.LogicExpr;
import edu.drexel.cs.serg.ape.grammar.MultivariateCompareExpr;
import edu.drexel.cs.serg.ape.grammar.NodeVisitor;

/**
 *
 * @author klynch
 * @since 0.1
 */
public class TerminalPicker implements NodeVisitor {
	private List<Expr> terms = new ArrayList<Expr>();

	public final List<Expr> getTerminals() {
		return Collections.unmodifiableList(terms);
	}

	public void reset() {
		terms.clear();
	}

	@Override
	public void visit(final LogicExpr expr) {
		for (Expr child : expr)
			child.visit(this);
	}

	@Override
	public void visit(final CompareExpr expr) {
		terms.add(expr);
	}

	@Override
	public void visit(final MultivariateCompareExpr expr) {
		terms.add(expr);
	}
}
