package org.proteinevolution.nodes.input.pdbdirreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.proteinevolution.models.spec.pdb.Atom;


/**
 * This is the model implementation of PDBDirReader.
 * Reads PDB files from a directory into a table with the corresponding file path.
 *
 * @author Lukas Zimmermann
 */
public class PDBDirReaderNodeModel extends NodeModel {
    

    
	// Input Directory
    public static final String INPUT_CFGKEY = "INPUT_CFGKEY";
    public static final String INPUT_DEFAULT = "";
    public static final String INPUT_HISTORY = "INPUT_HISTORY";
    private static final int HEADER_TITLE_START = 10;
    private final SettingsModelString input = new SettingsModelString(INPUT_CFGKEY,INPUT_DEFAULT);

    // Whether ATOM records are required in the PDB file to be listed
    public static final String ATOM_CFGKEY = "ATOM_CFGKEY";
    public static final boolean ATOM_DEFAULT = true;
    public static final String ATOM_LABEL = "Don't list PDB files without ATOM records";
    private final SettingsModelBoolean atom = new SettingsModelBoolean(ATOM_CFGKEY, ATOM_DEFAULT);
    
    /**
     * Constructor for the node model.
     */
    protected PDBDirReaderNodeModel() {
    
        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	// file name and Path spec
        DataColumnSpec[] allColSpecs = new DataColumnSpec[4];
        allColSpecs[0] = new DataColumnSpecCreator("filename", StringCell.TYPE).createSpec();
        allColSpecs[1] = new DataColumnSpecCreator("path", StringCell.TYPE).createSpec();
        allColSpecs[2] = new DataColumnSpecCreator("HEADER", StringCell.TYPE).createSpec();
        allColSpecs[3] = new DataColumnSpecCreator("TITLE", StringCell.TYPE).createSpec();
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
      
        // container
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        
        // List content of selected directory        
        File[] content = new File(this.input.getStringValue()).listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				
				String path = pathname.getName();
				return path.endsWith("pdb") || path.endsWith("ent");
			}
		});
        
        // Whether the PDB Dir reader required that the PDB files contain ATOM records
        boolean requireAtoms = this.atom.getBooleanValue();
        
        int rowCounter = 0;
        for(File currentFile : content) {
        	
        	DataCell[] cells = new DataCell[4];
        	cells[0] = StringCellFactory.create(currentFile.getName());
        	cells[1] = StringCellFactory.create(currentFile.getAbsolutePath());
        	
        	boolean atomSeen = false;
        	
        	// Open File and figure out HEADER and TITLE (need to be first two rows)
        	BufferedReader br = new BufferedReader(new FileReader(currentFile.getAbsolutePath()));
        	String line;
        	String header = "";
        	String title = "";
        	while ((line = br.readLine()) != null) {
        		line = line.trim();
        		if (line.startsWith("HEADER")) { 			
        			header = line.substring(HEADER_TITLE_START);	
        		} else if (line.startsWith("TITLE")) {
        			
        			title =  line.substring(HEADER_TITLE_START);
        			
        		} else if(requireAtoms) {
        		
        			if (Atom.isRecord(line)) {
        				
        				atomSeen = true;
        				break;
        			}
        		} else {
        			
        			break;
        		}
        	}
        	br.close();
        	br = null;
        	
        	cells[2] = StringCellFactory.create(header);
        	cells[3] = StringCellFactory.create(title); 
       
        	
        	// Only add PDB entry if it contains at least one ATOM Record
        	if (! requireAtoms || (requireAtoms && atomSeen)) {
        		container.addRowToTable(new DefaultRow("Row" + rowCounter++, cells));
        	}
        }
      
        // once we are done, we close the container and return its table
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

        this.input.saveSettingsTo(settings);
        this.atom.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {

    	this.input.loadSettingsFrom(settings);
    	this.atom.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    	this.input.validateSettings(settings);
    	this.atom.validateSettings(settings);
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

