package org.proteinevolution.knime.nodes.psipred;

import java.io.File;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.preferences.PreferencePage;

public abstract class PSIPREDBaseNodeModel extends ExecutableNodeModel {

	protected PSIPREDBaseNodeModel(int nrInDataPorts, int nrOutDataPorts) throws InvalidSettingsException {

		super(nrInDataPorts, nrOutDataPorts);
	}

	protected PSIPREDBaseNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) throws InvalidSettingsException {

		super(inPortTypes, outPortTypes);
	}

	
	@Override
	protected final File getExecutable() {

		return new File(
				ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.PSIPRED_EXECUTABLE_PATH),
				this.getExecutableName());
	}
}
