package org.proteinevolution.nodes.visualization.structureviewer;

import java.io.File;
import java.io.IOException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;


/**
 * This is the model implementation of StructureViewer.
 * 
 *
 * @author Lukas Zimmermann
 */
public class StructureViewerNodeModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(StructureViewerNodeModel.class);


	// Input file with the Structure 
	public static final String INPUT_CFGKEY = "INPUT_CFGKEY";
	public static final String INPUT_DEFAULT = "";
	public static final String INPUT_LABEL = "Input structure from file";
	public static final String INPUT_HISTORY = "INPUT_HISTORY";

	// Input Structure to be rendered
	public static final String STRUC_CFGKEY = "STRUC_CFGKEY";
	public static final String STRUC_DEFAULT = "";
	public static final String STRUC_LABEL = "Input Structure";

	// Row that contains the wanted protein structure
	public static final String ROW_CFGKEY = "ROW_CFGKEY";
	public static final int ROW_DEFAULT = 0;
	public static final String ROW_LABEL = "Row";

	private final SettingsModelString struc = new SettingsModelString(STRUC_CFGKEY, STRUC_DEFAULT);
	private final SettingsModelInteger row = new SettingsModelInteger(ROW_CFGKEY, ROW_DEFAULT);
	private final SettingsModelString input = new SettingsModelString(INPUT_CFGKEY, INPUT_DEFAULT);

	// Structure of this model
	private Structure structure;
	

	/**
	 * Constructor for the node model.
	 */
	protected StructureViewerNodeModel() {

		super(1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		// TODO Maybe some more preprocessing is required
		PDBFileReader pdbr = new PDBFileReader();
		this.structure = pdbr.getStructure(new File(this.input.getStringValue()));		
		
		return null;
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {

		// TODO: check if user settings are available, fit to the incoming
		// table structure, and the incoming types are feasible for the node
		// to execute. If the node can execute in its current state return
		// the spec of its output data table(s) (if you can, otherwise an array
		// with null elements), or throw an exception with a useful user message

		// No output port
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		// TODO save user settings to the config object.
		this.struc.saveSettingsTo(settings);
		this.row.saveSettingsTo(settings);
		this.input.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.struc.loadSettingsFrom(settings);
		this.row.loadSettingsFrom(settings);
		this.input.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.struc.validateSettings(settings);
		this.row.validateSettings(settings);
		this.input.validateSettings(settings);
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
	
	public Structure getStructure() {
		return this.structure;
	}
}

