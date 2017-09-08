package org.proteinevolution.externaltools.parameters;

import org.proteinevolution.externaltools.parameters.renderer.Renderer;
import org.proteinevolution.externaltools.parameters.validators.Validator;

public class Parameter<T> {

	protected T value;
	protected final String label;
	private final Renderer<T> renderer;
	private final Validator<T> validator;
		
	
	public Parameter(final T def, final String label, final Renderer<T> renderer, final Validator<T> validator) {
		
		this.value = def;
		this.label = label;
		
		this.renderer = renderer;
		this.validator = validator;
		
		if (this.label == null || this.value == null) {
			
			throw new IllegalArgumentException("Constructor arguments of Parameters must not be null!");
		}
		
		// Validate if possible
		if (this.validator != null) {
			
			this.validator.validate(this.value);
		}
	}
	
	public Parameter(final T def, final String label, final Renderer<T> renderer) {
		
		this(def, label, renderer, null);
	}
	
	
	public Parameter(final T def, final String label, final Validator<T> validator) {
		
		this(def, label, null, validator);
	}
	
	public Parameter(final T def, final String label) {
		
		this(def, label, null, null);
	}
	
	public String getLabel() {
		
		return this.label;
	}
	
	public T get() {
		
		return (this.renderer == null) ? this.value : this.renderer.render(value); 
	}
	
	public Validator<T> getValidator() {
		
		return this.validator;
	}
	
	public void set(final T value) {
		
		this.value = value;
	}
}
