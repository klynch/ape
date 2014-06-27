package edu.drexel.cs.serg.ape;

/**
 * The Fitness measure, defined by F_β = ((1 + β^2) * pos) / ((1 + β^2) * pos +
 * β^2 · fneg + fpos)
 * 
 * @author klynch
 * @since 0.1
 */
public class FMeasure extends ComparableNumber implements Cloneable {
	private static final double beta = 0.5;

	private static final long serialVersionUID = 10L;

	private final double falsePositive, falseNegative, measure;

	public FMeasure(double totalPos, double totalNeg, double falsePos, double falseNeg) {
		final double truePos = (double) (totalPos - falsePos) / (double) totalPos;
		falsePositive = (double) falsePos / (double) totalPos;
		falseNegative = (double) falseNeg / (double) totalNeg;

		final double numerator = (1 + beta * beta) * truePos;
		final double denominator = numerator + beta * beta * falseNegative + falsePositive;

		measure = numerator / denominator;

		// System.err.printf("%d %d %d %d : %f %f %f %f%n", falsePos, totalPos,
		// falseNeg, totalNeg, truePos, falsePositive, falseNegative,
		// measure);
	}

	public double getFalsePositive() {
		return falsePositive;
	}

	public double getFalseNegative() {
		return falseNegative;
	}

	public double getMeasure() {
		return measure;
	}

	@Override
	public byte byteValue() {
		return (byte) measure;
	}

	@Override
	public double doubleValue() {
		return measure;
	}

	@Override
	public float floatValue() {
		return (float) measure;
	}

	@Override
	public int intValue() {
		return (int) measure;
	}

	@Override
	public long longValue() {
		return (long) measure;
	}

	@Override
	public short shortValue() {
		return (short) measure;
	}

	@Override
	public int compareTo(final ComparableNumber other) {
		return Double.compare(measure, other.doubleValue());
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		else if (obj == null || !(obj instanceof FMeasure))
			return false;

		final FMeasure other = (FMeasure) obj;
		return this.compareTo(other) == 0;
	}

	@Override
	public int hashCode() {
		return (int) Double.doubleToLongBits(measure);
	}

	@Override
	public FMeasure clone() {
		try {
			return (FMeasure) super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new AssertionError(ex);
		}
	}

	@Override
	public String toString() {
		return String.format("%.15f %.15f %.15f", getMeasure(), getFalsePositive(), getFalseNegative());
	}
}
