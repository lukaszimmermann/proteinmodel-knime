package org.proteinevolution.knime.nodes.analysis.crosslinkpredictor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.biojava.nbio.structure.AminoAcid;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureTools;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.MissingCell;
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
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;
import org.proteinevolution.models.spec.pdb.PDBAtom;
import org.proteinevolution.models.spec.pdb.Residue;
import org.proteinevolution.models.structure.AtomIdentification;
import org.proteinevolution.models.structure.UnorderedAtomPair;


/**
 * @author Lukas Zimmermann
 */
public class CrossLinkPredictorNodeModel extends NodeModel {

	// the logger instance
	@SuppressWarnings("unused")
	private static final NodeLogger logger = NodeLogger.getLogger(CrossLinkPredictorNodeModel.class);


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

	private static int addRow(
			final List<Atom> atomList1,
			final List<Atom> atomList2,
			final Map<UnorderedAtomPair, Integer> sasd_distances,
			final DataContainer container,
			int rowNumber) {

		boolean same = atomList1 == atomList2;
		int sizeList2 = atomList2.size();

		for(int i = 0; i < atomList1.size(); ++i) {

			int max = same ? i : sizeList2;
			Atom atom1 = atomList1.get(i);

			for (int j = 0; j < max; j++) {

				Atom atom2 = atomList2.get(j);
				// calculate the euclidean distance between the atoms
				double diff1 = atom1.getX() - atom2.getX();
				double diff2 = atom1.getY() - atom2.getY();
				double diff3 = atom1.getZ() - atom2.getZ();

				AtomIdentification atomIdent1 = new AtomIdentification(atom1);
				String resname1 = atomIdent1.getResidue().name();
				int resi1 = atomIdent1.getResidueSeqNum();
				String chain1 = atomIdent1.getChainId();
				String atomname1 = atomIdent1.getAtom().repr;

				AtomIdentification atomIdent2 = new AtomIdentification(atom2);
				String resname2 = atomIdent2.getResidue().name();
				int resi2 = atomIdent2.getResidueSeqNum();
				String chain2 = atomIdent2.getChainId();
				String atomname2 = atomIdent2.getAtom().repr;

				String id1 = String.join("-", resname1, String.valueOf(resi1), chain1, atomname1);
				String id2 = String.join("-", resname2, String.valueOf(resi2), chain2, atomname2);

				UnorderedAtomPair pair = new UnorderedAtomPair(atomIdent1, atomIdent2);
				DataCell sasd_cell = sasd_distances.containsKey(pair) ? DoubleCellFactory.create(sasd_distances.get(pair)) : new MissingCell("no_SASD"); 
				sasd_distances.remove(pair); // Because we are no longer interested in these distances

				// Add Row to the final data table
				container.addRowToTable(
						new DefaultRow(
								"Row"+rowNumber++,
								new DataCell[] {
										StringCellFactory.create(id1),
										StringCellFactory.create(id2),
										StringCellFactory.create(resname1),
										IntCellFactory.create(resi1),
										StringCellFactory.create(atomname1),
										StringCellFactory.create(chain1),
										StringCellFactory.create(resname2),
										IntCellFactory.create(resi2),
										StringCellFactory.create(atomname2),
										StringCellFactory.create(chain2),
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

		super(new PortType[] {StructurePortObject.TYPE},
				new PortType[] {BufferedDataTable.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Euclidean Init (Use String here, because we cannot easily convert to Residue here
		Set<String> euc_donors_arg = new HashSet<String>(Arrays.asList(this.euc_donors.getStringArrayValue()));
		Set<String> euc_acceptors_arg = new HashSet<String>(Arrays.asList(this.euc_acceptors.getStringArrayValue()));

		// TODO Donor and acceptor residues (should be parameterized)
		///// TODO -BLOCK
		Map<Residue, Set<PDBAtom>> donors = new HashMap<Residue, Set<PDBAtom>>();
		Map<Residue, Set<PDBAtom>> acceptors = new HashMap<Residue, Set<PDBAtom>>();

		Set<PDBAtom> lys_atoms = new HashSet<PDBAtom>();
		lys_atoms.add(PDBAtom.CB);

		donors.put(Residue.LYS, lys_atoms);
		acceptors.put(Residue.LYS, lys_atoms);
		// END- TODO Block

		Structure structure = ((StructurePortObject) inData[0]).getStructure().getStructureImpl(0);

		// Initialize Grid
		Grid grid = new Grid(
				structure,
				donors,
				acceptors);

		DataColumnSpec[] allColSpecs = new DataColumnSpec[] {
				new DataColumnSpecCreator("atom1", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("atom2", StringCell.TYPE).createSpec(),
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
		List<Atom> euc_donors = new ArrayList<Atom>();
		List<Atom> euc_acceptors = new ArrayList<Atom>();
		List<Atom> euc_donors_acceptors = new ArrayList<Atom>();

		// Figure out which atoms we care about
		//////////////////////////////////////////////////////////////////////////////////////////////
		Set<PDBAtom> atoms_sasd = new HashSet<PDBAtom>();
		for (Set<PDBAtom> atoms : grid.copyDonors().values()) {

			atoms_sasd.addAll(atoms);
		}
		for (Set<PDBAtom> atoms : grid.copyAcceptors().values()) {

			atoms_sasd.addAll(atoms);
		}

		// CB atom currently hard coded (TODO)
		Set<PDBAtom> atoms_euclidean = new HashSet<PDBAtom>();
		atoms_euclidean.add(PDBAtom.CB);
		//////////////////////////////////////////////////////////////////////////////////////////////

		Atom[] structureAtoms = StructureTools.getAllNonHAtomArray(structure, false);

		// Fetch the Euclidean distances
		for (Atom atom : structureAtoms) {
			
			PDBAtom pdbatom = PDBAtom.of(atom.getName());			
			AminoAcid aminoAcid = (AminoAcid) atom.getGroup();
			
			// Continue if we do not care about this atom at all
			if ( ! atoms_euclidean.contains(pdbatom)) {
				
				continue;
			}

			// Get required attributes of the atom
			double x = atom.getX();
			double y = atom.getY();
			double z = atom.getZ();			
			
			int resid = aminoAcid.getResidueNumber().getSeqNum();
			String chain = aminoAcid.getChainId();
			Residue residue = Residue.aaOf(aminoAcid.getAminoType());

			//  Type (Donor/Acceptor) for Euclidean
			boolean isEucDonor = euc_donors_arg.contains(residue.toString());
			boolean isEucAcceptor = euc_acceptors_arg.contains(residue.toString());

			if (isEucDonor || isEucAcceptor) {

				if (isEucDonor && isEucAcceptor) {

					euc_donors_acceptors.add(atom);

				} else if (isEucDonor) {

					euc_donors.add(atom);

					// Must be acceptor
				} else {

					euc_acceptors.add(atom);
				}
			}
		}

		int rowCounter = 0;

		// Perform the grid search
		grid.performBFS();
		Map<UnorderedAtomPair, Integer> sasd_distances = grid.copyDistances();

		rowCounter = addRow(euc_donors_acceptors, euc_donors_acceptors, sasd_distances, container, rowCounter);
		rowCounter = addRow(euc_donors_acceptors, euc_donors, sasd_distances, container, rowCounter);
		rowCounter = addRow(euc_donors_acceptors, euc_acceptors, sasd_distances, container, rowCounter);
		rowCounter = addRow(euc_donors, euc_acceptors, sasd_distances, container, rowCounter);


		// Add the rows for remaining distance pairs in the SASD version
		for (UnorderedAtomPair atomPair : sasd_distances.keySet()) {

			AtomIdentification atomIdent1 = atomPair.getFirst();
			String resname1 = atomIdent1.getResidue().name();
			int resi1 = atomIdent1.getResidueSeqNum();
			String chain1 = atomIdent1.getChainId();
			String atomname1 = atomIdent1.getAtom().repr;

			AtomIdentification atomIdent2 = atomPair.getSecond();
			String resname2 = atomIdent2.getResidue().name();
			int resi2 = atomIdent2.getResidueSeqNum();
			String chain2 = atomIdent2.getChainId();
			String atomname2 = atomIdent2.getAtom().repr;

			String id1 = String.join("-", resname1, String.valueOf(resi1), chain1, atomname1);
			String id2 = String.join("-", resname2, String.valueOf(resi2), chain2, atomname2);

			// Add Row to the final data table
			container.addRowToTable(
					new DefaultRow(
							"Row"+rowCounter++,
							new DataCell[] {
									StringCellFactory.create(id1),
									StringCellFactory.create(id2),
									StringCellFactory.create(resname1),
									IntCellFactory.create(resi1),
									StringCellFactory.create(atomname1),
									StringCellFactory.create(chain1),
									StringCellFactory.create(resname2),
									IntCellFactory.create(resi2),
									StringCellFactory.create(atomname2),
									StringCellFactory.create(chain2),
									new MissingCell("No Euclidean distance calculated"),
									IntCellFactory.create(sasd_distances.get(atomPair))
							}));
		}

		container.close();
		return new BufferedDataTable[]{container.getTable()};
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

		if ( ! (inSpecs[0] instanceof StructurePortObjectSpec)) {

			throw new InvalidSettingsException("Inport Type of CrossLinkPredictor must be Structure");
		}
		if (((StructurePortObjectSpec) inSpecs[0]).getNStructures() != 1) {
			
			throw new InvalidSettingsException("Only one structure allowed for crosslink prediction!");
		}

		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.euc_donors.saveSettingsTo(settings);
		this.euc_acceptors.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.euc_donors.loadSettingsFrom(settings);
		this.euc_acceptors.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.euc_donors.validateSettings(settings);
		this.euc_acceptors.validateSettings(settings);
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
