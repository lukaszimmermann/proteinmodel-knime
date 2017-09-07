package org.proteinevolution.knime;

import java.io.IOException;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;

public interface KNIMEAdapter<A, B> {

	A portToInput(final PortObject[] ports);
	PortObject[] outputToPort(final B result, final ExecutionContext exec) throws IOException;
	
	PortType[] getInputPortType();
	PortType[] getOutputPortType();
}
