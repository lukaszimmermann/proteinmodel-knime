package org.proteinevolution.nodes.hhsuite.hhblits;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.commands.ExecutionException;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
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
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.models.knime.alignment.SequenceAlignment;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.models.knime.hhsuitedb.HHsuiteDB;
import org.proteinevolution.models.knime.hhsuitedb.HHsuiteDBPortObject;
import org.proteinevolution.models.spec.HHR;
import org.proteinevolution.nodes.hhsuite.HHSuiteNodeModel;




/*
 hhblits 
  -cpu 8 
  -v 2
  -i #{@basename}.resub_domain.a2m
   #{@E_hhblits} 
   -d #{HHBLITS_DB}
   -o #{@basename}.hhblits 
   -oa3m #{a3mFile} -n #{@maxhhblitsit}
    -mact 0.35  
 */

/**
 * This is the model implementation of HHblits.
 * 
 *
 * @author Lukas Zimmermann
 */
public class HHblitsNodeModel extends HHSuiteNodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(HHblitsNodeModel.class);

	// HHsuite database
	public static final String HHSUITEDB_CFGKEY = "HHSUITEDB";
	public static final String[] HHSUITEDB_DEFAULT = new String[0];
	private final SettingsModelStringArray param_hhsuitedb = new SettingsModelStringArray(HHSUITEDB_CFGKEY, HHSUITEDB_DEFAULT);
	
	// No. of iterations
	public static final String NITERATIONS_CFGKEY = "NITERATIONS";
	public static final String NITERATIONS_DEFAULT = "2";
	private final SettingsModelString param_niterations = new SettingsModelString(NITERATIONS_CFGKEY, NITERATIONS_DEFAULT);
	
	// E-value cutoff
	public static final String EVALUE_CFGKEY = "EVALUE";
	public static final double EVALUE_DEFAULT = 0.001;
	public static final double EVALUE_MIN = 0;
	public static final double EVALUE_MAX = 1;
	private final SettingsModelDoubleBounded param_evalue = new SettingsModelDoubleBounded(EVALUE_CFGKEY, EVALUE_DEFAULT, EVALUE_MIN, EVALUE_MAX);
	
	// Min coverage with master
	public static final String QID_CFGKEY = "QID";
	public static final double QID_DEFAULT = 0;
	public static final double QID_MIN = 0;
	public static final double QID_MAX = 100;
	private final SettingsModelDoubleBounded param_qid = new SettingsModelDoubleBounded(QID_CFGKEY, QID_DEFAULT, QID_MIN, QID_MAX);

	// Min coverage with master
	public static final String COV_CFGKEY = "COV";
	public static final double COV_DEFAULT = 0;
	public static final double COV_MIN = 0;
	public static final double COV_MAX = 100;
	private final SettingsModelDoubleBounded param_cov = new SettingsModelDoubleBounded(COV_CFGKEY, COV_DEFAULT, COV_MIN, COV_MAX);


	/**
	 * Constructor for the node model.
	 */
	protected HHblitsNodeModel() throws InvalidSettingsException {

		super(new PortType[] {SequenceAlignmentPortObject.TYPE, HHsuiteDBPortObject.TYPE},
				new PortType[] {BufferedDataTable.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Get the alignment and the hhsuite database
		SequenceAlignment sequenceAlignment = ((SequenceAlignmentPortObject) inData[0]).getAlignment();
		HHsuiteDB hhsuitedb = ((HHsuiteDBPortObject) inData[1]).getHHsuiteDB();
	

		// Write sequenceAlignment as FASTA file into temporary file
		File inputFile = this.getTempFile(".fasta");

		FileWriter fw = new FileWriter(inputFile);
		sequenceAlignment.writeFASTA(fw);
		fw.close(); 

		// Start HHblits
		File execFile = this.getExecutable();
		
		// Build up command-line
		StringBuilder commandLine = new StringBuilder(execFile.getAbsolutePath());
		
		// Append input alignment
		commandLine.append(" -i ");
		commandLine.append(inputFile.getAbsolutePath());
		
		// Append databases which are to be searched
		for (String dbname : this.param_hhsuitedb.getStringArrayValue()) {
			
			commandLine.append(" -d ");
			commandLine.append(hhsuitedb.getPrefix(dbname));
		}
		
		// Append number of iterations
		commandLine.append(" -n ");
		commandLine.append(this.param_niterations.getStringValue());
	
		// Append e-value cutoff
		commandLine.append(" -e ");
		commandLine.append(this.param_evalue.getDoubleValue());
	
		// Append min sequence identity
		commandLine.append(" -qid ");
		commandLine.append(this.param_qid.getDoubleValue());
		
		// Append min sequence coverage
		commandLine.append(" -cov ");
		commandLine.append(this.param_cov.getDoubleValue());
		

		// Append standard output to the command line call
		File outputFile = this.getTempFile(".hhr");
		commandLine.append(" -o ");
		commandLine.append(outputFile.getAbsolutePath());
	
		String commandLineString = commandLine.toString();
		
		logger.warn(commandLine);
		
		Process p = Runtime.getRuntime().exec(commandLineString);

		// Executing HHBlits
		int statusCode = p.waitFor();

		
		DataColumnSpec[] allColSpecs = new DataColumnSpec[] {
			
				new DataColumnSpecCreator("No", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Hit", StringCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Probability", DoubleCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("E-value", DoubleCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("P-value", DoubleCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Score", DoubleCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("SS", DoubleCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Cols", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Query_start", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Query_end", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Template_start", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Template_end", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Ref", IntCell.TYPE ).createSpec(),
		};
		DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
		BufferedDataContainer container = exec.createDataContainer(outputSpec);
		
		try(BufferedReader br = new BufferedReader(new FileReader(outputFile))) {
						
			if ( statusCode != 0) {
				
				throw new ExecutionException("Execution of HHblits failed.");
			}
			
			byte state = 0;
			int rowCounter = 0;

			String line;
			while ((line = br.readLine()) != null) {
			
				// Empty line before hitlist
				if (line.trim().isEmpty() && state == 0) {
					
					// Advance by one line, we do not care about the header
					br.readLine();					
					state = 1;
					
				// read a line of the header block of the HHR file
				} else if (state == 1 && ! line.trim().isEmpty()) {
				
					String ref = line.substring(HHR.REF_START).trim();
					ref = ref.substring(1, ref.length() - 1);
					
					// Add line to data table
					container.addRowToTable(
							new DefaultRow(
									"Row"+rowCounter++,
									new DataCell[] {
											
											IntCellFactory.create(line.substring(HHR.NO_START,HHR.NO_END).trim()),
											StringCellFactory.create(line.substring(HHR.HIT_START, HHR.HIT_END).trim()),
											DoubleCellFactory.create(line.substring(HHR.PROB_START, HHR.PROB_END).trim()),
											DoubleCellFactory.create(line.substring(HHR.EVAL_START, HHR.EVAL_END).trim()),
											DoubleCellFactory.create(line.substring(HHR.PVAL_START, HHR.PVAL_END).trim()),
											DoubleCellFactory.create(line.substring(HHR.SCORE_START, HHR.SCORE_END).trim()),
											DoubleCellFactory.create(line.substring(HHR.SS_START, HHR.SS_END).trim()),
											IntCellFactory.create(line.substring(HHR.COLS_START, HHR.COLS_END).trim()),
											IntCellFactory.create(line.substring(HHR.QUERY_START_START, HHR.QUERY_START_END).trim()),
											IntCellFactory.create(line.substring(HHR.QUERY_END_START, HHR.QUERY_END_END).trim()),
											IntCellFactory.create(line.substring(HHR.TEMPLATE_START_START, HHR.TEMPLATE_START_END).trim()),
											IntCellFactory.create(line.substring(HHR.TEMPLATE_END_START, HHR.TEMPLATE_END_END).trim()),
											IntCellFactory.create(ref)
									}));
					
					
				// end of header block in HHR file
				} else if (state == 1) {
					
					break;
				}
			}
			
			
		} finally {
			
			inputFile.delete();
			outputFile.delete();
			container.close();
		}		
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
	protected DataTableSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {

		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.param_hhsuitedb.saveSettingsTo(settings);
		this.param_niterations.saveSettingsTo(settings);
		this.param_evalue.saveSettingsTo(settings);
		this.param_qid.saveSettingsTo(settings);
		this.param_cov.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_hhsuitedb.loadSettingsFrom(settings);
		this.param_niterations.loadSettingsFrom(settings);
		this.param_evalue.loadSettingsFrom(settings);
		this.param_qid.loadSettingsFrom(settings);
		this.param_cov.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
			
		this.param_hhsuitedb.validateSettings(settings);
		this.param_niterations.validateSettings(settings);
		this.param_evalue.validateSettings(settings);
		this.param_qid.validateSettings(settings);
		this.param_cov.validateSettings(settings);
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

		return "hhblits";
	}
}

