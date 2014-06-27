package edu.drexel.cs.serg.ape;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import edu.drexel.cs.serg.ape.breed.ExprPicker;
import edu.drexel.cs.serg.ape.ga.Designer;
import edu.drexel.cs.serg.ape.ga.Organism;
import edu.drexel.cs.serg.ape.grammar.CompareExpr;
import edu.drexel.cs.serg.ape.grammar.CompareOper;
import edu.drexel.cs.serg.ape.grammar.Expr;
import edu.drexel.cs.serg.ape.grammar.LogicExpr;
import edu.drexel.cs.serg.ape.grammar.LogicOper;
import edu.drexel.cs.serg.ape.grammar.NodeVisitor;
import edu.drexel.cs.serg.ape.mutate.AddRadiation;
import edu.drexel.cs.serg.ape.mutate.CompareRadiation;
import edu.drexel.cs.serg.ape.mutate.OperRadiation;

/**
 * Defines all algorithms for generating, evaluating, and breeding predicates
 * 
 * @author klynch
 * @since 0.1
 */
public class PredicateDesigner extends Designer<Expr, FMeasure> {
	private final Random rand;
	private final ExprBuilder builder;
	private final double mutationRate;
	private final ExprPicker picker;

	/*
	 * If true, All seeds are randomly generated. Otherwise, a hyperbox is
	 * added.
	 */
	private final boolean randomGeneration;

	/*
	 * If true, multivariate nonterminals are used in addition to single
	 * variable
	 */
	private final boolean generateMultivariate;

	public PredicateDesigner(final Properties props) {
		this(props, new Random(System.currentTimeMillis() ^ Thread.currentThread().getId()));
	}

	public PredicateDesigner(final Properties props, final Random rand) {
		final int maxDepth = Integer.parseInt(props.getProperty("expr.depth"));
		this.mutationRate = Double.parseDouble(props.getProperty("mutation.rate"));
		this.randomGeneration = Boolean.parseBoolean(props.getProperty("random.generation", "false"));
		this.generateMultivariate = Boolean.parseBoolean(props.getProperty("generate.multivariate", "true"));
		if (this.generateMultivariate) {
			this.builder = new MultivariateExprBuilder(new Random(rand.nextLong()), maxDepth);
		} else {
			this.builder = new ExprBuilder(new Random(rand.nextLong()), maxDepth);
		}

		this.rand = rand;
		this.picker = new ExprPicker(rand);
	}

	/**
	 * Cross breeds two organisms. Clone parent A, replacing a sub-expression
	 * with a sub-expression from parent B.
	 * 
	 * Mutates the result with probability mutationRate.
	 */
	@Override
	public Organism<Expr, FMeasure> breed(final Organism<Expr, FMeasure> a, final Organism<Expr, FMeasure> b) {
		// Clone parent A
		final Expr dna = a.getChromosome().clone(null);
		dna.visit(picker);

		// select a sub-expression for removal and replace with a subexpression
		// from parent B
		final Expr target = picker.getResult();
		final Expr parent = target.getParent();

		b.getChromosome().visit(picker);
		final Expr child = picker.getResult().clone(parent);
		if (parent != null) {
			parent.removeChild(target);
			child.setIndex(parent.getArity());
			parent.addChild(child);
		} else
			child.setIndex(-1);

		if (rand.nextDouble() < mutationRate)
			mutate(dna);

		return new Organism<Expr, FMeasure>(dna);
	}

	/**
	 * if randomly generating organisms, return an empty population.
	 * 
	 * Otherwise, the generated seeds is a single hyperbox of the variable
	 * dimensions:
	 * 
	 * AND_{i=0}^{n}(v_i >= 0 AND v_i <= 1)
	 */
	@Override
	public Collection<Organism<Expr, FMeasure>> createSeeds() {
		final List<Organism<Expr, FMeasure>> result = new LinkedList<Organism<Expr, FMeasure>>();

		if (randomGeneration) {
			return result;
		}

		// for all variables
		final LogicExpr and = new LogicExpr(null, -1);
		and.setOper(LogicOper.AND);

		for (String label : DataOracle.fields) {
			final LogicExpr pair = new LogicExpr(and, and.getArity());
			pair.setOper(LogicOper.AND);

			// v >= 0
			final CompareExpr child1 = new CompareExpr(pair, pair.getArity());
			child1.setOper(CompareOper.GE);
			child1.setIdentifier(label);
			child1.setValue(0);
			pair.addChild(child1);

			// v <= 1
			final CompareExpr child2 = new CompareExpr(pair, pair.getArity());
			child2.setOper(CompareOper.LE);
			child2.setIdentifier(label);
			child2.setValue(1);
			pair.addChild(child2);

			and.addChild(pair);
		}

		final Organism<Expr, FMeasure> org = new Organism<Expr, FMeasure>(and);
		evaluate(org);
		result.add(org);
		return result;
	}

	@Override
	protected Expr build() {
		return builder.build();
	}

	/**
	 * Run the expression over both the clean and dirty datasets
	 */
	@Override
	public FMeasure evaluate(final Expr dna) {
		final DataOracle oracle = DataOracle.getInstance();
		int falsePos = 0, falseNeg = 0, totalNeg = 0, totalPos = 0;

		for (Map<String, Double> row : oracle.getCleanTrace()) {
			if (!dna.evaluate(row))
				++falsePos;

			++totalPos;
		}

		for (Map<String, Double> row : oracle.getDirtyTrace()) {
			if (dna.evaluate(row))
				++falseNeg;

			++totalNeg;
		}

		return new FMeasure(totalPos, totalNeg, falsePos, falseNeg);
	}

	/**
	 * Perform a random point mutation
	 * 
	 * @param dna
	 */
	protected void mutate(final Expr dna) {
		final int n = rand.nextInt(3);
		NodeVisitor radiation = null;

		switch (n) {
		case 0:
			radiation = new AddRadiation(rand, builder);
			break;
		case 1:
			radiation = new CompareRadiation(rand);
			break;
		case 2:
			radiation = new OperRadiation(rand);
			break;
		}

		dna.visit(radiation);
	}
}
