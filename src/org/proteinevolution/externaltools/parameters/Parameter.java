package org.proteinevolution.externaltools.parameters;

public abstract class Parameter<T> {

	protected T value;
	protected final String label;
	
	protected Parameter(final T def, final String label) {
		
		this.value = def;
		this.label = label;
		
		if (this.label == null || this.value == null) {
			
			throw new IllegalArgumentException("Constructor arguments of Parameters must not be null!");
		}
	}
	
	public String getLabel() {
		
		return this.label;
	}
	
	public T get() {
		
		return this.value;
	}
	
	public void set(final T value) {
		
		this.value = value;
	}
}
