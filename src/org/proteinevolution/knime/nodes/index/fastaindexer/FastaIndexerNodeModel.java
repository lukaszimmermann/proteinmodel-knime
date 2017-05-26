package org.proteinevolution.knime.nodes.index.fastaindexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.knime.base.node.util.BufferedFileReader;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.IntValue;
import org.knime.core.data.MissingCell;
import org.knime.core.data.RowKey;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.proteinevolution.knime.nodes.input.xquestreader.XQuestReaderNodeModel;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This is the model implementation of FastaIndexer.
 * Indexes cross-link identifications with a FASTA database and adds information on the absolute position and flanking residues
 *
 * @author Lukas Zimmermann
 */


public class FastaIndexerNodeModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(FastaIndexerNodeModel.class);

	// Input file (FASTA database)
	public static final String INPUT_CFGKEY = "Input File";
	public static final String INPUT_DEFAULT = "/local/work/knime_node_devel/26S.fasta";
	public static final String INPUT_HISTORYKEY = "INPUT_HISTORYKEY";
	public static final String INPUT_VALIDEXT = "fasta|fas|fa";

	// column containing pep1 sequence
	public static final String SEQ_CFGKEY = "Seq";
	public static final String SEQ_DEFAULT = "seq";  // required

	// column containing pep1 sequence
	public static final String POS_CFGKEY = "Pos";
	public static final String POS_DEFAULT = "pos";  // required

	// column containing pep1 protein ID set
	public static final String PROT_CFGKEY = "Prot";
	public static final String PROT_DEFAULT = null;   // optional (all proteins if missing)

	// Variable modification
	public static final String MOD_CFGKEY = "Mod";
	public static final String MOD_DEFAULT = "M";   // Modification (by which X should be replaced)


	// Number of columns in output table
	private static final int N_COLUMNS = 5;

	private final SettingsModelString input_file =
			new SettingsModelString(
					FastaIndexerNodeModel.INPUT_CFGKEY, 
					FastaIndexerNodeModel.INPUT_DEFAULT);

	private final SettingsModelString seq =
			new SettingsModelColumnName(
					FastaIndexerNodeModel.SEQ_CFGKEY, 
					FastaIndexerNodeModel.SEQ_DEFAULT);

	private final SettingsModelString pos =
			new SettingsModelColumnName(
					FastaIndexerNodeModel.POS_CFGKEY, 
					FastaIndexerNodeModel.POS_DEFAULT);

	private final SettingsModelString prot =
			new SettingsModelColumnName(
					FastaIndexerNodeModel.PROT_CFGKEY, 
					FastaIndexerNodeModel.PROT_DEFAULT);

	private final SettingsModelString mod =
			new SettingsModelString(
					FastaIndexerNodeModel.MOD_CFGKEY, 
					FastaIndexerNodeModel.MOD_DEFAULT);

	private int key_counter = 0;



	private Map<String, String> indexFASTA(BufferedDataTable table, int prot_index) throws IOException {

		// Index of FASTA database
		Map<String, String> sequences = new HashMap<String, String>();
		Set<String> prot_ids = new HashSet<String>();
		if (prot_index != -1) {

			for (DataRow row : table) {			
				for(DataCell cell : (SetCell) row.getCell(prot_index)) {
					prot_ids.add(cell.toString());
				}
			}
		}
		// Go through FASTA file and index the sequences
		BufferedReader br = new BufferedReader(new FileReader(this.input_file.getStringValue()));
		String line;
		StringBuilder sbuilder = new StringBuilder();
		String current_identifier = null;

		while ((line = br.readLine()) != null) {
			line = line.trim();
			// Skip empty lines
			if (line.isEmpty()) {
				continue;
			}
			if(line.startsWith(">")) {

				line = line.split("\\s+")[0];
				if (line.startsWith(">")) {
					line = line.substring(1);
				}
				if (current_identifier != null) {						
					sequences.put(current_identifier, sbuilder.toString());
				}
				sbuilder = new StringBuilder();
				current_identifier = prot_ids.contains(line) || prot_index == -1 ? line : null;

			} else {
				if (current_identifier != null) {
					sbuilder.append(line);
				}
			}	
		}
		if (current_identifier != null) {
			sequences.put(current_identifier, sbuilder.toString());
		}
		sbuilder = null;
		br.close();

		return sequences;
	} 




	/**
	 * Constructor for the node model.
	 */
	protected FastaIndexerNodeModel() {

		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		// Fetch first table on inport
		BufferedDataTable table = inData[0];
		DataTableSpec spec = table.getDataTableSpec();

		// Indices
		int seq_index = spec.findColumnIndex(this.seq.getStringValue());
		int pos_index = spec.findColumnIndex(this.pos.getStringValue());
		int prot_index = spec.findColumnIndex(this.prot.getStringValue());

		// Make index of fasta file
		Map<String, String> fasta_index = this.indexFASTA(table, prot_index);

		// Assemble result table		
		DataColumnSpec[] allColSpecs = new DataColumnSpec[N_COLUMNS];
		allColSpecs[0] = new DataColumnSpecCreator("key", StringCell.TYPE).createSpec();
		allColSpecs[1] = new DataColumnSpecCreator("abspos", IntCell.TYPE).createSpec();
		allColSpecs[2] = new DataColumnSpecCreator("flank_left", StringCell.TYPE).createSpec();
		allColSpecs[3] = new DataColumnSpecCreator("flank_right", StringCell.TYPE).createSpec();
		allColSpecs[4] = new DataColumnSpecCreator("prot", StringCell.TYPE).createSpec();
		
		DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
		BufferedDataContainer container = exec.createDataContainer(outputSpec);

		// Make annotations
		for (DataRow row: table){

			// Fetch peptideSeq
			String seq = row.getCell(seq_index).toString().toUpperCase().replace("X", this.mod.getStringValue());
			int pos = ((IntValue) row.getCell(pos_index)).getIntValue();

			// Annotate protein specific
			if (prot_index != -1) {

				for(DataCell cell : (SetCell) row.getCell(prot_index)) {

					String prot = cell.toString();
					// Skip if this protein cannot be annotated
					if (!fasta_index.containsKey(prot)) {

						//logger.warn("Protein with accession " + prot + " not in database. Annotation is ommitted");
						continue;
					}
					String protein_seq = fasta_index.get(prot).toUpperCase();
					int index_of_seq = protein_seq.indexOf(seq);

					if (index_of_seq == -1) {
						logger.warn("Peptide sequence is not part of protein sequence. Skipping.");
						continue;
					}
					// Annotate
					RowKey key = new RowKey("Row " + this.key_counter++);
					DataCell[] cells = new DataCell[N_COLUMNS];

					// Key
					cells[0] = StringCellFactory.create(row.getKey().getString());

					// absolute position (1-based) 
					cells[1] = IntCellFactory.create(index_of_seq + pos);


					int endpos = index_of_seq + seq.length();

					// flank-left
					// N-terminal 
					if (index_of_seq == 0) {

						cells[2] = new MissingCell("N-terminal");
					} else {

						cells[2] = StringCellFactory.create(String.valueOf(protein_seq.charAt(index_of_seq - 1)));
					}
					// flank-right
					// C-terminal
					if (endpos == protein_seq.length()) {

						cells[3] = new MissingCell("c-terminal");
					} else {
						cells[3] = StringCellFactory.create(String.valueOf(protein_seq.charAt(endpos)));
					}

					// Protein
					cells[4] = StringCellFactory.create(prot);
					
					container.addRowToTable(new DefaultRow(key, cells));				
					try {
						exec.checkCanceled();
					}
					catch(CanceledExecutionException e) {
					}
				}
			}
			else {

				// TODO Annotate all

			}
		}
		container.close();
		return  new BufferedDataTable[] {container.getTable()};
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

		this.input_file.saveSettingsTo(settings);
		this.seq.saveSettingsTo(settings);
		this.pos.saveSettingsTo(settings);
		this.prot.saveSettingsTo(settings);
		this.mod.saveSettingsTo(settings);
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
		this.input_file.loadSettingsFrom(settings);
		this.seq.loadSettingsFrom(settings);
		this.pos.loadSettingsFrom(settings);
		this.prot.loadSettingsFrom(settings);
		this.mod.loadSettingsFrom(settings);
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
		this.input_file.validateSettings(settings);
		this.seq.validateSettings(settings);
		this.pos.validateSettings(settings);
		this.prot.validateSettings(settings);
		this.mod.validateSettings(settings);
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

