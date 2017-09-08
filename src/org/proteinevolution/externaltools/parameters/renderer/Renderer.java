package org.proteinevolution.externaltools.parameters.renderer;

@FunctionalInterface
public interface Renderer<T> {

	T render(final T value);
}
