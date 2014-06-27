package edu.drexel.cs.serg.ape.breed;

import java.util.Random;

import edu.drexel.cs.serg.ape.Randomizer;
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
public class ExprPicker extends Randomizer implements NodeVisitor {
	private Expr result = null;

	public ExprPicker() {
		super();
	}

	public ExprPicker(final Random rand) {
		super(rand);
	}

	public final Expr getResult() {
		return result;
	}

	@Override
	public void visit(final LogicExpr expr) {
		final int n = randInt(expr.getArity() + 1);
		if (n == expr.getArity())
			result = expr;
		else
			expr.getChild(n).visit(this);
	}

	@Override
	public void visit(final CompareExpr expr) {
		result = expr;
	}

	@Override
	public void visit(final MultivariateCompareExpr expr) {
		result = expr;
	}
}
