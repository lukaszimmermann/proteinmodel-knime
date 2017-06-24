package org.proteinevolution.knime.nodes.base;

import java.io.File;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
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

	private static final NodeLogger logger = NodeLogger
			.getLogger(ExecutableNodeModel.class);
	
	protected ExecutableNodeModel(int nrInDataPorts, int nrOutDataPorts) throws InvalidSettingsException {

		super(nrInDataPorts, nrOutDataPorts);

		// Checks whether the executable is fine
		this.checkExecutable();

		// Checks whether the binary of this node can be executed
		this.check();
	}

	protected abstract void check() throws InvalidSettingsException;

	protected ExecutableNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) throws InvalidSettingsException {

		super(inPortTypes, outPortTypes);
		this.checkExecutable();
		this.check();
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


	protected static Process exec(final String command, final ExecutionContext exec, final String[] envp, final File dir) throws Exception {

		logger.warn(command);
		Process process = Runtime.getRuntime().exec(command, envp, dir);
		
		while (process.isAlive()) {

			try {
				exec.checkCanceled();
			} catch (CanceledExecutionException e) {		
				process.destroy();
			}
		}
		if (process.waitFor() != 0) {
			
			throw new Exception("Excecution of Executable failed!");
		}
		return process;
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
