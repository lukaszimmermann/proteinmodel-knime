package org.proteinevolution.externaltools.parameters;

public class IntegerBoundedParameter extends BoundedParameter<Integer> {

	public IntegerBoundedParameter(final Integer value, final Integer lower, final Integer upper, final String label) {
		super(value, lower, upper, label);
	}
}
