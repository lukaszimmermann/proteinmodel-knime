package org.proteinevolution.knime;

import java.io.IOException;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;

public interface KNIMEAdapter<A, B> {

	public A portToInput(final PortObject[] ports);
	public PortObject[] resultToPort(final B result, final ExecutionContext exec) throws IOException;
	
	public PortType[] getInputPortType();
	public PortType[] getOutputPortType();
}
