package edu.drexel.cs.serg.ape.mutate;

import java.util.Random;

import edu.drexel.cs.serg.ape.ExprBuilder;
import edu.drexel.cs.serg.ape.Randomizer;
import edu.drexel.cs.serg.ape.grammar.CompareExpr;
import edu.drexel.cs.serg.ape.grammar.LogicExpr;
import edu.drexel.cs.serg.ape.grammar.MultivariateCompareExpr;
import edu.drexel.cs.serg.ape.grammar.NodeVisitor;

/**
 * Randomly adds new expressions
 * 
 * @author klynch
 * @since 0.1
 */
public class AddRadiation extends Randomizer implements NodeVisitor {
	protected final ExprBuilder builder;

	public AddRadiation(final ExprBuilder builder) {
		super();
		this.builder = builder;
	}

	public AddRadiation(final Random rand, final ExprBuilder builder) {
		super(rand);
		this.builder = builder;
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
			expr.addChild(builder.buildExpr(expr));
		else
			expr.getChild(index).visit(this);
	}

	@Override
	public void visit(final CompareExpr expr) {
		if (expr.getParent() != null)
			expr.getParent().addChild(builder.buildCompareExpr(expr, 0));
	}

	@Override
	public void visit(final MultivariateCompareExpr expr) {
		if (expr.getParent() != null)
			expr.getParent().addChild(builder.buildCompareExpr(expr, 0));
	}
}
