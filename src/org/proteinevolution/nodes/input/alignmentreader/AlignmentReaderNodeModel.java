package org.proteinevolution.nodes.input.alignmentreader;

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
import org.proteinevolution.models.knime.alignment.SequenceAlignment;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentPortObjectSpec;
import org.proteinevolution.models.spec.AlignmentFormat;


/**
 * This is the model implementation of AlignmentReader.
 * 
 *
 * @author Lukas Zimmermann
 */
public class AlignmentReaderNodeModel extends NodeModel {
	
    public static final String INPUT_CFGKEY = "INPUT";
    public static final String INPUT_DEFAULT = "";
    public static final String INPUT_HISTORY = "INPUT_HISTORY";
    private final SettingsModelString input = new SettingsModelString(INPUT_CFGKEY, INPUT_DEFAULT);
        
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
    	SequenceAlignment sequenceAlignment = SequenceAlignment.fromFASTA(this.input.getStringValue());
    	AlignmentFormat alignmentFormat = sequenceAlignment.getAlignmentFormat();
    	
        return new SequenceAlignmentPortObject[] {
        		
        	new SequenceAlignmentPortObject(
        			sequenceAlignment,
        			new SequenceAlignmentPortObjectSpec(SequenceAlignment.TYPE, alignmentFormat))	
        };
    }

    /**
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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
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
}
