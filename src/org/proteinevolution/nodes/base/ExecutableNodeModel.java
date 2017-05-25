package org.proteinevolution.nodes.base;

import java.io.File;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.port.PortType;

/**
 *  This base class for node model is meant for nodes that rely on external executables to be executed.
 *  
 *  
 * @author lzimmermann
 *
 */
public abstract class ExecutableNodeModel extends NodeModel {

	protected ExecutableNodeModel(int nrInDataPorts, int nrOutDataPorts) throws InvalidSettingsException {
		
		super(nrInDataPorts, nrOutDataPorts);
		this.checkExecutable();
	}

	protected ExecutableNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) throws InvalidSettingsException {
		
		super(inPortTypes, outPortTypes);
		this.checkExecutable();
	}

	private void checkExecutable() throws InvalidSettingsException {

		File file = this.getExecutable();

		if ( ! file.exists()) {

			throw new InvalidSettingsException("File denotes by path " + file.getAbsolutePath() + " does not exist.");	
		}
		if ( ! file.isFile()) {
			
			throw new InvalidSettingsException("File denotes by path " + file.getAbsolutePath() + " is not a regular file.");	
		}
		
		if ( ! file.canExecute()) {
			
			throw new InvalidSettingsException("File denotes by path " + file.getAbsolutePath() + " is not executable.");	
		}
	}
	
	/**
	 * Returns the file name of the executable.
	 * @return File name of the executable.
	 */
	protected abstract String getExecutableName();

	/**
	 * Returns the file object with the executable for this node.
	 * 
	 * @return File which points to the executable of this node.
	 */
	protected abstract File getExecutable();
}
