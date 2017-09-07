package org.proteinevolution.knime.nodes.input.alignmentreader;

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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;


/**
 * This is the model implementation of AlignmentReader.
 * 
 *
 * @author Lukas Zimmermann
 */
final class AlignmentReaderNodeModel extends NodeModel {
	        
    static SettingsModelString getInput() {
  	
    	return new SettingsModelString("INPUT_CFGKEY", "");
    }
    private final SettingsModelString input = getInput();
    
    /**
     * Constructor for the node model.
     */
    protected AlignmentReaderNodeModel() {
                	
        super(new PortType[] {},
              new PortType[] {SequenceAlignmentPortObject.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SequenceAlignmentPortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {
    
    	// TODO Currently, only reading FASTA is supported
    	final SequenceAlignmentContent sequenceAlignment = SequenceAlignmentContent.fromFASTA(this.input.getStringValue());    	
        return new SequenceAlignmentPortObject[] {
        		
        	new SequenceAlignmentPortObject(
        			sequenceAlignment,
        			new SequenceAlignmentPortObjectSpec(
        					SequenceAlignmentContent.TYPE,
        					sequenceAlignment.getAlignmentFormat()))	
        };
    }

    /**
     */
    @Override
    protected void reset() {
    	
       // Nothing to be done here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        
        // Nothing to be done here
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

        this.input.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    	this.input.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    		this.input.validateSettings(settings);
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
