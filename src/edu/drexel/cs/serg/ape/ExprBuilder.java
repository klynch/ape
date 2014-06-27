package edu.drexel.cs.serg.ape;

import java.util.Random;

import edu.drexel.cs.serg.ape.grammar.CompareExpr;
import edu.drexel.cs.serg.ape.grammar.Expr;
import edu.drexel.cs.serg.ape.grammar.LogicExpr;

/**
 * Randomly builds expressions
 * 
 * @author klynch
 * @since 0.1
 */
public class ExprBuilder extends Randomizer {
	protected final int maxDepth;
	protected final double p = (1 - 1.0 / 32.0) / 2;

	public ExprBuilder(int maxDepth) {
		super();
		this.maxDepth = maxDepth;
	}

	public ExprBuilder(final Random rand, int maxDepth) {
		super(rand);
		this.maxDepth = maxDepth;
	}

	public Expr build() {
		return buildExpr(null);
	}

	public Expr buildExpr(final Expr parent) {
		return buildExpr(parent, maxDepth);
	}

	public Expr buildExpr(final Expr parent, int depth) {
		if (depth == 0 || rand.nextDouble() > p)
			return buildCompareExpr(parent, depth - 1);
		else
			return buildLogicExpr(parent, depth - 1);
	}

	public Expr buildCompareExpr(final Expr parent, int depth) {
		final CompareExpr expr = new CompareExpr(parent, getIndex(parent));
		expr.setOper(randCompareOper());
		expr.setIdentifier(randIdentifier());
		expr.setValue(randDouble());
		return expr;
	}

	public LogicExpr buildLogicExpr(final Expr parent, int depth) {
		final LogicExpr expr = new LogicExpr(parent, getIndex(parent));
		expr.setOper(randLogicOper());

		final int n = 2;
		for (int i = 0; i < n; ++i)
			expr.addChild(buildExpr(expr, depth - 1));

		return expr;
	}

	protected int getIndex(final Expr parent) {
		if (parent == null)
			return -1;
		else
			return parent.getArity();
	}
}
