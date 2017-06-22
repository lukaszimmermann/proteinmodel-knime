package org.proteinevolution.externaltools.parameters;

import java.util.Objects;

public final class DoubleParameter implements Parameter {

	private double value;
	
	public DoubleParameter(final double value) {
		
		this.value = value;
	}
	
	public double get() {
		
		return this.value;
	}
	public void set(final double value) {
		
		this.value = value;
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
		if ( ! (o instanceof DoubleParameter)) {
			
			return false;
		}
		return this.value == ((DoubleParameter) o).value;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(this.value);
	}
}
