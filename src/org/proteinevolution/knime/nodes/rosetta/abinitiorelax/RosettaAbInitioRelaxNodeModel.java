package org.proteinevolution.knime.nodes.rosetta.abinitiorelax;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.knime.nodes.rosetta.RosettaBaseNodeModel;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;
import org.proteinevolution.models.spec.AlignmentFormat;


/**
 * This is the model implementation of RosettaAbInitioRelax.
 * 
 *
 * @author Lukas Zimmermann
 */
public class RosettaAbInitioRelaxNodeModel extends RosettaBaseNodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(RosettaAbInitioRelaxNodeModel.class);


	// Param: -abinitio::relax
	public static SettingsModelBoolean getParamRelax() {

		return new SettingsModelBoolean("RELAX_CFGKEY", false);
	}
	private final SettingsModelBoolean param_relax = getParamRelax();


	// Param: -abinitio::increase_cycles 10
	public static SettingsModelIntegerBounded getParamIncCycles() {

		return new SettingsModelIntegerBounded("INC_CYCLES_CFGKEY", 10, 0, 100);
	}
	private final SettingsModelIntegerBounded param_inc_cycles = getParamIncCycles();


	// Param: -abinitio::rg_reweight 0.5
	public static SettingsModelDoubleBounded getParamRgReweight() {

		return new SettingsModelDoubleBounded("RG_REWEIGHT_CFGKEY", 0.5, 0, 10);
	}
	private final SettingsModelDoubleBounded param_rg_reweight = getParamRgReweight();


	
	// -rsd_wt_helix 0.5       # Reweight env, pair, and cb scores for helix residues by this factor
	// Param: -abinitio::rsd_wt_helix 0.5
	public static SettingsModelDoubleBounded getParamRsdWtHelix() {

		return new SettingsModelDoubleBounded("RST_WT_HELIX_CFGKEY", 0.5, 0, 10);
	}
	private final SettingsModelDoubleBounded param_rst_wt_helix = getParamRsdWtHelix();
	
	
	
	// -rsd_wt_loop 0.5        # Reweight env, pair, and cb scores for loop residues by this factor
	public static SettingsModelDoubleBounded getParamRsdWtLoop() {

		return new SettingsModelDoubleBounded("RST_WT_LOOP_CFGKEY", 0.5, 0, 10);
	}
	private final SettingsModelDoubleBounded param_rst_wt_loop = getParamRsdWtLoop();
		
	
	// -relax::fast   # At the end of the de novo protein_folding, do a relax step of type "FastRelax". This has been shown to be the best deal for speed and robustness.
	public static SettingsModelBoolean getParamRelaxFast() {

		return new SettingsModelBoolean("RELAX_FAST_CFGKEY", true);
	}
	private final SettingsModelBoolean param_relax_fast = getParamRelaxFast();
		
	
	/**
	 * Constructor for the node model.
	 */
	protected RosettaAbInitioRelaxNodeModel() throws InvalidSettingsException {

		super(new PortType[] {SequenceAlignmentPortObject.TYPE},
				new PortType[] {});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Fetch sequence
		SequenceAlignmentContent sequence = ((SequenceAlignmentPortObject) inData[0]).getAlignment();




		return null;
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

		if ( ! (inSpecs[0] instanceof SequenceAlignmentPortObjectSpec)) {

			throw new InvalidSettingsException("InPort Type 0 of RosettaAbInitioRelax must be SequenceAlignment!");
		}

		if (((SequenceAlignmentPortObjectSpec) inSpecs[0]).getAlignmentFormat() != AlignmentFormat.SingleSequence) {

			throw new InvalidSettingsException("ERROR: Only one sequence allowed for the Rosetta AbInitioRelax node!");
		}

		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.param_relax.saveSettingsTo(settings);
		this.param_inc_cycles.saveSettingsTo(settings);
		this.param_rg_reweight.saveSettingsTo(settings);
		this.param_rst_wt_helix.saveSettingsTo(settings);
		this.param_rst_wt_loop.saveSettingsTo(settings);
		this.param_relax_fast.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_relax.loadSettingsFrom(settings);
		this.param_inc_cycles.loadSettingsFrom(settings);
		this.param_rg_reweight.loadSettingsFrom(settings);
		this.param_rst_wt_helix.loadSettingsFrom(settings);
		this.param_rst_wt_loop.loadSettingsFrom(settings);
		this.param_relax_fast.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_relax.validateSettings(settings);
		this.param_inc_cycles.validateSettings(settings);
		this.param_rg_reweight.validateSettings(settings);
		this.param_rst_wt_helix.validateSettings(settings);
		this.param_rst_wt_loop.validateSettings(settings);
		this.param_relax_fast.validateSettings(settings);
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

		// TODO More flexible
		return "AbinitioRelax.default.linuxgccrelease";
	}
}

