package org.proteinevolution.nodes.input.pdb.xwalk;

import java.io.File;
import java.io.IOException;

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
import org.proteinevolution.models.spec.pdb.Residue;


/**
 * This is the model implementation of XWalk.
 * This node represents an adaption of the XWalk program originally developed by Kahraman et al. [1]. * n * n[1] Abdullah Kahraman, Lars Malmström, Ruedi Aebersold; Xwalk: computing and visualizing distances in cross-linking experiments. Bioinformatics 2011; 27 (15): 2163-2164. doi: 10.1093/bioinformatics/btr348
 *
 * @author Lukas Zimmermann
 */
public class XWalkNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(XWalkNodeModel.class);
        
    /*
     * Input/Output options
     */
    
    // Input PDB file
    public static final String INPUT_CFGKEY = "INPUT_CFGKEY";
    public static final String INPUT_DEFAULT = "";
    public static final String INPUT_HISTORY = "INPUT_HISTORY";
    
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
    
    // AA2
    public static final String AA2_CFGKEY = "AA2_CFGKEY";
    public static final String[] AA2_DEFAULT = new String[] {Residue.LYS.toString()};
    public static final String AA2_LABEL = "Second amino acid to cross-link";
    
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
    
    
    
    
    // Settings models
    private final SettingsModelString input = new SettingsModelString(INPUT_CFGKEY, INPUT_DEFAULT);
    
    /**
     * Constructor for the node model.
     */
    protected XWalkNodeModel() {
    
        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        // TODO do something here
        logger.info("Node Model Stub... this is not yet implemented !");

        
        // the data table spec of the single output table, 
        // the table will have three columns:
        DataColumnSpec[] allColSpecs = new DataColumnSpec[3];
        allColSpecs[0] = 
            new DataColumnSpecCreator("Column 0", StringCell.TYPE).createSpec();
        allColSpecs[1] = 
            new DataColumnSpecCreator("Column 1", DoubleCell.TYPE).createSpec();
        allColSpecs[2] = 
            new DataColumnSpecCreator("Column 2", IntCell.TYPE).createSpec();
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
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

