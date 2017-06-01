package org.proteinevolution.knime.nodes.rosetta;

import java.io.File;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;

public abstract class RosettaBaseNodeModel extends ExecutableNodeModel {

	protected RosettaBaseNodeModel(int nrInDataPorts, int nrOutDataPorts) throws InvalidSettingsException {

		super(nrInDataPorts, nrOutDataPorts);
	}

	protected RosettaBaseNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) throws InvalidSettingsException {

		super(inPortTypes, outPortTypes);
	}

	
	@Override
	protected abstract File getExecutable();
}
