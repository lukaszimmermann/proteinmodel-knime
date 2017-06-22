package org.proteinevolution.externaltools.parameters;

import java.util.Objects;

public final class StringParameter implements Parameter {

	private String value;
	
	public StringParameter(final String value) {
		
		this.value = value;
	}
	
	public String get() {
		
		return this.value;
	}
	public void set(final String value) {
		
		this.value = value;
	}
	
	@Override
	public String toString(){
		
		return this.value;
	}
	
	@Override
	public  boolean equals(final Object o) {
		
		if (o == this) {
			
			return true;
		}
		if ( ! (o instanceof StringParameter)) {
			
			return false;
		}
		return this.value.equals(((StringParameter) o).value);
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(this.value);
	}
}
