package org.proteinevolution.nodes.hhsuite.hhblits;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
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
import org.proteinevolution.models.knime.alignment.SequenceAlignment;
import org.proteinevolution.models.knime.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.nodes.hhsuite.HHSuiteNodeModel;




/*
 
 hhblits 
  -cpu 8 
  -v 2
  -i #{@basename}.resub_domain.a2m
   #{@E_hhblits} 
   -d #{HHBLITS_DB}
   -o #{@basename}.hhblits 
   -oa3m #{a3mFile} -n #{@maxhhblitsit}
    -mact 0.35  
 */


/**
 * This is the model implementation of HHblits.
 * 
 *
 * @author Lukas Zimmermann
 */
public class HHblitsNodeModel extends HHSuiteNodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(HHblitsNodeModel.class);
        

    /**
     * Constructor for the node model.
     */
    protected HHblitsNodeModel() throws InvalidSettingsException {
    
        super(new PortType[] {SequenceAlignmentPortObject.TYPE},
        	  new PortType[] {BufferedDataTable.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {

        // the data table spec of the single output table, 
        // the table will have three columns:
        DataColumnSpec[] allColSpecs = new DataColumnSpec[1];
        allColSpecs[0] = new DataColumnSpecCreator("Column 0", StringCell.TYPE).createSpec();

        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        
        // Get the alignment
        SequenceAlignment sequenceAlignment = ((SequenceAlignmentPortObject) inData[0]).getAlignment();
        
        File execFile = this.getExecutable();
        
        logger.warn("Going to execute " + execFile);
    
       
        container.close();
        return new BufferedDataTable[]{container.getTable()};
    }

    /**
     * {@inheritDoc}
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
    protected DataTableSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
    	
    	
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
            
        // TODO check if the settings could be applied to our model
        // e.g. if the count is in a certain range (which is ensured by the
        // SettingsModel).
        // Do not actually set any values of any member variables.

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

	@Override
	protected String getExecutableName() {
		
		return "hhblits";
	}
}

