package org.proteinevolution.knime.nodes.input.pdbreader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;


/**
 * This is the model implementation of PDBReader.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBReaderNodeModel extends NodeModel {

	// INPUT PDB file
	public static final String INPUT_CFGKEY = "INPUT";
	public static final String INPUT_HISTORY = "INPUT_HISTORY";
	public static final String DEFAULT = "";
	private final SettingsModelString param_input = new SettingsModelString(INPUT_CFGKEY, DEFAULT);

	/**
	 * Constructor for the node model.
	 */
	protected PDBReaderNodeModel() {

		super(new PortType[] {},
				new PortType[] {StructurePortObject.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		List<List<String>> input = new ArrayList<List<String>>(1);
		input.add(Files.readAllLines(Paths.get(this.param_input.getStringValue()), StandardCharsets.UTF_8));
		
		return new StructurePortObject[] {

				new StructurePortObject(
						new StructureContent(input),
						new StructurePortObjectSpec(StructureContent.TYPE, 1))	
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	
		// Nothing to do here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {

		return new PortObjectSpec[]{new StructurePortObjectSpec(StructureContent.TYPE, 1)};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.param_input.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_input.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_input.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

		// Nothing to do here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

		// Nothing to do here
	}
}
