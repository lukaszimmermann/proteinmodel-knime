package org.proteinevolution.knime.nodes.concoord.disco;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.knime.nodes.concoord.ConcoordBaseNodeModel;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;
import org.proteinevolution.models.util.CommandLine;



/**
 * This is the model implementation of ConcoordDisco.
 * 
 *
 * @author Lukas Zimmermann
 */
public class ConcoordDiscoNodeModel extends ConcoordBaseNodeModel {


	private static final NodeLogger logger = NodeLogger
			.getLogger(ConcoordDiscoNodeModel.class);
	
	
	// Param: Selection of atomsMargin (VdW parameters)
	public static SettingsModelString getParamAtomsMargin() {

		return new SettingsModelString("ATOMS_MARGIN_CFGKEY", "OPLS-UA (united atoms)");
	}
	private final SettingsModelString param_atoms_margin = getParamAtomsMargin();


	// Param: Selection of bonds/angles
	public static SettingsModelString getParamBonds() {

		return new SettingsModelString("BONDS_CFGKEY", "Concoord default");
	}
	private final SettingsModelString param_bonds = getParamBonds();
	

	// Param: Number of structures to be generated
	public static SettingsModelIntegerBounded getParamNoStructures() {

		return new SettingsModelIntegerBounded("NO_STRUCTURES_CFGKEY", 500, 1, 1000);
	}
	private final SettingsModelIntegerBounded param_no_structures = getParamNoStructures();
	

	/**
	 * Constructor for the node model.
	 */
	protected ConcoordDiscoNodeModel() throws InvalidSettingsException {

		super(new PortType[]   {StructurePortObject.TYPE, BufferedDataTable.TYPE},
				new PortType[] {StructurePortObject.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Write
		DataTable dataTable = (DataTable) inData[1];
		DataTableSpec dataTableSpec = dataTable.getDataTableSpec();
		
		// Figure out the indices of the columns in the data table
		int[] indices = new int[namesDist.length];

		for (int i = 0; i < namesDist.length; ++i) {

			indices[i] = dataTableSpec.findColumnIndex(namesDist[i]);
		}
		// Write distFile
		File distFile = ConcoordBaseNodeModel.writeDistFile(dataTable);
		
		StructureContent structureContent;
		try(CommandLine cmd = new CommandLine(this.getExecutable())) {
			
			// Output directory for the PDB structures
			File outputPDB = Files.createTempDirectory("discoStructures").toFile();
			
			cmd.addOption("-d", distFile.getAbsolutePath());
			cmd.addInput("-p", ((StructurePortObject) inData[0]).getStructureContent());
			cmd.addOption("-op", new File(outputPDB, "prefix").getAbsolutePath());
			cmd.addOption("-n", this.param_no_structures.getIntValue());
			
			File tempLib = ConcoordBaseNodeModel.createTempLib(
					exec,
					this.param_atoms_margin.getStringValue(),
					this.param_bonds.getStringValue());
			
			ExecutableNodeModel.exec(cmd.toString(), exec, new String[] {
					String.format("CONCOORDLIB=%s", tempLib.getAbsoluteFile())	
			}, Files.createTempDirectory("concoord_work").toFile());
			
			structureContent = StructureContent.fromDirectory(outputPDB.getAbsolutePath());
			FileUtil.deleteRecursively(outputPDB);
		}
		
		return new StructurePortObject[] {
				
				new StructurePortObject(
						structureContent,
						new StructurePortObjectSpec(StructureContent.TYPE,
								structureContent.getNumberOfStructures()))
		};
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

		if ( ! (inSpecs[0] instanceof StructurePortObjectSpec)) {

			throw new InvalidSettingsException("Port type 0 of ConcoordDisco must be structure!");
		}
		
		if ( ((StructurePortObjectSpec) inSpecs[0]).getNStructures() != 1) {

			throw new InvalidSettingsException("Only one Structure allowed for ConcoordDisco!");
		}
		
		if ( ! (inSpecs[1] instanceof DataTableSpec)) {

			throw new InvalidSettingsException("Port type 1 of ConcoordDisco must be data table!");
		}
		DataTableSpec distSpec = (DataTableSpec) inSpecs[1];

		// Check that all column names are present
		for (String name : namesDist) {
			
			if ( ! distSpec.containsName(name)) {
				
				throw new InvalidSettingsException("Column name " + name + " missing in input table!");
			}
		}
	
		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		
		this.param_atoms_margin.saveSettingsTo(settings);
		this.param_bonds.saveSettingsTo(settings);
		this.param_no_structures.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		
		this.param_atoms_margin.loadSettingsFrom(settings);
		this.param_bonds.loadSettingsFrom(settings);
		this.param_no_structures.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {            
		
		this.param_atoms_margin.validateSettings(settings);
		this.param_bonds.validateSettings(settings);
		this.param_no_structures.validateSettings(settings);
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

		return "disco";
	}
}
