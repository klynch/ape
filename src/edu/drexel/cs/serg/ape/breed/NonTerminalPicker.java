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
public class NonTerminalPicker implements NodeVisitor {
	private List<LogicExpr> nonterms = new ArrayList<LogicExpr>();

	public final List<LogicExpr> getNonTerminals() {
		return Collections.unmodifiableList(nonterms);
	}

	public void reset() {
		nonterms.clear();
	}

	@Override
	public void visit(final LogicExpr expr) {
		nonterms.add(expr);

		for (Expr child : expr)
			child.visit(this);
	}

	@Override
	public void visit(final CompareExpr expr) {
		// nothing to do
	}

	@Override
	public void visit(final MultivariateCompareExpr expr) {
		// nothing to do
	}
}
