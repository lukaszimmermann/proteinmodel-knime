package org.proteinevolution.knime.nodes.psipred.chkparse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.knime.nodes.psipred.PSIPREDBaseNodeModel;
import org.proteinevolution.models.spec.FileExtensions;
import org.proteinevolution.models.util.URIUtils;


/**
 * This is the model implementation of Chkparse.
 * 
 *
 * @author Lukas Zimmermann
 */
public class ChkparseNodeModel extends PSIPREDBaseNodeModel {

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
			
			Process process = ExecutableNodeModel.exec(cmd.toString(), exec, null, null);	
			FileUtils.copyInputStreamToFile(process.getInputStream(), child);
			urics.add(new URIContent(child.toURI(), FileExtensions.MTX));	
		}
		return new URIPortObject[] {

				new URIPortObject(new URIPortObjectSpec(FileExtensions.MTX), urics)};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {

		URIUtils.checkURIExtension(inSpecs[0], FileExtensions.CHK);
		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		// Node has no settings.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		// Node has no settings.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		// Node has no settings
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

		// Node has no internals
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

		// Node has no internals
	}

	@Override
	protected String getExecutableName() {

		return "chkparse";
	}
}
