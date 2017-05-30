package org.proteinevolution.knime.nodes.psipred.chkparse;

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
import org.proteinevolution.knime.nodes.psipred.PSIPREDNodeModel;
import org.proteinevolution.models.util.CommandLine;


/**
 * This is the model implementation of Chkparse.
 * 
 *
 * @author Lukas Zimmermann
 */
public class ChkparseNodeModel extends PSIPREDNodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(ChkparseNodeModel.class);



	/**
	 * Constructor for the node model.
	 */
	protected ChkparseNodeModel() throws InvalidSettingsException {

		super(new PortType[] {PortTypeRegistry.getInstance().getPortType(URIPortObject.class)},
				new PortType[] {PortTypeRegistry.getInstance().getPortType(URIPortObject.class)});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Fetch input properties
		URIPortObject in = (URIPortObject) inData[0];

		File child = null;
		List<URIContent> urics = new ArrayList<URIContent>(1);

		try(CommandLine cmd = new CommandLine(this.getExecutable())) {

			cmd.addOption("", in.getURIContents().get(0).getURI().getPath());

			child = new File(exec.createFileStore("chkparse").getFile(), "out");
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

				throw new ExecutionException("Execution of Chkparse failed.");
			}			
			FileUtils.copyInputStreamToFile(process.getInputStream(), child);
			urics.add(new URIContent(child.toURI(), ".mtx"));	
		}
		return new URIPortObject[] {

				new URIPortObject(new URIPortObjectSpec(".mtx"), urics)};
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

			throw new InvalidSettingsException("Chkparse node only expects exactly one file, but either none or multiple extensions were encountered.");
		}
		if ( ! extensions.get(0).equals(".chk")) {

			throw new InvalidSettingsException("Chkparse expects file with extension .chk, but extension was: " + extensions.get(0));
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

		// TODO load internal data. 
		// Everything handed to output ports is loaded automatically (data
		// returned by the execute method, models loaded in loadModelContent,
		// and user settings set through loadSettingsFrom - is all taken care 
		// of). Load here only the other internals that need to be restored
		// (e.g. data used by the views).

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

		// TODO save internal models. 
		// Everything written to output ports is saved automatically (data
		// returned by the execute method, models saved in the saveModelContent,
		// and user settings saved through saveSettingsTo - is all taken care 
		// of). Save here only the other internals that need to be preserved
		// (e.g. data used by the views).

	}

	@Override
	protected String getExecutableName() {

		return "chkparse";
	}
}

