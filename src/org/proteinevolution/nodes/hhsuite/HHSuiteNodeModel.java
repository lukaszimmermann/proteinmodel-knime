package org.proteinevolution.nodes.hhsuite;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.preferences.hhsuite.HHSuitePreferencePage;

public abstract class HHSuiteNodeModel extends NodeModel {

	protected HHSuiteNodeModel(int nrInDataPorts, int nrOutDataPorts) throws InvalidSettingsException {
		super(nrInDataPorts, nrOutDataPorts);

		File file = this.getExecutable();
		if (file == null) {

			throw new InvalidSettingsException("Executable of " + this.getExecutableName() + " could not be found. Check HHsuite configuration.");
		}
		file.setExecutable(true);
	}

	protected HHSuiteNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) throws InvalidSettingsException {
		super(inPortTypes, outPortTypes);

		File file = this.getExecutable();
		if (file == null) {

			throw new InvalidSettingsException("Executable of " + this.getExecutableName() + " could not be found. Check HHsuite configuration.");
		}
		file.setExecutable(true);
	}

	
	/**
	 * Returns the name of the executable of the HHsuite tool.
	 * 
	 * @return Name of the executable  of the HHsuite tool.
	 */
	protected abstract String getExecutableName();


	/**
	 * Returns the full absolute path to the HHsuite tool executable.
	 * If the methods fails doing so, it returns null;
	 * 
	 * @return Path to the HHsuite tool executable or null if this is not possible.
	 */
	protected final File getExecutable() {

		File file = new File(
				ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(HHSuitePreferencePage.HHSUITE_EXECUTABLE_PATH),
				this.getExecutableName());
		if ( ! file.exists() || ! file.isFile()) {

			return null;
		}
		return file;
	}
}
