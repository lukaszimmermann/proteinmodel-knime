package org.proteinevolution.nodes.analysis.crosslinkpredictor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.MissingCell;
import org.knime.core.data.blob.BinaryObjectFileStoreDataCell;
import org.knime.core.data.container.DataContainer;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.DoubleCell.DoubleCellFactory;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.StringCell.StringCellFactory;
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
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.proteinevolution.models.spec.pdb.Atom;
import org.proteinevolution.models.spec.pdb.Element;
import org.proteinevolution.models.spec.pdb.Residue;
import org.proteinevolution.models.structure.AtomIdentification;
import org.proteinevolution.models.structure.Grid;
import org.proteinevolution.models.structure.GridFlag;
import org.proteinevolution.models.structure.LocalAtom;
import org.proteinevolution.models.structure.UnorderedAtomPair;


/**
 * @author Lukas Zimmermann
 */
public class CrossLinkPredictorNodeModel extends NodeModel {

	// the logger instance
	@SuppressWarnings("unused")
	private static final NodeLogger logger = NodeLogger.getLogger(CrossLinkPredictorNodeModel.class);

	// Input PDB file
	public static final String INPUT_CFGKEY = "INPUT_CFGKEY";
	public static final String INPUT_DEFAULT = "";
	public static final String INPUT_HISTORY = "INPUT_HISTORY";
	private final SettingsModelString input = new SettingsModelString(INPUT_CFGKEY, INPUT_DEFAULT);


	// EUCLIDEAN
	// Donor
	public static final String EUC_DONORS_CFGKEY = "EUC_DONORS_CFGKEY";
	public static final String[] EUC_DONORS_DEFAULT = new String[] {Residue.LYS.toString()};
	public static final String EUC_DONORS_LABEL = "Donor amino acid residue to cross link";
	private SettingsModelStringArray euc_donors = new SettingsModelStringArray(EUC_DONORS_CFGKEY, EUC_DONORS_DEFAULT);

	// Acceptor
	public static final String EUC_ACCEPTORS_CFGKEY = "AA2_CFGKEY";
	public static final String[] EUC_ACCEPTORS_DEFAULT = new String[] {Residue.LYS.toString()};
	public static final String EUC_ACCEPTORS_LABEL = "Acceptor amino acid residue to cross link";
	private SettingsModelStringArray euc_acceptors = new SettingsModelStringArray(EUC_ACCEPTORS_CFGKEY, EUC_ACCEPTORS_DEFAULT);


	// Column selection for the Grid for SASD calculation
	public static final String GRID_SELECTION_CFGKEY = "GRID_SELECTION_CFGKEY";
	public static final String GRID_SELECTION_DEFAULT = "";
	public static final String GRID_SELECTION_LABEL = "Select column with protein grid";
	private final SettingsModelString grid = new SettingsModelString(GRID_SELECTION_CFGKEY, GRID_SELECTION_DEFAULT);

	// Accessibility threshold
	private static final double minAccessibility = 0.5;


	private static int addRow(
			final List<LocalAtom> atomList1,
			final List<LocalAtom> atomList2,
			final Map<UnorderedAtomPair, Integer> sasd_distances,
			final DataContainer container,
			int rowNumber) {

		boolean same = atomList1 == atomList2;
		int sizeList2 = atomList2.size();
		
		for(int i = 0; i < atomList1.size(); ++i) {
			
			int max = same ? i : sizeList2;
			LocalAtom atom1 = atomList1.get(i);
			
			for (int j = 0; j < max; j++) {
				
				LocalAtom atom2 = atomList2.get(j);
				// calculate the euclidean distance between the atoms
				double diff1 = atom1.getX() - atom2.getX();
				double diff2 = atom1.getY() - atom2.getY();
				double diff3 = atom1.getZ() - atom2.getZ();

				AtomIdentification atomIdent1 = atom1.getAtomIdentification();
				AtomIdentification atomIdent2 = atom2.getAtomIdentification();

				UnorderedAtomPair pair = new UnorderedAtomPair(atomIdent1, atomIdent2);
				DataCell sasd_cell = sasd_distances.containsKey(pair) ? DoubleCellFactory.create(sasd_distances.get(pair)) : new MissingCell("no_SASD"); 
				sasd_distances.remove(pair); // Because we are no longer interested in these distances

				// Add Row to the final data table
				container.addRowToTable(
						new DefaultRow(
								"Row"+rowNumber++,
								new DataCell[] {

										StringCellFactory.create(atomIdent1.getResidue().name()),
										IntCellFactory.create(atomIdent1.getResi()),
										StringCellFactory.create(atomIdent1.getAtom().repr),
										StringCellFactory.create(atomIdent1.getChain()),
										StringCellFactory.create(atomIdent2.getResidue().name()),
										IntCellFactory.create(atomIdent2.getResi()),
										StringCellFactory.create(atomIdent2.getAtom().repr),
										StringCellFactory.create(atomIdent2.getChain()),
										DoubleCellFactory.create(Math.sqrt(diff1*diff1 + diff2*diff2 + diff3*diff3)),
										sasd_cell
								}));		
			}
		}
		return rowNumber;
	}

	/**
	 * Constructor for the node model.
	 */
	protected CrossLinkPredictorNodeModel() {

		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		// Euclidean Init (Use String here, because we cannot easily convert to Residue here
		Set<String> euc_donors_arg = new HashSet<String>(Arrays.asList(this.euc_donors.getStringArrayValue()));
		Set<String> euc_acceptors_arg = new HashSet<String>(Arrays.asList(this.euc_acceptors.getStringArrayValue()));

		// SASD Init
		Grid grid = null;
		BufferedDataTable intable = inData[0];
		int grid_index = intable.getSpec().findColumnIndex(this.grid.getStringValue());

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (intable.size() != 1) {

			throw new IllegalArgumentException("Only one input row allowed for CrossLinkPredictor");
		}

		// Only one row here
		for (DataRow row : intable) {

			// Set the grid
			BinaryObjectFileStoreDataCell cell = (BinaryObjectFileStoreDataCell) row.getCell(grid_index);

			ObjectInputStream ois = new ObjectInputStream(cell.openInputStream());
			grid = (Grid) ois.readObject();
			ois.close();   			
		}
		if ( ! grid.isFlagSet(GridFlag.SASD_CALCULATION)) {

			throw new IllegalArgumentException("This Grid cannot be used for SASD calculation. Check GridBuilder settings.");
		}

		DataColumnSpec[] allColSpecs = new DataColumnSpec[] {

				new DataColumnSpecCreator("resname1", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("resid1", IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator("atomname1", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("chain1", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("resname2", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("resid2", IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator("atomname2", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("chain2", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("Euclidean_distance", DoubleCell.TYPE).createSpec(),
				new DataColumnSpecCreator("SASD_distance", DoubleCell.TYPE).createSpec()
		};
		DataTableSpec outputSpec = new DataTableSpec(allColSpecs);    
		BufferedDataContainer container = exec.createDataContainer(outputSpec);

		// List keeping track of the acceptor/donors residues (for Euclidean distance)
		List<LocalAtom> euc_donors = new ArrayList<LocalAtom>();
		List<LocalAtom> euc_acceptors = new ArrayList<LocalAtom>();
		List<LocalAtom> euc_donors_acceptors = new ArrayList<LocalAtom>();

		// Figure out which atoms we care about
		//////////////////////////////////////////////////////////////////////////////////////////////
		Set<Atom> atoms_sasd = new HashSet<Atom>();
		for (Set<Atom> atoms : grid.copyDonors().values()) {

			atoms_sasd.addAll(atoms);
		}
		for (Set<Atom> atoms : grid.copyAcceptors().values()) {

			atoms_sasd.addAll(atoms);
		}

		// CB atom currently hard coded (TODO)
		Set<Atom> atoms_euclidean = new HashSet<Atom>();
		atoms_euclidean.add(Atom.CB);
		//////////////////////////////////////////////////////////////////////////////////////////////


		String input_filename = this.input.getStringValue();
		BufferedReader br = new BufferedReader(new FileReader(input_filename));
		String line;
		// Fetch the Euclidean distances
		while( ( line = br.readLine()) != null  ) {

			// Skip non-ATOM line
			if ( ! Atom.isRecord(line)) {

				continue;
			}
			// Determine current Atom and residue name
			Atom atom = Atom.toAtom(line.substring(Atom.FIELD_ATOM_NAME_START, Atom.FIELD_ATOM_NAME_END).trim());

			// Skip hydrogen
			if (atom.element == Element.H) {

				continue;
			}

			// Continue if we do not care about this atom at all
			if ( ! atoms_euclidean.contains(atom) && ! atoms_sasd.contains(atom)) {
				continue;
			}

			// Get required attributes of the atom
			double x = Double.parseDouble(line.substring(Atom.FIELD_X_START, Atom.FIELD_X_END));
			double y = Double.parseDouble(line.substring(Atom.FIELD_Y_START, Atom.FIELD_Y_END));
			double z = Double.parseDouble(line.substring(Atom.FIELD_Z_START, Atom.FIELD_Z_END));
			int resid = Integer.parseInt(line.substring(Atom.FIELD_RESIDUE_SEQ_NUMBER_START, Atom.FIELD_RESIDUE_SEQ_NUMBER_END).trim());
			String chain = line.substring(Atom.FIELD_CHAIN_IDENTIFIER_START, Atom.FIELD_CHAIN_IDENTIFIER_END);
			String residueName = line.substring(Atom.FIELD_RESIDUE_NAME_START, Atom.FIELD_RESIDUE_NAME_END).trim();


			//  Type (Donor/Acceptor) for Euclidean
			boolean isEucDonor = euc_donors_arg.contains(residueName);
			boolean isEucAcceptor = euc_acceptors_arg.contains(residueName);

			if (isEucDonor || isEucAcceptor) {

				LocalAtom currentAtom = new LocalAtom(x,y,z, new AtomIdentification(atom, Residue.valueOf(residueName), resid, chain));

				if (isEucDonor && isEucAcceptor) {

					euc_donors_acceptors.add(currentAtom);

				} else if (isEucDonor) {

					euc_donors.add(currentAtom);

					// Must be acceptor
				} else {

					euc_acceptors.add(currentAtom);
				}
			}
		}
		br.close();
		br = null;

		int rowCounter = 0;

		// Perform the grid search
		grid.performBFS(40);
		Map<UnorderedAtomPair, Integer> sasd_distances = grid.copyDistances();
		
		rowCounter = addRow(euc_donors_acceptors, euc_donors_acceptors, sasd_distances, container, rowCounter);
		rowCounter = addRow(euc_donors_acceptors, euc_donors, sasd_distances, container, rowCounter);
		rowCounter = addRow(euc_donors_acceptors, euc_acceptors, sasd_distances, container, rowCounter);
		rowCounter = addRow(euc_donors, euc_acceptors, sasd_distances, container, rowCounter);

		container.close();
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

		// TODO save user settings to the config object.
		this.input.saveSettingsTo(settings);
		this.euc_donors.saveSettingsTo(settings);
		this.euc_acceptors.saveSettingsTo(settings);
		this.grid.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		// TODO load (valid) settings from the config object.
		// It can be safely assumed that the settings are valided by the 
		// method below.

		this.input.loadSettingsFrom(settings);
		this.euc_donors.loadSettingsFrom(settings);
		this.euc_acceptors.loadSettingsFrom(settings);
		this.grid.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		// TODO check if the settings could be applied to our model
		// e.g. if the count is in a certain range (which is ensured by the
		// SettingsModel).
		// Do not actually set any values of any member variables.

		this.input.validateSettings(settings);
		this.euc_donors.validateSettings(settings);
		this.euc_acceptors.validateSettings(settings);
		this.grid.validateSettings(settings);
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
