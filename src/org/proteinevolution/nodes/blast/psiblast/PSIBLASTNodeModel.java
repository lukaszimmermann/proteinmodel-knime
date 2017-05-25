package org.proteinevolution.nodes.blast.psiblast;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentContent;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.models.util.CommandLine;
import org.proteinevolution.nodes.blast.BLASTNodeModel;


/**
 * This is the model implementation of PSIBLAST.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PSIBLASTNodeModel extends BLASTNodeModel {


	// Database
	public static final String DATABASE_CFGKEY = "DATABASE";
	public static final String DATABASE_DEFAULT = "";
	public static final String DATABASE_HISTORY = "DATABASE_HISTORY";
	private final SettingsModelString param_database = new SettingsModelString(DATABASE_CFGKEY, DATABASE_DEFAULT);

	// Inclusion Etresh
	public static final String INCLUSION_ETRESH_CFGKEY = "INCLUSION_ETRESH";
	public static final double INCLUSION_ETRESH_DEFAULT = 0.001;
	public static final double INCLUSION_ETRESH_MIN = 0;
	public static final double INCLUSION_ETRESH_MAX = 1000;
	private final SettingsModelDoubleBounded param_inclusion_etresh = 
			new SettingsModelDoubleBounded(INCLUSION_ETRESH_CFGKEY, INCLUSION_ETRESH_DEFAULT, INCLUSION_ETRESH_MIN, INCLUSION_ETRESH_MAX);

	// Number of iterations
	public static final String N_ITERATIONS_CFGKEY = "N_ITERATIONS";
	public static final int N_ITERATIONS_DEFAULT = 3;
	public static final int N_ITERATIONS_MIN = 1;
	public static final int N_ITERATIONS_MAX = 8;
	private final SettingsModelIntegerBounded param_n_iterations = 
			new SettingsModelIntegerBounded(N_ITERATIONS_CFGKEY, N_ITERATIONS_DEFAULT, N_ITERATIONS_MIN, N_ITERATIONS_MAX);


	// N Alignments
	public static final String N_ALIGNMENTS_CFGKEY = "N_ALIGNMENTS";
	public static final int N_ALIGNMENTS_DEFAULT = 250;
	public static final int N_ALIGNMENTS_MIN = 0;
	public static final int N_ALIGNMENTS_MAX = 5000;
	private final SettingsModelIntegerBounded param_n_alignments = 
			new SettingsModelIntegerBounded(N_ALIGNMENTS_CFGKEY, N_ALIGNMENTS_DEFAULT, N_ALIGNMENTS_MIN, N_ALIGNMENTS_MAX);

	// N Descriptions
	public static final String N_DESCRIPTIONS_CFGKEY = "N_ALIGNMENTS";
	public static final int N_DESCRIPTIONS_DEFAULT = 250;
	public static final int N_DESCRIPTIONS_MIN = 0;
	public static final int N_DESCRIPTIONS_MAX = 5000;
	private final SettingsModelIntegerBounded param_n_descriptions = 
			new SettingsModelIntegerBounded(N_DESCRIPTIONS_CFGKEY, N_DESCRIPTIONS_DEFAULT, N_DESCRIPTIONS_MIN, N_DESCRIPTIONS_MAX);


	/**
	 * Constructor for the node model.
	 */
	protected PSIBLASTNodeModel() throws InvalidSettingsException {

		super(
				new PortType[] {SequenceAlignmentPortObject.TYPE},
				new PortType[] {});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Get the alignment (TODO Should be multiple sequences)
		SequenceAlignmentContent sequenceAlignment = ((SequenceAlignmentPortObject) inData[0]).getAlignment();

		try (CommandLine cmd = new CommandLine(this.getExecutable())) {
			
			cmd.addOption("-db", this.param_database.getStringValue());
			cmd.addInput("-query", sequenceAlignment);
			cmd.addOption("-inclusion_ethresh", this.param_inclusion_etresh.getDoubleValue());
			cmd.addOption("-num_iterations", this.param_n_iterations.getIntValue());
			cmd.addOption("-num_alignments", this.param_n_alignments.getIntValue());
			cmd.addOption("-num_descriptions", this.param_n_descriptions.getIntValue());
			cmd.addOutput("-out_pssm");	
		}
		
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

			this.param_database.saveSettingsTo(settings);
			this.param_inclusion_etresh.saveSettingsTo(settings);
			this.param_n_iterations.saveSettingsTo(settings);
			this.param_n_alignments.saveSettingsTo(settings);
			this.param_n_descriptions.saveSettingsTo(settings);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
				throws InvalidSettingsException {

			this.param_database.loadSettingsFrom(settings);
			this.param_inclusion_etresh.loadSettingsFrom(settings);
			this.param_n_iterations.loadSettingsFrom(settings);
			this.param_n_alignments.loadSettingsFrom(settings);
			this.param_n_descriptions.loadSettingsFrom(settings);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void validateSettings(final NodeSettingsRO settings)
				throws InvalidSettingsException {

			this.param_database.validateSettings(settings);
			this.param_inclusion_etresh.validateSettings(settings);
			this.param_n_iterations.validateSettings(settings);
			this.param_n_alignments.validateSettings(settings);
			this.param_n_descriptions.validateSettings(settings);
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

			return "psiblast";
		}
	}
