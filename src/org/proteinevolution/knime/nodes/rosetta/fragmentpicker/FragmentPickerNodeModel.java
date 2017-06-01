package org.proteinevolution.knime.nodes.rosetta.fragmentpicker;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;
import org.proteinevolution.models.spec.AlignmentFormat;
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


	/**
	 * Constructor for the node model.
	 */
	protected FragmentPickerNodeModel() throws InvalidSettingsException {

		super(new PortType[] {SequenceAlignmentPortObject.TYPE, StructurePortObject.TYPE}, 
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

		if ( ! (inSpecs[0] instanceof SequenceAlignmentPortObjectSpec)) {

			throw new InvalidSettingsException("Inport 1 of FragmentPicker must be a SequenceAlignment.");
		}
		SequenceAlignmentPortObjectSpec spec = (SequenceAlignmentPortObjectSpec) inSpecs[0];

		if (spec.getAlignmentFormat() == null) {

			throw new InvalidSettingsException("Alignment format of input SequenceAlignment is null!");
		}

		if (spec.getAlignmentFormat() != AlignmentFormat.SingleSequence) {

			throw new InvalidSettingsException("Only single sequences are currently allowed for ROSETTA's fragment picker");
		}
		
		if ( ! (inSpecs[1] instanceof StructurePortObjectSpec)) {

			throw new InvalidSettingsException("Inport 2 of FragmentPicker must be a Structure.");
		}

		return new PortObjectSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {


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


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {



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

