package org.proteinevolution.nodes.input.pdb.crosslinktheorist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.proteinevolution.models.spec.pdb.Atom;
import org.proteinevolution.models.spec.pdb.Residue;


/**
 * This is the model implementation of XWalk.
 * This node represents an adaption of the XWalk program originally developed by Kahraman et al. [1]. * n * n[1] Abdullah Kahraman, Lars Malmström, Ruedi Aebersold; Xwalk: computing and visualizing distances in cross-linking experiments. Bioinformatics 2011; 27 (15): 2163-2164. doi: 10.1093/bioinformatics/btr348
 *
 * @author Lukas Zimmermann
 */
public class CrossLinkTheoristNodeModel extends NodeModel {
    
	
	// Class for keeping track of residues within this node
	
	private final class LocalAtom {
		
		public final int resid;
		public final String resname;
		public final String chain;
		public final double x;
		public final double y;
		public final double z;
		
		public LocalAtom(int resid, String resname, String chain, double x, double y, double z) {
			this.resid = resid;
			this.resname = resname;
			this.chain = chain;
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
	

    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(CrossLinkTheoristNodeModel.class);
        
    /*
     * Input/Output options
     */
    
    // Input PDB file
    public static final String INPUT_CFGKEY = "INPUT_CFGKEY";
    public static final String INPUT_DEFAULT = "";
    public static final String INPUT_HISTORY = "INPUT_HISTORY";
    private final SettingsModelString input = new SettingsModelString(INPUT_CFGKEY, INPUT_DEFAULT);
    
    
    // Whether side chain atoms of cross-linkes residues should be removed from the calculation
    public static final String XSC_CFGKEY = "XSC_CFGKEY";
    public static final boolean XSC_DEFAULT = false;
    public static final String XSC_LABEL = "Remove side-chain atoms";
    
 	//  Reads in only backbone and beta carbon atom coordinates from the input file and increases -radius to 2.0.
    public static final String BB_CFGKEY = "BB_CFGKEY";
    public static final boolean BB_DEFAULT = false;
    public static final String BB_LABEL = "Read only backbone";
   
    /*
     * RESIDUE/ATOM SELECTION:
     */
    // AA1
    public static final String AA1_CFGKEY = "AA1_CFGKEY";
    public static final String[] AA1_DEFAULT = new String[] {Residue.LYS.toString()};
    public static final String AA1_LABEL = "First amino acid to cross-link";
    private SettingsModelStringArray aa1 = new SettingsModelStringArray(AA1_CFGKEY, AA1_DEFAULT);
    
    // AA2
    public static final String AA2_CFGKEY = "AA2_CFGKEY";
    public static final String[] AA2_DEFAULT = new String[] {Residue.LYS.toString()};
    public static final String AA2_LABEL = "Second amino acid to cross-link";
    private SettingsModelStringArray aa2 = new SettingsModelStringArray(AA2_CFGKEY, AA2_DEFAULT);
    
    // c1
    public static final String C1_CFGKEY = "C1_CFGKEY";
    public static final String[] C1_DEFAULT = new String[0];
    public static final String C1_LABEL = "First list of chains";
    
    // Whether all chains should be used for c1
    public static final String C1ALL_CFGKEY = "C1ALL_CFGKEY";
    public static final boolean C1ALL_DEFAULT = true;
    public static final String C1ALL_LABEL = "Use all chains for residue 1";
    
    // c2
    public static final String C2_CFGKEY = "C2_CFGKEY";
    public static final String[] C2_DEFAULT = new String[0];
    public static final String C2_LABEL = "Second list of chains";
    
    // Whether all chains should be used for c2
    public static final String C2ALL_CFGKEY = "C2ALL_CFGKEY";
    public static final boolean C2ALL_DEFAULT = true;
    public static final String C2ALL_LABEL = "Use all chains for residue 2";
    
    
    // intra, inter, homo
    public static final String INTRA_CFGKEY = "INTRA_CFGKEY";
    public static final boolean INTRA_DEFAULT = false;
    public static final String INTRA_LABEL = "Output only intra molecular distances";
    
    public static final String INTER_CFGKEY = "INTER_CFGKEY";
    public static final boolean INTER_DEFAULT = false;
    public static final String INTER_LABEL = "Output only inter molecular distances";    
    
    public static final String HOMO_CFGKEY = "HOMO_CFGKEY";
    public static final boolean HOMO_DEFAULT = false;
    public static final String HOMO_LABEL = "Shortest distance only";
   
        
    // DIGESTION
    public static final String TRYPSIN_CFGKEY = "TRYPSIN_CFGKEY";
    public static final boolean TRYPSIN_DEFAULT = false;
    public static final String TRYPSIN_LABEL = "Digest with Trypsin";
    
    
    // DISTANCE
    // Maxdist
    public static final String MAXDIST_CFGKEY = "MAXDIST_CFGKEY";
    public static final int MAXDIST_DEFAULT = 34;
    public static final String MAXDIST_LABEL = "Max. distance to calculate";
    public static final int MAXDIST_MIN = 0;
    public static final int MAXDIST_MAX = 100;
    
    // Only Euclidean
    public static final String EUCLIDEAN_CFGKEY = "EUCLIDEAN_CFGKEY";
    public static final boolean EUCLIDEAN_DEFAULT = false;
    public static final String EUCLIDEAN_LABEL = "Euclidean Only";
    
    // Probability
    public static final String PROB_CFGKEY = "PROB_CFGKEY";
    public static final boolean PROB_DEFAULT = false;
    public static final String PROB_LABEL = "Prob. for DSS and BS3";
    
    // Bfactor
    public static final String BFACTOR_CFGKEY = "BFACTOR_CFGKEY";
    public static final boolean BFACTOR_DEFAULT = false;
    public static final String BFACTOR_LABEL = "Add BFactor uncertainty";
    
    
    //  SOLVENT-PATH-DISTANCE GRID RELATED
    public static final String RADIUS_CFGKEY = "RADIUS_CFGKEY";
    public static final double RADIUS_DEFAULT = 1.4;
    public static final String RADIUS_LABEL = "Solvent radius for SAS calculation [A]";
    public static final int RADIUS_MIN = 0;
    public static final int RADIUS_MAX = 3;
    
    public static final String SPACE_CFGKEY = "SPACE_CFGKEY";
    public static final double SPACE_DEFAULT = 1.0;
    public static final String SPACE_LABEL = "Grid cell spacing [A]";
    public static final int SPACE_MIN = 0;
    public static final int SPACE_MAX = 3;
    
   
    /**
     * Constructor for the node model.
     */
    protected CrossLinkTheoristNodeModel() {
    
        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	// Which residues we want to cross-links
    	Set<String> aa1_value = new HashSet<String>(Arrays.asList(this.aa1.getStringArrayValue()));
    	Set<String> aa2_value = new HashSet<String>(Arrays.asList(this.aa2.getStringArrayValue()));
    	
    	List<LocalAtom> donors = new ArrayList<LocalAtom>();
    	List<LocalAtom> acceptors = new ArrayList<LocalAtom>();
    	//TODO Continue here
    	List<>
    	
    	
    	// Go through the PDB file
    	try(BufferedReader br = new BufferedReader(new FileReader(this.input.getStringValue()))) {
    	    		
    		String line;
    		while( ( line = br.readLine()) != null  ) {
    				
    			line = line.trim();
    			
    			// We only care about ATOM records here
    			if (Atom.isRecord(line)) {

    				int resid = Integer.parseInt(line.substring(Atom.FIELD_RESIDUE_SEQ_NUMBER_START, Atom.FIELD_RESIDUE_SEQ_NUMBER_END).trim());
    				String atomname = line.substring(Atom.FIELD_ATOM_NAME_START, Atom.FIELD_ATOM_NAME_END).trim();
    				String resname =  line.substring(Atom.FIELD_RESIDUE_NAME_START, Atom.FIELD_RESIDUE_NAME_END);
    				String chain = line.substring(Atom.FIELD_CHAIN_IDENTIFIER_START, Atom.FIELD_CHAIN_IDENTIFIER_END);
    				
    				// See if we care about this atom (Currently only CB)
    				if (atomname.equals(Atom.CB.toString())) {
    					
    					boolean isDonor = aa1_value.contains(resname);
    					boolean isAcceptor = aa2_value.contains(resname);
    					boolean isBoth = isDonor && isAcceptor;
    					
    					if (isDonor || isAcceptor) {
    						
    						// Get all the required attributes of the CB atom
        					double x = Double.parseDouble(line.substring(Atom.FIELD_X_START, Atom.FIELD_X_END));
        					double y = Double.parseDouble(line.substring(Atom.FIELD_Y_START, Atom.FIELD_Y_END));
        					double z = Double.parseDouble(line.substring(Atom.FIELD_Z_START, Atom.FIELD_Z_END));
    					
        					LocalAtom atom = new LocalAtom(resid, resname, chain, x, y, z);
        					
        					// Calculate crosslinks with this atom being the donor
        					if (isDonor) {
        						
        						for(LocalAtom  acceptor : acceptors) {
        							
        							if (acc)
        							
        							
        						}
        						
        					}
        					
        					
        					
        					
        					// Remember that atom for future crosslinks
        					donors.add( isDonor ? atom : null);
        					acceptors.add( isAcceptor ? atom : null);	
    					}
    				}
    			}
    		}    		
    	}
    	
    	
    	//XWalk.getVirtualCrossLinks();
    	
    	
    	// Put the crossLinks into a data table
    	
        // the data table spec of the single output table, 
        // the table will have three columns:
        DataColumnSpec[] allColSpecs = new DataColumnSpec[1];
        allColSpecs[0] = new DataColumnSpecCreator("Column 0", StringCell.TYPE).createSpec();
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        
       
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        // let's add m_count rows to it
   
        logger.warn(this.input.getStringValue());
        
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

        // TODO save user settings to the config object.
    	this.input.saveSettingsTo(settings);
    	this.aa1.saveSettingsTo(settings);
    	this.aa2.saveSettingsTo(settings);
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
    	this.aa1.loadSettingsFrom(settings);
    	this.aa2.loadSettingsFrom(settings);
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
    	this.aa1.validateSettings(settings);
    	this.aa2.validateSettings(settings);
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

