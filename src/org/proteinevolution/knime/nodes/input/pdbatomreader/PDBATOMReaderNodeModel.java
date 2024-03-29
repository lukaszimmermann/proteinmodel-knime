package org.proteinevolution.knime.nodes.input.pdbatomreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.DoubleCell.DoubleCellFactory;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.proteinevolution.models.spec.pdb.PDBAtom;


/**
 * This is the model implementation of PDBATOMReader.
 * Reads ATOM records from a PDB file and lists them in KNIME table.
 *
 * @author Lukas Zimmermann
 */
public class PDBATOMReaderNodeModel extends NodeModel {
    
  
   public static final String INPUT_CFGKEY = "INPUT_CFGKEY";
   public static final String INPUT_DEFAULT = "";
   public static final String INPUT_HISTORY = "INPUT_HISTORY";
   public static final String INPUT_LABEL = "Input PDB file";
   
   private static final int N_COLUMNS = 14;
   
  
   
   private final SettingsModelString input = new SettingsModelString(
		   INPUT_CFGKEY,
		   INPUT_DEFAULT);
   
    
    /**
     * Constructor for the node model.
     */
    protected PDBATOMReaderNodeModel() {
    
        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    
        // the data table spec of the single output table, 
        // the table will have three columns:
        DataColumnSpec[] allColSpecs = new DataColumnSpec[N_COLUMNS];
        allColSpecs[0] = new DataColumnSpecCreator("atom_serial_number", IntCell.TYPE).createSpec();
        allColSpecs[1] = new DataColumnSpecCreator("atom_name", StringCell.TYPE).createSpec();
        allColSpecs[2] = new DataColumnSpecCreator("atom_alternative_location", StringCell.TYPE).createSpec();
        allColSpecs[3] = new DataColumnSpecCreator("residue_name", StringCell.TYPE).createSpec();
        allColSpecs[4] = new DataColumnSpecCreator("chain_identifier", StringCell.TYPE).createSpec();
        allColSpecs[5] = new DataColumnSpecCreator("residue_sequence_number", IntCell.TYPE).createSpec();
        allColSpecs[6] = new DataColumnSpecCreator("code_insertion_residues", StringCell.TYPE).createSpec();
        allColSpecs[7] = new DataColumnSpecCreator("x", DoubleCell.TYPE).createSpec();
        allColSpecs[8] = new DataColumnSpecCreator("y", DoubleCell.TYPE).createSpec();
        allColSpecs[9] = new DataColumnSpecCreator("z", DoubleCell.TYPE).createSpec();
        allColSpecs[10] = new DataColumnSpecCreator("occupancy", DoubleCell.TYPE).createSpec();
        allColSpecs[11] = new DataColumnSpecCreator("temperature_factor", DoubleCell.TYPE).createSpec();
        allColSpecs[12] = new DataColumnSpecCreator("seqment_identifier", StringCell.TYPE).createSpec();
        allColSpecs[13] = new DataColumnSpecCreator("element_symbol", StringCell.TYPE).createSpec();
        
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        BufferedDataContainer container = exec.createDataContainer(outputSpec);

        // Cells for the resulting table
        DataCell[] cells = new DataCell[N_COLUMNS];
        
     	
        BufferedReader reader = new BufferedReader(new FileReader(this.input.getStringValue()));
        String line;
        int row_counter = 0;
        
        while ((line = reader.readLine()) != null) {
        	
        	line = line.trim();
        	
        	// Skip empty lines
        	if (line.isEmpty()) {
        		continue;
        	}
        	
        	// Only consider "ATOM" lines
        	if (PDBAtom.isRecord(line)) {
        		
        		// ATOM      1  N   LYS A   5      28.463-179.347 -37.294  1.00 24.83           N
        		cells[0] = IntCellFactory.create(line.substring(PDBAtom.FIELD_ATOM_SERIAL_NUMBER_START, PDBAtom.FIELD_ATOM_SERIAL_NUMBER_END).trim());
        		cells[1] = StringCellFactory.create(line.substring(PDBAtom.FIELD_ATOM_NAME_START, PDBAtom.FIELD_ATOM_NAME_END).trim());
        		cells[2] = StringCellFactory.create(line.substring(PDBAtom.FIELD_ATOM_ALTLOC_START, PDBAtom.FIELD_ATOM_ALTLOC_END).trim());
        		cells[3] = StringCellFactory.create(line.substring(PDBAtom.FIELD_RESIDUE_NAME_START, PDBAtom.FIELD_RESIDUE_NAME_END).trim());
        		cells[4] = StringCellFactory.create(line.substring(PDBAtom.FIELD_CHAIN_IDENTIFIER_START, PDBAtom.FIELD_CHAIN_IDENTIFIER_END).trim());
        		cells[5] = IntCellFactory.create(line.substring(PDBAtom.FIELD_RESIDUE_SEQ_NUMBER_START, PDBAtom.FIELD_RESIDUE_SEQ_NUMBER_END).trim());
        		cells[6] = StringCellFactory.create(line.substring(PDBAtom.FIELD_CODE_RESIDUE_INSERTION_START, PDBAtom.FIELD_CODE_RESIDUE_INSERTION_END).trim());
        		cells[7] = DoubleCellFactory.create(line.substring(PDBAtom.FIELD_X_START, PDBAtom.FIELD_X_END).trim());
        		cells[8] = DoubleCellFactory.create(line.substring(PDBAtom.FIELD_Y_START, PDBAtom.FIELD_Y_END).trim());
        		cells[9] = DoubleCellFactory.create(line.substring(PDBAtom.FIELD_Z_START, PDBAtom.FIELD_Z_END).trim());
        		cells[10] = DoubleCellFactory.create(line.substring(PDBAtom.FIELD_OCCUPANCY_START, PDBAtom.FIELD_OCCUPANCY_END).trim());
        		cells[11] = DoubleCellFactory.create(line.substring(PDBAtom.FIELD_TEMPERATURE_FACTOR_START, PDBAtom.FIELD_TEMPERATURE_FACTOR_END).trim());
        		cells[12] = StringCellFactory.create(line.substring(PDBAtom.FIELD_SEGMENT_IDENTIFIER_START, PDBAtom.FIELD_SEGMENT_IDENTIFIER_END).trim());
        		cells[13] = StringCellFactory.create(line.substring(PDBAtom.FIELD_ELEMENT_SYMBOL_START, PDBAtom.FIELD_ELEMENT_SYMBOL_END).trim());
        		
        		container.addRowToTable(new DefaultRow(new RowKey("Row" + row_counter++), cells));
        	}
        }
        
        reader.close();
        reader = null;
        container.close();
        BufferedDataTable out = container.getTable();
        return new BufferedDataTable[]{out};
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

        // TODO save user settings to the config object.
        
    	this.input.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // TODO load (valid) settings from the config object.
        // It can be safely assumed that the settings are valided by the 
        // method below.
        
    	this.input.loadSettingsFrom(settings);
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

