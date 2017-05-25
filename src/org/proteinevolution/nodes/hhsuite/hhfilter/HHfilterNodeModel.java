package org.proteinevolution.nodes.hhsuite.hhfilter;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.ExecutionException;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentContent;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentPortObjectSpec;
import org.proteinevolution.models.spec.AlignmentFormat;
import org.proteinevolution.models.util.CommandLine;
import org.proteinevolution.nodes.hhsuite.HHSuiteNodeModel;


/**
 * This is the model implementation of HHfilter.
 * 
 *
 * @author Lukas Zimmermann
 */
public class HHfilterNodeModel extends HHSuiteNodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(HHfilterNodeModel.class);


	// Min coverage with master
	public static final String ID_CFGKEY = "ID";
	public static final double ID_DEFAULT = 90;
	public static final double ID_MIN = 0;
	public static final double ID_MAX = 100;
	private final SettingsModelDoubleBounded param_id = new SettingsModelDoubleBounded(ID_CFGKEY, ID_DEFAULT, ID_MIN, ID_MAX);


	public static final String DIFF_CFGKEY = "DIFF";
	public static final int DIFF_DEFAULT = 0;
	public static final int DIFF_MIN = 0;
	public static final int DIFF_MAX = 10000;
	private final SettingsModelIntegerBounded param_diff = new SettingsModelIntegerBounded(DIFF_CFGKEY, DIFF_DEFAULT, DIFF_MIN, DIFF_MAX);


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
	protected HHfilterNodeModel() throws InvalidSettingsException {

		super(new PortType[] {SequenceAlignmentPortObject.TYPE},
				new PortType[] {SequenceAlignmentPortObject.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		SequenceAlignmentContent sequenceAlignment = ((SequenceAlignmentPortObject) inData[0]).getAlignment();

		SequenceAlignmentContent sequenceAlignmentOut = null;
		AlignmentFormat sequenceAlignmentOutFormat = null;

		// Construct the commandLine call for this hhblits invocation
		try (CommandLine cmd = new CommandLine(this.getExecutable())) {

			cmd.addInput("-i", sequenceAlignment);
			cmd.addOption("-qid", this.param_qid.getDoubleValue());
			cmd.addOption("-cov", this.param_cov.getDoubleValue());
			cmd.addOption("-id", this.param_id.getDoubleValue());
			cmd.addOption("-diff", this.param_diff.getIntValue());
			cmd.addOutput("-o");

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

				throw new ExecutionException("Execution of HHfilter failed.");
			}
			sequenceAlignmentOut = SequenceAlignmentContent.fromFASTA(cmd.getAbsoluteFilePath("-o"));
			sequenceAlignmentOutFormat = sequenceAlignmentOut.getAlignmentFormat();	
		}
		return new PortObject[]{
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

		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.param_cov.saveSettingsTo(settings);
		this.param_diff.saveSettingsTo(settings);
		this.param_id.saveSettingsTo(settings);
		this.param_qid.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_cov.loadSettingsFrom(settings);
		this.param_diff.loadSettingsFrom(settings);
		this.param_id.loadSettingsFrom(settings);
		this.param_qid.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_cov.validateSettings(settings);
		this.param_diff.validateSettings(settings);
		this.param_id.validateSettings(settings);
		this.param_qid.validateSettings(settings);
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

		return "hhfilter";
	}
}
