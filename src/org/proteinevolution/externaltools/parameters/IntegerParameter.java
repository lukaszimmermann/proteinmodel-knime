package org.proteinevolution.externaltools.parameters;

import java.util.Objects;

public final class IntegerParameter implements Parameter {

	private int value;
	
	public IntegerParameter(final int value) {
		
		this.value = value;
	}
	
	public int get() {
		
		return this.value;
	}
	public void set(final int value) {
		
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
		if ( ! (o instanceof IntegerParameter)) {
			
			return false;
		}
		return this.value == ((IntegerParameter) o).value;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(this.value);
	}
}
