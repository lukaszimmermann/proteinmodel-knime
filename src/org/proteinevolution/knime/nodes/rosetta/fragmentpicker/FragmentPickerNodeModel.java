package org.proteinevolution.knime.nodes.rosetta.fragmentpicker;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.uri.URIPortObject;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;
import org.proteinevolution.models.spec.AlignmentFormat;
import org.proteinevolution.models.spec.FileExtensions;
import org.proteinevolution.models.util.URIUtils;
import org.proteinevolution.preferences.PreferencePage;


/**
 * This is the model implementation of FragmentPicker.
 * 
 *
 * @author Lukas Zimmermann
 */
public class FragmentPickerNodeModel extends ExecutableNodeModel {


	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(FragmentPickerNodeModel.class);


	// Name of the secondary structure
	public static final String CFG_SSNAME = "CFG_SSNAME";
	private final SettingsModelString param_ssname = new SettingsModelString(CFG_SSNAME, "predA");
	
	/**
	 * Constructor for the node model.
	 */
	protected FragmentPickerNodeModel() throws InvalidSettingsException {

		super(new PortType[] {
				SequenceAlignmentPortObject.TYPE,
				StructurePortObject.TYPE,
				PortTypeRegistry.getInstance().getPortType(URIPortObject.class),
				BufferedDataTable.TYPE
		}, 
				new PortType[] {SequenceAlignmentPortObject.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Get Sequence Alignment and Structure
		SequenceAlignmentContent sequenceAlignment = ((SequenceAlignmentPortObject) inData[0]).getAlignment();
		StructureContent structure = ((StructurePortObject) inData[1]).getStructure();



		// TODO Implement me
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

		// Validate port 0 for target sequence of fragment picker
		if ( ! (inSpecs[0] instanceof SequenceAlignmentPortObjectSpec)) {

			throw new InvalidSettingsException("Inport 0 of FragmentPicker must be a SequenceAlignment.");
		}
		SequenceAlignmentPortObjectSpec spec = (SequenceAlignmentPortObjectSpec) inSpecs[0];

		if (spec.getAlignmentFormat() != AlignmentFormat.SingleSequence) {

			throw new InvalidSettingsException("Only single sequences are currently allowed for ROSETTA's fragment picker");
		}

		// Validate port 1 for target structure 
		if ( ! (inSpecs[1] instanceof StructurePortObjectSpec)) {

			throw new InvalidSettingsException("Inport 2 of FragmentPicker must be a Structure.");
		}
		
		// Validate port 2 for PSIPRED SS2 secondary structure assignment
		URIUtils.checkURIExtension(inSpecs[2], FileExtensions.SS2);
		
		// Validate port 3 for the scoring function weights
		if ( ! (inSpecs[3] instanceof DataTableSpec)) {
			
			throw new InvalidSettingsException("Inport 3 of FragmentPicker must be a KNIME Data Table.");
		}
		DataTableSpec tablespec = (DataTableSpec) inSpecs[3];
		

		return new PortObjectSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.param_ssname.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_ssname.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_ssname.validateSettings(settings);
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

		// Implementation not necessary for ROSETTA's fragment picker
		return null;
	}

	@Override
	protected File getExecutable() {

		return new File(ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.ROSETTA_FRAGMENTPICKER_EXECUTABLE));
	}
}

