package org.proteinevolution.externaltools.parameters.validators;

public interface Validator<T> {

	void validate(final T value) throws IllegalArgumentException;
}
