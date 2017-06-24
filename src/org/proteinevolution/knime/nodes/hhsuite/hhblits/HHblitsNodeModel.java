package org.proteinevolution.knime.nodes.hhsuite.hhblits;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.knime.nodes.hhsuite.HHSuiteNodeModel;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;
import org.proteinevolution.knime.porttypes.hhsuitedb.HHsuiteDBContent;
import org.proteinevolution.knime.porttypes.hhsuitedb.HHsuiteDBPortObject;
import org.proteinevolution.models.spec.AlignmentFormat;


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
				new PortType[] {SequenceAlignmentPortObject.TYPE, BufferedDataTable.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Get the alignment and the hhsuite database
		SequenceAlignmentContent sequenceAlignment = ((SequenceAlignmentPortObject) inData[0]).getAlignment();
		HHsuiteDBContent hhsuitedb = ((HHsuiteDBPortObject) inData[1]).getHHsuiteDB();

		// Make container for HHR result table
		BufferedDataContainer container = exec.createDataContainer(getHHRDataTableSpec());

		SequenceAlignmentContent sequenceAlignmentOut = null;
		AlignmentFormat sequenceAlignmentOutFormat = null;

		// Construct the commandLine call for this hhblits invocation
		try (CommandLine cmd = new CommandLine(this.getExecutable())) {

			cmd.addFile("-i", sequenceAlignment);

			for (String dbname : this.param_hhsuitedb.getStringArrayValue()) {

				cmd.addOption("-d", hhsuitedb.getPrefix(dbname));
			}

			cmd.addOption("-n", this.param_niterations.getStringValue());
			cmd.addOption("-e", this.param_evalue.getDoubleValue());
			cmd.addOption("-qid", this.param_qid.getDoubleValue());
			cmd.addOption("-cov", this.param_cov.getDoubleValue());
			cmd.addOutputFile("-o");
			cmd.addOutputFile("-oa3m");

			ExecutableNodeModel.exec(cmd.toString(), exec, null, null);
			
			sequenceAlignmentOut = SequenceAlignmentContent.fromFASTA(cmd.getFile("-oa3m").getAbsolutePath());
			sequenceAlignmentOutFormat = sequenceAlignmentOut.getAlignmentFormat();	

			// Read HHR output file
			byte state = 0;
			int rowCounter = 0;			
			try(BufferedReader br = new BufferedReader(new FileReader(cmd.getFile("-o").getAbsolutePath()))) {
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
				new SequenceAlignmentPortObject(
						sequenceAlignmentOut,
						new SequenceAlignmentPortObjectSpec(SequenceAlignmentContent.TYPE, sequenceAlignmentOutFormat)),
				container.getTable()
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

		return new PortObjectSpec[]{ null, null };
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

