package org.proteinevolution.knime.nodes.output.pdbwriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;


/**
 * This is the model implementation of PDBWriter.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBWriterNodeModel extends NodeModel {

	
	// Param: output file:
	public static SettingsModelString getParamOutput() {

		return new SettingsModelString("OUTPUT_CFGKEY", "");
	}
	private final SettingsModelString param_output = getParamOutput();

	// Param: Omit hetero
	public static SettingsModelBoolean getParamOmitHetero() {

		return new SettingsModelBoolean("OMIT_HETERO_CFGKEY", false);
	}
	private final SettingsModelBoolean param_omit_hetero = getParamOmitHetero();

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(PDBWriterNodeModel.class);

	/**
	 * Constructor for the node model.
	 */
	protected PDBWriterNodeModel() {

		super(new PortType[] {StructurePortObject.TYPE},
				new PortType[] {});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		StructureContent structureContent = ((StructurePortObject) inData[0]).getStructure();
		structureContent.setOmitHET(this.param_omit_hetero.getBooleanValue());
		
		FileWriter fw = new FileWriter(this.param_output.getStringValue());
		structureContent.write(fw);
		fw.close();
		return null;
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

		return new DataTableSpec[]{};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.param_output.saveSettingsTo(settings);
		this.param_omit_hetero.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_output.loadSettingsFrom(settings);
		this.param_omit_hetero.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_output.validateSettings(settings);
		this.param_omit_hetero.validateSettings(settings);
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
