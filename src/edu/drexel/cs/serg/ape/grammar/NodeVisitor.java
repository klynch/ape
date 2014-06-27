package edu.drexel.cs.serg.ape.grammar;

/**
 * 
 * @author klynch
 * @since 0.1
 */
public interface NodeVisitor {
	void visit(LogicExpr expr);

	void visit(CompareExpr expr);

	void visit(MultivariateCompareExpr expr);
}
