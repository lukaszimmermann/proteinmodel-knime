package org.proteinevolution.knime.nodes.analysis.pdbcompare;

import java.io.File;
import java.io.IOException;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureTools;
import org.biojava.nbio.structure.align.StructureAlignment;
import org.biojava.nbio.structure.align.StructureAlignmentFactory;
import org.biojava.nbio.structure.align.ce.CeMain;
import org.biojava.nbio.structure.align.ce.CeParameters;
import org.biojava.nbio.structure.align.model.AFPChain;
import org.biojava.nbio.structure.align.util.AFPChainScorer;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.DoubleCell.DoubleCellFactory;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;


/**
 * This is the model implementation of PDBCompare.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBCompareNodeModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(PDBCompareNodeModel.class);


	/**
	 * Constructor for the node model.
	 */
	protected PDBCompareNodeModel() {

		super(new PortType[]   {StructurePortObject.TYPE, StructurePortObject.TYPE},
				new PortType[] {BufferedDataTable.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Structure of the result table
		BufferedDataContainer container = exec.createDataContainer(new DataTableSpec(new DataColumnSpec[] {
				new DataColumnSpecCreator("port0", IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator("port1", IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator("CE_opt_RMSD", DoubleCell.TYPE).createSpec(),
				new DataColumnSpecCreator("TM_score", DoubleCell.TYPE).createSpec()
		}));

		// Fetch reference and hypothesis structure from port 0
		Structure[] references = ((StructurePortObject) inData[0]).getStructureContent().getAllStructureImpl();
		Structure[] hypotheses = ((StructurePortObject) inData[1]).getStructureContent().getAllStructureImpl(); 

		// The alignment algorithm
		StructureAlignment algorithm  = StructureAlignmentFactory.getAlgorithm(CeMain.algorithmName);

		// Parameters
		CeParameters params = new CeParameters();
		params.setMaxGapSize(-1);

		// Pairwise compare all structures with the CE algorithm
		int rowCounter = 0;
		for (int i = 0; i < references.length; ++i) {

			for (int j = 0; j < hypotheses.length; ++j) {

				Atom[] ca1 = StructureTools.getAtomCAArray(references[i]);
				Atom[] ca2 = StructureTools.getAtomCAArray(hypotheses[j]);
				// Perform alignment
				AFPChain afpChain = algorithm.align(ca1,ca2,params);	
	
				container.addRowToTable(new DefaultRow("RowKey"+rowCounter++, new DataCell[] {
						
						IntCellFactory.create(i),
						IntCellFactory.create(j),
						DoubleCellFactory.create(afpChain.getTotalRmsdOpt()),
						DoubleCellFactory.create(AFPChainScorer.getTMScore(afpChain, ca1, ca2))
				}));
			}
		}
		container.close();
		return new PortObject[] {container.getTable()};
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

		if ( ! (inSpecs[0] instanceof StructurePortObjectSpec) || ! (inSpecs[1] instanceof StructurePortObjectSpec)) {

			throw new InvalidSettingsException("Port types of PDBCompare node must be Structure");
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
