package org.proteinevolution.knime.nodes.rosetta.abinitiorelax;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.proteinevolution.knime.nodes.rosetta.RosettaBaseNodeModel;


/**
 * This is the model implementation of RosettaAbInitioRelax.
 * 
 *
 * @author Lukas Zimmermann
 */
public class RosettaAbInitioRelaxNodeModel extends RosettaBaseNodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(RosettaAbInitioRelaxNodeModel.class);
        
    
  
    /**
     * Constructor for the node model.
     */
    protected RosettaAbInitioRelaxNodeModel() throws InvalidSettingsException {
    
        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
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
		
		// TODO More flexible
		return "AbinitioRelax.default.linuxgccrelease";
	}
}

