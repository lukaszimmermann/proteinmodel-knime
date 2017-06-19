package org.proteinevolution.knime.nodes.analysis.pdbcompare;

import java.io.File;
import java.io.IOException;

import org.biojava.nbio.structure.align.StructureAlignment;
import org.biojava.nbio.structure.align.StructureAlignmentFactory;
import org.biojava.nbio.structure.align.ce.CeMain;
import org.knime.core.data.DataTableSpec;
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
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;


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
				new PortType[] {StructurePortObject.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {

    	// Fetch reference and hypothesis structure from port 0
    	StructureContent reference = ((StructurePortObject) inData[0]).getStructure();
    	StructureContent hypotheses = ((StructurePortObject) inData[1]).getStructure(); 
   
    	
    	
    	StructureAlignment algorithm  = StructureAlignmentFactory.getAlgorithm(CeMain.algorithmName);

    	
    	
    	
    	
    	
    	
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
        
    	if ( ! (inSpecs[0] instanceof StructurePortObject) || ! (inSpecs[1] instanceof StructurePortObject)) {
    		
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
