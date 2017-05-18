package org.proteinevolution.nodes.hhsuite.hhsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.commands.ExecutionException;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
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
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentContent;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentPortObjectSpec;
import org.proteinevolution.models.knime.hhsuitedb.HHsuiteDB;
import org.proteinevolution.models.knime.hhsuitedb.HHsuiteDBPortObject;
import org.proteinevolution.models.spec.AlignmentFormat;
import org.proteinevolution.models.util.CommandLine;
import org.proteinevolution.nodes.hhsuite.HHSuiteNodeModel;


/**
 * This is the model implementation of HHsearch.
 * 
 *
 * @author Lukas Zimmermann
 */
public class HHsearchNodeModel extends HHSuiteNodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(HHsearchNodeModel.class);

	// HHsuite database
	public static final String HHSUITEDB_CFGKEY = "HHSUITEDB";
	public static final String[] HHSUITEDB_DEFAULT = new String[0];
	private final SettingsModelStringArray param_hhsuitedb = new SettingsModelStringArray(HHSUITEDB_CFGKEY, HHSUITEDB_DEFAULT);


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
	protected HHsearchNodeModel() throws InvalidSettingsException {

		super(new PortType[] {SequenceAlignmentPortObject.TYPE, HHsuiteDBPortObject.TYPE},
				new PortType[] {BufferedDataTable.TYPE, SequenceAlignmentPortObject.TYPE});

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Get the alignment and the hhsuite database
		SequenceAlignmentContent sequenceAlignment = ((SequenceAlignmentPortObject) inData[0]).getAlignment();
		HHsuiteDB hhsuitedb = ((HHsuiteDBPortObject) inData[1]).getHHsuiteDB();

		// Make container for HHR result table
		BufferedDataContainer container = exec.createDataContainer(getHHRDataTableSpec());

		SequenceAlignmentContent sequenceAlignmentOut = null;
		AlignmentFormat sequenceAlignmentOutFormat = null;

		// Construct the commandLine call for this hhblits invocation
		try (CommandLine cmd = new CommandLine(this.getExecutable())) {

			cmd.withInput("-i", sequenceAlignment);

			for (String dbname : this.param_hhsuitedb.getStringArrayValue()) {

				cmd.withOption("-d", hhsuitedb.getPrefix(dbname));
			}

			cmd
			.withOption("-e", this.param_evalue.getDoubleValue())
			.withOption("-qid", this.param_qid.getDoubleValue())
			.withOption("-cov", this.param_cov.getDoubleValue())
			.withOutput("-o")
			.withOutput("-oa3m");

			String commandLineString = cmd.toString();
			logger.warn(cmd);

			// Run hhblits process and constantly check whether is needs to be terminated 
			// (TODO) Might need to be adapted for cluster execution
			Process p = Runtime.getRuntime().exec(commandLineString);
			while( p.isAlive() ) {

				try {	
					exec.checkCanceled();

				}  catch(CanceledExecutionException e) {

					p.destroy();
				}
			}

			// Execute HHBlits, nodes throws exception if this fails.
			if ( p.waitFor() != 0) {

				throw new ExecutionException("Execution of HHblits failed.");
			}

			sequenceAlignmentOut = SequenceAlignmentContent.fromFASTA(cmd.getAbsoluteFilePath("-oa3m"));
			sequenceAlignmentOutFormat = sequenceAlignmentOut.getAlignmentFormat();	

			// Read HHR output file
			byte state = 0;
			int rowCounter = 0;			
			try(BufferedReader br = new BufferedReader(new FileReader(cmd.getAbsoluteFilePath("-o")))) {


				String line;
				while ((line = br.readLine()) != null) {

					// Empty line before hitlist
					if (line.trim().isEmpty() && state == 0) {

						// Advance by one line, we do not care about the header
						br.readLine();					
						state = 1;

						// read a line of the header block of the HHR file
					} else if (state == 1 && ! line.trim().isEmpty()) {
						;
						// Add line to data table
						container.addRowToTable(new DefaultRow("Row"+rowCounter++, getHHRRow(line) ));

						// end of header block in HHR file
					} else if (state == 1) {

						break;
					}
				}
			}
		} finally {

			container.close();
		}

		return new PortObject[]{
				container.getTable(),
				new SequenceAlignmentPortObject(
						sequenceAlignmentOut,
						new SequenceAlignmentPortObjectSpec(SequenceAlignmentContent.TYPE, sequenceAlignmentOutFormat))
		};
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
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {

		// TODO: check if user settings are available, fit to the incoming
		// table structure, and the incoming types are feasible for the node
		// to execute. If the node can execute in its current state return
		// the spec of its output data table(s) (if you can, otherwise an array
		// with null elements), or throw an exception with a useful user message

		return new DataTableSpec[]{null, null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.param_hhsuitedb.saveSettingsTo(settings);
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
		return "hhsearch";
	}
}

