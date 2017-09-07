package org.proteinevolution.externaltools.parameters.validators;

public class RangeValidator<T extends Comparable<T>> implements Validator<T> {

	private final T lower;
	private final T upper;
	
	public static final RangeValidator<Double> percentage = new RangeValidator<>(0.0, 100.0);
	public static final RangeValidator<Double> probability = new RangeValidator<>(0.0, 1.0);
	
	public static final RangeValidator<Integer> natural = new RangeValidator<Integer>(0, Integer.MAX_VALUE);
	
	public RangeValidator(final T lower, final T upper)  {
		
		this.lower = lower;
		this.upper = upper;
		
		if (this.lower == null || this.upper == null) {
			
			throw new IllegalArgumentException("Bounds of RangeValidator are not allowed to be null!");
		}
		
		if (this.lower.compareTo(this.upper) == 1) {
			
			throw new IllegalArgumentException("Lower bound is larger than upper bound!");
		}	
	}
	
	@Override
	public void validate(final T value) throws IllegalArgumentException {
	
		if (value.compareTo(this.lower) == -1  || value.compareTo(this.upper) == 1) {
		
			throw new IllegalArgumentException("Setting bounded parameter to value " + value + " violates bounds!");	
		}
	}
	
	public T getLower() {
		
		return this.lower;
	}
	
	public T getUpper() {
		
		return this.upper;
	}
}
