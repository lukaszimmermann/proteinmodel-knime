package org.proteinevolution.externaltools.parameters;

public class DoubleBoundedParameter extends BoundedParameter<Double> {

	public DoubleBoundedParameter(final Double value, final Double lower, final Double upper, final String label) {
		super(value, lower, upper, label);
	}
}
