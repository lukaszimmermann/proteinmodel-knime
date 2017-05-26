package org.proteinevolution.knime.nodes.blast;

import java.io.File;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.preferences.PreferencePage;

public abstract class BLASTNodeModel extends ExecutableNodeModel {

	protected BLASTNodeModel(int nrInDataPorts, int nrOutDataPorts) throws InvalidSettingsException {

		super(nrInDataPorts, nrOutDataPorts);
	}

	protected BLASTNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) throws InvalidSettingsException {

		super(inPortTypes, outPortTypes);
	}

	
	@Override
	protected final File getExecutable() {

		return new File(
				ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.BLAST_EXECUTABLE_PATH),
				this.getExecutableName());
	}
}
