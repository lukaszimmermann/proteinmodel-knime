package org.proteinevolution.knime.nodes.analysis.alignmentinfo;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.data.def.StringCell;
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
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;


/**
 * This is the model implementation of AlignmentInfo.
 * 
 *
 * @author Lukas Zimmermann
 */
public class AlignmentInfoNodeModel extends NodeModel {
    

    /**
     * Constructor for the node model.
     */
    protected AlignmentInfoNodeModel() {
    
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
        DataColumnSpec[] allColSpecs = new DataColumnSpec[] {
        		
        		new DataColumnSpecCreator("no_sequences", IntCell.TYPE).createSpec(),
        		new DataColumnSpecCreator("length", IntCell.TYPE).createSpec(),
        		new DataColumnSpecCreator("format", StringCell.TYPE).createSpec()
        };
        BufferedDataContainer container = exec.createDataContainer(new DataTableSpec(allColSpecs));
        SequenceAlignmentContent in = ((SequenceAlignmentPortObject) inData[0]).getAlignment();
             
        container.addRowToTable(
        		new DefaultRow(
        				"Row0",
        				 new DataCell[] {
        						 IntCellFactory.create(in.getNumberSequences()),
        						 IntCellFactory.create(in.getLength()),
        						 StringCellFactory.create(in.getAlignmentFormat().toString())
        				 }));
        container.close();
        return new BufferedDataTable[]{container.getTable()};
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
    protected DataTableSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
        
    	if ( ! (inSpecs[0] instanceof SequenceAlignmentPortObjectSpec)) {
    		
    		throw new InvalidSettingsException("Port object for node AlignmentInfo must be SequenceAlignment.");
    	}
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

    	// No settings for this node
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    	// No settings for this node
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    	// No settings for this  node
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
    	// No internals for this node
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
    	// No internals for this node
    }
}
