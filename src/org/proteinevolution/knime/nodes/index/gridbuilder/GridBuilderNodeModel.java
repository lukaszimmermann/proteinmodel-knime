package org.proteinevolution.knime.nodes.index.gridbuilder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.blob.BinaryObjectCellFactory;
import org.knime.core.data.blob.BinaryObjectDataCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.proteinevolution.models.spec.pdb.Atom;
import org.proteinevolution.models.spec.pdb.Residue;
import org.proteinevolution.models.structure.AtomIdentification;
import org.proteinevolution.models.structure.Grid;
import org.proteinevolution.models.structure.LocalAtom;


/**
 * This is the model implementation of GridBuilder.
 * Build a grid with a certain grid spacing around a pdb structure.
 * This can currently be used for SASD calculation.
 *
 * @author Lukas Zimmermann
 */
public class GridBuilderNodeModel extends NodeModel {

	// the logger instance
	@SuppressWarnings("unused")
	private static final NodeLogger logger = NodeLogger
	.getLogger(GridBuilderNodeModel.class);

	// INPUT
	public static String INPUT_CFGKEY = "INPUT_CFGKEY";
	public static String INPUT_DEFAULT = "";
	public static String INPUT_HISTORY = "INPUT_HISTORY";
	private final SettingsModelString input = new SettingsModelString(INPUT_CFGKEY, INPUT_DEFAULT); 

	// SAS SASD compatibility
	public static String SASD_CFGKEY = "SASD_CFGKEY";
	public static boolean SASD_DEFAULT = true;
	public static String SASD_LABEL = "SASD Compatibility";


	/**
	 * Constructor for the node model.
	 */
	protected GridBuilderNodeModel() {

		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {


		// the data table spec of the single output table, 
		// the table will have three columns:
		DataColumnSpec[] allColSpecs = new DataColumnSpec[] {

				new DataColumnSpecCreator("Grid", BinaryObjectDataCell.TYPE).createSpec(),
				new DataColumnSpecCreator("x_dim", IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator("y_dim", IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator("z_dim", IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator("size", IntCell.TYPE).createSpec(),
		};
		DataTableSpec outputSpec = new DataTableSpec(allColSpecs);

		Grid grid = null;

		// TODO Donor and acceptor residues (should be parameterized)
		///// TODO -BLOCK
		Map<Residue, Set<Atom>> donors = new HashMap<Residue, Set<Atom>>();
		Map<Residue, Set<Atom>> acceptors = new HashMap<Residue, Set<Atom>>();

		Set<Atom> lys_atoms = new HashSet<Atom>();
		lys_atoms.add(Atom.NZ);

		donors.put(Residue.LYS, lys_atoms);
		acceptors.put(Residue.LYS, lys_atoms);
		// END- TODO Block


		// Read the input file in two passes.
		// First: Determine the dimensions of the grid
		BufferedReader br = new BufferedReader(new FileReader(this.input.getStringValue()));

		double lower_x = Double.MAX_VALUE;
		double lower_y = Double.MAX_VALUE;
		double lower_z = Double.MAX_VALUE;

		double upper_x = Double.MIN_VALUE;
		double upper_y = Double.MIN_VALUE;
		double upper_z = Double.MIN_VALUE;

		String line;
		while ( (line = br.readLine()) != null ) {

			// Only care about atom records
			if (Atom.isRecord(line)) {

				// Atom coordinates
				double x = Double.parseDouble(line.substring(Atom.FIELD_X_START, Atom.FIELD_X_END));
				double y = Double.parseDouble(line.substring(Atom.FIELD_Y_START, Atom.FIELD_Y_END));
				double z = Double.parseDouble(line.substring(Atom.FIELD_Z_START, Atom.FIELD_Z_END));    			

				lower_x = x < lower_x ? x : lower_x;
				lower_y = y < lower_y ? y : lower_y;
				lower_z = z < lower_z ? z : lower_z;

				upper_x = x > upper_x ? x : upper_x;
				upper_y = y > upper_y ? y : upper_y;
				upper_z = z > upper_z ? z : upper_z;        			
			}
		}
		grid = new Grid(
				lower_x,
				lower_y,
				lower_z,
				upper_x,
				upper_y,
				upper_z,
				donors,
				acceptors);

		br = null;
		br = new BufferedReader(new FileReader(this.input.getStringValue()));

	
		while ( (line = br.readLine()) != null ) {

			// Only care about atom records
			if (Atom.isRecord(line)) {
				grid.addAtom(
						new LocalAtom(
								Double.parseDouble(line.substring(Atom.FIELD_X_START, Atom.FIELD_X_END)),
								Double.parseDouble(line.substring(Atom.FIELD_Y_START, Atom.FIELD_Y_END)),
								Double.parseDouble(line.substring(Atom.FIELD_Z_START, Atom.FIELD_Z_END)),
										new AtomIdentification(
												Atom.toAtom(line.substring(Atom.FIELD_ATOM_NAME_START, Atom.FIELD_ATOM_NAME_END).trim()),
												Residue.valueOf(line.substring(Atom.FIELD_RESIDUE_NAME_START, Atom.FIELD_RESIDUE_NAME_END).trim()),
												Integer.parseInt(line.substring(Atom.FIELD_RESIDUE_SEQ_NUMBER_START, Atom.FIELD_RESIDUE_SEQ_NUMBER_END).trim()),
												line.substring(Atom.FIELD_CHAIN_IDENTIFIER_START, Atom.FIELD_CHAIN_IDENTIFIER_END).trim())));;
			}
		} 
		

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bos);
		out.writeObject(grid);
		out.flush();
		BinaryObjectCellFactory factory = new BinaryObjectCellFactory(exec);

		BufferedDataContainer container = exec.createDataContainer(outputSpec);	
		container.addRowToTable(new DefaultRow("Row0",
				new DataCell[] {

						factory.create(bos.toByteArray()),
						IntCellFactory.create(grid.getXDim()),
						IntCellFactory.create(grid.getYDim()),
						IntCellFactory.create(grid.getZDim()),
						IntCellFactory.create(grid.getSize()),
		}));

		br.close();
		bos.close();
		out.close();
		container.close();

		// Better safe than sorry
		bos = null;
		out = null;
		br  = null;
		factory = null;

		return new BufferedDataTable[]{container.getTable()};
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

		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.input.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.input.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

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
}
