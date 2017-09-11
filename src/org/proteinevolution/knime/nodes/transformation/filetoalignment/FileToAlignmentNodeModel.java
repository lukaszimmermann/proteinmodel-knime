package org.proteinevolution.knime.nodes.transformation.filetoalignment;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.uri.IURIPortObject;
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
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;

/**
 * This is the model implementation of FileToAlignment.
 * 
 *
 * @author Lukas Zimmermann
 */
public class FileToAlignmentNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected FileToAlignmentNodeModel() {
  
    	 super(new PortType[] {IURIPortObject.TYPE},
               new PortType[] {SequenceAlignmentPortObject.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	final SequenceAlignmentContent sequenceAlignmentOut = SequenceAlignmentContent.fromFASTA(((IURIPortObject) inData[0])
    			.getURIContents()
    			.get(0)
    			.getURI().getPath());
		return new PortObject[]{
				
				new SequenceAlignmentPortObject(
						sequenceAlignmentOut,
						new SequenceAlignmentPortObjectSpec(
								SequenceAlignmentContent.TYPE,
								sequenceAlignmentOut.getAlignmentFormat()))
		};			
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        
    	// Nothing to be done here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {

    	// TODO Check for correct input type of alignment
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         
    	// Nothing to be done here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        
    	// Nothing to be done here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        
    	// Nothing to be done here
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
    	// Nothing to be done here
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
    	// Nothing to be done here
    }
}

