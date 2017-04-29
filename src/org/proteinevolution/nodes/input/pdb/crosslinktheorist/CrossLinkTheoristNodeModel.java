package org.proteinevolution.nodes.input.pdb.crosslinktheorist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.container.DataContainer;
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
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.proteinevolution.models.spec.metrics.Distance;
import org.proteinevolution.models.spec.pdb.Atom;
import org.proteinevolution.models.spec.pdb.Residue;


/**
 * @author Lukas Zimmermann
 */
public class CrossLinkTheoristNodeModel extends NodeModel {
    
	
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
	@SuppressWarnings("unused")
    private static final NodeLogger logger = NodeLogger.getLogger(CrossLinkTheoristNodeModel.class);
        
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
    public static final String AA1_LABEL = "Donor amino acid residue to cross link";
    private SettingsModelStringArray aa1 = new SettingsModelStringArray(AA1_CFGKEY, AA1_DEFAULT);
    
    // AA2
    public static final String AA2_CFGKEY = "AA2_CFGKEY";
    public static final String[] AA2_DEFAULT = new String[] {Residue.LYS.toString()};
    public static final String AA2_LABEL = "Acceptor amino acid residue to cross link";
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
    
    // Distance metrics to calculate
    public static final String DISTANCE_SELECTION_CFGKEY = "DISTANCE_SELECTION_CFGKEY";
    public static final String[] DISTANCE_SELECTION_DEFAULT = new String[] {Distance.EUCLIDEAN.toString()};
    public static final String  DISTANCE_SELECTION_LABEL = "Select distances to calculate";
    private final SettingsModelStringArray distance = new SettingsModelStringArray(DISTANCE_SELECTION_CFGKEY, DISTANCE_SELECTION_DEFAULT);
    
    
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
    
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distances between the atoms a and b. The distance metrics currently supported are:
     * 	* Euclidean
     * 
     * @param a First atom of the distance pair between the distances should be computed
     * @param b Second atom of the distance pair between the distances should be computed
     * @return New row number that can be used
     */
    private static int assembleRow(LocalAtom current_atom,
    		                 List<LocalAtom> previous_atoms,
    		                 DataContainer container,
    		                 int row_number) {
    	
    	for (LocalAtom previous_atom : previous_atoms) {
			
        	double diff1 = current_atom.x - previous_atom.x;
        	double diff2 = current_atom.y - previous_atom.y;
        	double diff3 = current_atom.z - previous_atom.z;
    		
			// Assemble the data cell for this cross-link		
			container.addRowToTable(
					new DefaultRow(
							new RowKey("Row" + row_number++),
							new DataCell[] {
    								
    								StringCellFactory.create(current_atom.resname),
    								IntCellFactory.create(current_atom.resid),
    								StringCellFactory.create(Atom.CB.toString()),
    								StringCellFactory.create(current_atom.chain),
    								StringCellFactory.create(previous_atom.resname),
    								IntCellFactory.create(previous_atom.resid),
    								StringCellFactory.create(Atom.CB.toString()),   // Because we still assume CB here
    								StringCellFactory.create(previous_atom.chain),
    								DoubleCellFactory.create(Math.sqrt(diff1*diff1 + diff2*diff2 + diff3*diff3))
    						}));
		}
    	return row_number;
    }
    
   
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
    	
    	// Determine which distances to calculate
    	//Set<String> distance_arg = new HashSet<String>(Arrays.asList(this.distance.getStringArrayValue()));
    	
    	//boolean calcEuclidean = distance_arg.contains(Distance.EUCLIDEAN.toString());
    	//boolean calcSASD = distance_arg.contains(Distance.SASD.toString());
    	
       
        DataColumnSpec[] allColSpecs = new DataColumnSpec[] {
        		
        		new DataColumnSpecCreator("resname1", StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator("resid1", IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator("atomname1", StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator("chain1", StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator("resname2", StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator("resid2", IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator("atomname2", StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator("chain2", StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator("Euclidean_distance", DoubleCell.TYPE).createSpec()
        };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);    
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
   
    	// Which residues we want to cross-links
    	Set<String> aa1_arg = new HashSet<String>(Arrays.asList(this.aa1.getStringArrayValue()));
    	Set<String> aa2_arg = new HashSet<String>(Arrays.asList(this.aa2.getStringArrayValue()));
    	
    	// List keeping track of the acceptor residues
    	List<LocalAtom> donors = new ArrayList<LocalAtom>();
    	
    	// List keeping track of acceptor residues
    	List<LocalAtom> acceptors = new ArrayList<LocalAtom>();
    	
    	// List keeping track of residues that are both acceptors and donors
    	List<LocalAtom> both = new ArrayList<LocalAtom>();
    	
    	int row_counter = 0;
   
    	// Go through the PDB file
    	try(BufferedReader br = new BufferedReader(new FileReader(this.input.getStringValue()))) {
    	
    		String line;
    		while( ( line = br.readLine()) != null  ) {
   				
    			// We only care about ATOM records here
    			if (Atom.isRecord(line)) {

    				// Atom coordinates
    				double x = Double.parseDouble(line.substring(Atom.FIELD_X_START, Atom.FIELD_X_END));
					double y = Double.parseDouble(line.substring(Atom.FIELD_Y_START, Atom.FIELD_Y_END));
					double z = Double.parseDouble(line.substring(Atom.FIELD_Z_START, Atom.FIELD_Z_END));    			
    				
		
    				String atomname = line.substring(Atom.FIELD_ATOM_NAME_START, Atom.FIELD_ATOM_NAME_END).trim();
    				
    				// See if we care about this atom (Currently only CB)
    				if (atomname.equals(Atom.CB.toString())) {    					
    			
        				String resname =  line.substring(Atom.FIELD_RESIDUE_NAME_START, Atom.FIELD_RESIDUE_NAME_END);
    					
    					boolean isDonor = aa1_arg.contains(resname);
    					boolean isAcceptor = aa2_arg.contains(resname);
    					
    					// Only continue if the current residue is either a donor or acceptor or both 
    					if (isDonor || isAcceptor) {
    						
		
        					// Fetch the remaining required attributes
        					String chain = line.substring(Atom.FIELD_CHAIN_IDENTIFIER_START, Atom.FIELD_CHAIN_IDENTIFIER_END);
        					int resid = Integer.parseInt(line.substring(Atom.FIELD_RESIDUE_SEQ_NUMBER_START, Atom.FIELD_RESIDUE_SEQ_NUMBER_END).trim());
    					
        					LocalAtom current_atom = new LocalAtom(resid, resname, chain, x, y, z);
        					
        					// We always have to calculate the distances to all boths in any case
        					row_counter = assembleRow(current_atom, both, container, row_counter);
        			
        					List<LocalAtom> to_append = null;
        					
        					if (isDonor) {
        						
        						// Calculate the distances to all acceptors and append data row
        						row_counter = assembleRow(current_atom, acceptors, container, row_counter);
        						to_append = isAcceptor ? both : donors;
        				
        					// Must be acceptor
        					} else {
        						
        						// We have to calculate the distance to all donors
        						row_counter = assembleRow(current_atom, donors, container, row_counter);
        						to_append = acceptors;
        					}
        					to_append.add(current_atom);
    					}
    				}
    			}
    		}    		
    	}
        // once we are done, we close the container and return its table
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
    	this.distance.saveSettingsTo(settings);
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
    	this.distance.loadSettingsFrom(settings);
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
    	this.distance.validateSettings(settings);
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

