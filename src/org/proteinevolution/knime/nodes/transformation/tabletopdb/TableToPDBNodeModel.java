package org.proteinevolution.knime.nodes.transformation.tabletopdb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.knime.porttypes.structure.StructureCell;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;



/**
 * This is the model implementation of TableToPDB.
 * 
 *
 * @author Lukas Zimmermann
 */
public class TableToPDBNodeModel extends NodeModel {
    
	
	// Param: Column selection
	public static final SettingsModelColumnName getParamInput() {
		
		return new SettingsModelColumnName("INPUT_CFGKEY", "");
	}
	private final SettingsModelColumnName param_input = getParamInput();
	
    /**
     * Constructor for the node model.
     */
    protected TableToPDBNodeModel() {
    
		super(new PortType[]   {BufferedDataTable.TYPE},
				new PortType[] {StructurePortObject.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {

    	DataTable table = (DataTable) inData[0];
    	
    	// Index of Structure
    	int strucIndex = table.getDataTableSpec().findColumnIndex(this.param_input.getColumnName());
    	List<String> pdbStrings = new ArrayList<String>();
    	
    	for(DataRow row : table) {
    		
    		StructureContent content = ((StructureCell) row.getCell(strucIndex)).getContent();
    		assert content.getNumberOfStructures() == 1;
    		pdbStrings.add(content.getPdbString(0));
    	}
      return new StructurePortObject[] {
    	  new StructurePortObject(new StructureContent(pdbStrings), new StructurePortObjectSpec(StructureContent.TYPE, pdbStrings.size()))
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
        
    	if ( ! (inSpecs[0] instanceof DataTableSpec)) {
    		
    		throw new InvalidSettingsException("Port type of TableToPDB must be DataTable!");
    	}

        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	
    	this.param_input.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	
    	this.param_input.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	
    	this.param_input.validateSettings(settings);
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

