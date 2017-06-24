package org.proteinevolution.externaltools.parameters;

class BoundedParameter<T extends Comparable<T>> extends Parameter<T> {

	private final T lower;
	private final T upper;
	
	protected BoundedParameter(final T value, final T lower, final T upper, final String label) {
		super(value, label);
		
		this.lower = lower;
		this.upper = upper;
		
		if (this.lower == null || this.upper == null) {
			
			throw new IllegalArgumentException("Bounds of bounded parameters are not allowed to be null!");
		}
		
		if (this.lower.compareTo(this.upper) == 1) {
			
			throw new IllegalArgumentException("Lower bound is larger than upper bound!");
		}
		
		if (this.value.compareTo(this.lower) == -1  || this.value.compareTo(this.upper) == 1) {
			
			throw new IllegalArgumentException("Setting bounded parameter to value " + value + " violated bounds!");
		}
	}
	
	public T getLower() {
		
		return this.lower;
	}
	
	public T getUpper() {
		
		return this.upper;
	}
	
	@Override
	public void set(final T value) {
	
		if (this.value.compareTo(this.lower) == -1  || this.value.compareTo(this.upper) == 1) {
		
			throw new IllegalArgumentException("Setting bounded parameter to value " + value + " violated bounds!");
			
		} else {
			
			this.value = value;
		}
	}
}
