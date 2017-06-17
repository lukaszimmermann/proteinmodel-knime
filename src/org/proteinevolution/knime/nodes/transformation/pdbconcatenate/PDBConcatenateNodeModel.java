package org.proteinevolution.knime.nodes.transformation.pdbconcatenate;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;


/**
 * This is the model implementation of PDBConcatenate.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBConcatenateNodeModel extends NodeModel {


	/**
	 * Constructor for the node model.
	 */
	protected PDBConcatenateNodeModel() {

		super(new PortType[]   {StructurePortObject.TYPE, StructurePortObject.TYPE},
				new PortType[] {StructurePortObject.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		StructurePortObject first = (StructurePortObject) inData[0];
		StructurePortObject second = (StructurePortObject) inData[1];

		return new StructurePortObject[] {
				new StructurePortObject(
						first.getStructure().concatenate(second.getStructure()),
						new StructurePortObjectSpec(StructureContent.TYPE,
								((StructurePortObjectSpec) first.getSpec()).getNStructures() 
								+  ((StructurePortObjectSpec) second.getSpec()).getNStructures()))
		};
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

		if (   ! (inSpecs[0] instanceof StructurePortObjectSpec)
				|| ! (inSpecs[1] instanceof StructurePortObjectSpec)) {

			throw new InvalidSettingsException("Port type 0,1 of PDBConcatenate must be structure!");
		}    	
		return new DataTableSpec[]{null};
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

}

