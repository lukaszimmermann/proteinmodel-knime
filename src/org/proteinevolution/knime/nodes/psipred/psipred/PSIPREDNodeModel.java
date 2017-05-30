package org.proteinevolution.knime.nodes.psipred.psipred;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.commands.ExecutionException;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.uri.URIContent;
import org.knime.core.data.uri.URIPortObject;
import org.knime.core.data.uri.URIPortObjectSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.proteinevolution.knime.nodes.psipred.PSIPREDBaseNodeModel;
import org.proteinevolution.models.util.CommandLine;


/**
 * This is the model implementation of Chkparse.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PSIPREDNodeModel extends PSIPREDBaseNodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(PSIPREDNodeModel.class);



	/**
	 * Constructor for the node model.
	 */
	protected PSIPREDNodeModel() throws InvalidSettingsException {

		super(new PortType[] {PortTypeRegistry.getInstance().getPortType(URIPortObject.class)},
				new PortType[] {PortTypeRegistry.getInstance().getPortType(URIPortObject.class)});
	}

	
	
	// MUST execute: $execdir/psipred $tmproot.mtx $datadir/weights.dat $datadir/weights.dat2 $datadir/weights.dat3 > $rootname.ss
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {
		
		File child = null;
		List<URIContent> urics = new ArrayList<URIContent>(1);
		
		File executable = this.getExecutable();
	
		// Make assumption on location of datadir (TODO maybe not optimal)
		File datadir = new File(executable.getParentFile().getParent(), "data");
		
		try(CommandLine cmd = new CommandLine(executable)) {

			cmd.addOption("", ((URIPortObject) inData[0]).getURIContents().get(0).getURI().getPath());
			
			// add weights
			cmd.addOption("", new File(datadir, "weights.dat").getAbsolutePath());
			cmd.addOption("", new File(datadir, "weights.dat2").getAbsolutePath());
			cmd.addOption("", new File(datadir, "weights.dat3").getAbsolutePath());
			

			child = new File(exec.createFileStore("psipred").getFile(), "out");
			Process process = Runtime.getRuntime().exec(cmd.toString());
			logger.warn(cmd.toString());
			
			// TODO Might not work on the cluster
			while( process.isAlive() ) {

				try {	
					exec.checkCanceled();
					
				}  catch(CanceledExecutionException e) {

					process.destroy();
				}
			}
			if ( process.waitFor() != 0) {

				throw new ExecutionException("Execution of psipred failed.");
			}			
			FileUtils.copyInputStreamToFile(process.getInputStream(), child);
			urics.add(new URIContent(child.toURI(), ".ss"));	
		}
		return new URIPortObject[] {

				new URIPortObject(new URIPortObjectSpec(".ss"), urics)};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// TODO Code executed on reset.
		// Models build during execute are cleared here.
		// Also data handled in load/saveInternals will be erased here.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {

		// Validate some properties
		List<String> extensions = ((URIPortObjectSpec) inSpecs[0]).getFileExtensions();

		if (extensions.size() != 1) {

			throw new InvalidSettingsException("psipred node only expects exactly one file, but either none or multiple extensions were encountered.");
		}
		if ( ! extensions.get(0).equals(".mtx")) {

			throw new InvalidSettingsException("psipred expects file with extension .mtx, but extension was: " + extensions.get(0));
		}

		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

	}

	@Override
	protected String getExecutableName() {

		return "psipred";
	}
}

