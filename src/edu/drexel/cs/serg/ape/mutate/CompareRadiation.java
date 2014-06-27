package edu.drexel.cs.serg.ape.mutate;

import java.util.Random;

import edu.drexel.cs.serg.ape.Randomizer;
import edu.drexel.cs.serg.ape.grammar.CompareExpr;
import edu.drexel.cs.serg.ape.grammar.LogicExpr;
import edu.drexel.cs.serg.ape.grammar.MultivariateCompareExpr;
import edu.drexel.cs.serg.ape.grammar.NodeVisitor;

/**
 *
 * @author klynch
 * @since 0.1
 */
public class CompareRadiation extends Randomizer implements NodeVisitor {
	public CompareRadiation() {
		super();
	}

	public CompareRadiation(final Random rand) {
		super(rand);
	}

	@Override
	public void visit(final LogicExpr expr) {
		if (expr.getArity() == 0)
			return;

		final int index = randInt(expr.getArity());
		expr.getChild(index).visit(this);
	}

	@Override
	public void visit(final CompareExpr expr) {
		if (rand.nextBoolean())
			expr.setIdentifier(randIdentifier());
		else
			expr.setValue(randDouble());
	}

	@Override
	public void visit(final MultivariateCompareExpr expr) {
		if (rand.nextBoolean())
			expr.setLeftIdentifier(randIdentifier());
		else
			expr.setRightIdentifier(randIdentifier());
	}
}
