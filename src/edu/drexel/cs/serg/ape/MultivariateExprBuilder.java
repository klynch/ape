package edu.drexel.cs.serg.ape;

import java.util.Random;

import edu.drexel.cs.serg.ape.grammar.Expr;
import edu.drexel.cs.serg.ape.grammar.MultivariateCompareExpr;

/**
 * Builds expressions with multivariate expressions
 * 
 * @author klynch
 * @since 0.1
 */
public class MultivariateExprBuilder extends ExprBuilder {
	protected final double compare_p = 0.5;

	public MultivariateExprBuilder(int maxDepth) {
		super(maxDepth);
	}

	public MultivariateExprBuilder(final Random rand, int maxDepth) {
		super(rand, maxDepth);
	}

	/**
	 * With probability compare_p, generate multivariate expressions
	 */
	public Expr buildCompareExpr(final Expr parent, int depth) {
		if (rand.nextDouble() > compare_p) {
			final MultivariateCompareExpr expr = new MultivariateCompareExpr(parent, getIndex(parent));
			expr.setOper(randCompareOper());
			expr.setLeftIdentifier(randIdentifier());
			expr.setRightIdentifier(randIdentifier());
			return expr;
		} else {
			return super.buildCompareExpr(parent, depth);
		}
	}
}
