package org.proteinevolution.knime.nodes.concoord;

import java.io.File;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.preferences.PreferencePage;

public abstract class ConcoordBaseNodeModel extends ExecutableNodeModel {

	protected ConcoordBaseNodeModel(int nrInDataPorts, int nrOutDataPorts) throws InvalidSettingsException {

		super(nrInDataPorts, nrOutDataPorts);
	}

	protected ConcoordBaseNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) throws InvalidSettingsException {

		super(inPortTypes, outPortTypes);
	}
	
	
	@Override
	protected final File getExecutable() {

		return new File(
				new File(
						ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.CONCOORD_PATH),
						"bin"),
				this.getExecutableName());
	}
}
