package org.proteinevolution.knime.nodes.transformation.pdbtotable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
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
import org.proteinevolution.knime.porttypes.structure.StructureCell;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;


/**
 * This is the model implementation of PDBToTable.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBToTableNodeModel extends NodeModel {


	/**
	 * Constructor for the node model.
	 */
	protected PDBToTableNodeModel() {

		super(new PortType[]   {StructurePortObject.TYPE},
				new PortType[] {BufferedDataTable.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		BufferedDataContainer container = exec.createDataContainer(
				new DataTableSpec(new DataColumnSpec[] {
				new DataColumnSpecCreator("pdb", StructureCell.TYPE).createSpec(),
		}));
		StructureContent structureContent =  ((StructurePortObject) inData[0]).getStructureContent();
		List<String> pdbStrings = structureContent.getAllPdbStrings();
		
		int rowCounter = 0;
		for (String pdbString : pdbStrings) {
			
			container.addRowToTable(new DefaultRow("Row"+rowCounter++,
					new StructureCell[] {	
							new StructureCell(new StructureContent(pdbString))	
					}));
		}
		container.close();
		return new BufferedDataTable[] { container.getTable()};
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

		if ( ! (inSpecs[0] instanceof StructurePortObjectSpec)) {
			
			throw new InvalidSettingsException("Port type 0 of PDBToTable must be Structure!");
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
