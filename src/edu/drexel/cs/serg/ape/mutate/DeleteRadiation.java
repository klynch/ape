package edu.drexel.cs.serg.ape.mutate;

import java.util.Random;

import edu.drexel.cs.serg.ape.Randomizer;
import edu.drexel.cs.serg.ape.grammar.CompareExpr;
import edu.drexel.cs.serg.ape.grammar.LogicExpr;
import edu.drexel.cs.serg.ape.grammar.MultivariateCompareExpr;
import edu.drexel.cs.serg.ape.grammar.NodeVisitor;

/**
 * Randomly deletes portions of an expression
 * 
 * @author klynch
 * @since 0.1
 */
public class DeleteRadiation extends Randomizer implements NodeVisitor {
	public DeleteRadiation() {
		super();
	}

	public DeleteRadiation(final Random rand) {
		super(rand);
	}

	@Override
	public void visit(final LogicExpr expr) {
		int n = expr.getArity();
		if (expr.getParent() != null)
			++n;

		if (n == 0)
			return;

		final int index = randInt(n);
		if (index == expr.getArity())
			expr.getParent().removeChild(expr);
		else
			expr.getChild(index).visit(this);

		if (expr.getArity() == 0)
			throw new IllegalStateException("???");
	}

	@Override
	public void visit(final CompareExpr expr) {
		if (expr.getParent() != null)
			expr.getParent().removeChild(expr);
	}

	@Override
	public void visit(final MultivariateCompareExpr expr) {
		if (expr.getParent() != null)
			expr.getParent().removeChild(expr);
	}
}
