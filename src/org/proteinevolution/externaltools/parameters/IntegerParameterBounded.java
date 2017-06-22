package org.proteinevolution.externaltools.parameters;

import java.util.Objects;

public final class IntegerParameterBounded implements Parameter {

	private int value;

	private final int lower;
	private final int upper;

	public IntegerParameterBounded(final int value, final int lower, final int upper) {

		this.value = value;
		this.lower = lower;
		this.upper = upper;

		if (this.upper < this.lower) {

			throw new IllegalArgumentException("Upper bound cannot be smaller than lower bound");
		}
		if (this.value < this.lower) {

			throw new IllegalArgumentException("Default value cannot be smaller than lower bound");
		}
		if (this.value > this.upper) {

			throw new IllegalArgumentException("Default value cannot be larger than upper bound");
		}
	}

	public double get() {

		return this.value;
	}
	public void set(final int value) {

		if (value < this.lower) {

			throw new IllegalArgumentException("Value cannot be set smaller then lower bound");
			
		} else if (value > this.upper) {

			throw new IllegalArgumentException("Value cannot be set larger then upper bound");
			
		} else {
			
			this.value = value;
		}
	}

	@Override
	public String toString(){

		return String.valueOf(this.value);
	}

	@Override
	public  boolean equals(final Object o) {

		if (o == this) {

			return true;
		}
		if ( ! (o instanceof IntegerParameterBounded)) {

			return false;
		}
		IntegerParameterBounded other = (IntegerParameterBounded) o;
		return     this.value == other.value
				&& this.lower == other.lower
				&& this.upper == other.upper;
	}

	@Override
	public int hashCode() {

		return Objects.hash(this.value);
	}
}
