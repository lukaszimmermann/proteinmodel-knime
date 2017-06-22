package org.proteinevolution.knime.nodes.rosetta;

import java.io.File;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.preferences.PreferencePage;

public abstract class RosettaBaseNodeModel extends ExecutableNodeModel {
	
	

	protected RosettaBaseNodeModel(int nrInDataPorts, int nrOutDataPorts) throws InvalidSettingsException {

		super(nrInDataPorts, nrOutDataPorts);
	}

	protected RosettaBaseNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) throws InvalidSettingsException {

		super(inPortTypes, outPortTypes);
	}
	
	@Override
	protected final File getExecutable() {

		return new File(
				ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.ROSETTA_EXECUTABLE_PATH),
				this.getExecutableName());
	}
	
	@Override
	protected void check() throws InvalidSettingsException {
		
		// Nothing to be done here
	}
}
