package org.proteinevolution.externaltools.parameters.renderer;

public interface Renderer<T> {

	default T render(final T value) {
		
		return value;
	}
}
