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
public class OperRadiation extends Randomizer implements NodeVisitor {
	public OperRadiation() {
		super();
	}

	public OperRadiation(final Random rand) {
		super(rand);
	}

	@Override
	public void visit(final LogicExpr expr) {
		final int index = randInt(expr.getArity() + 1);
		if (index == expr.getArity())
			expr.setOper(randLogicOper());
		else
			expr.getChild(index).visit(this);
	}

	@Override
	public void visit(final CompareExpr expr) {
		expr.setOper(randCompareOper());
	}

	@Override
	public void visit(final MultivariateCompareExpr expr) {
		expr.setOper(randCompareOper());
	}
}
