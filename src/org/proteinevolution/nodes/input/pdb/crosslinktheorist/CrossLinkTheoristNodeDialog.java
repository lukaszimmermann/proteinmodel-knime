package org.proteinevolution.nodes.input.pdb.crosslinktheorist;

import javax.swing.ListSelectionModel;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.proteinevolution.models.knime.LineParserChangeListener;
import org.proteinevolution.models.knime.PDBChainStringSelection;
import org.proteinevolution.models.spec.pdb.Residue;

/**
 * <code>NodeDialog</code> for the "XWalk" Node.
 * This node represents an adaption of the XWalk program originally developed by Kahraman et al. [1]. * n * n[1] Abdullah Kahraman, Lars Malmström, Ruedi Aebersold; Xwalk: computing and visualizing distances in cross-linking experiments. Bioinformatics 2011; 27 (15): 2163-2164. doi: 10.1093/bioinformatics/btr348
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class CrossLinkTheoristNodeDialog extends DefaultNodeSettingsPane {


    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(CrossLinkTheoristNodeDialog.class);
	
    /**
     * New pane for configuring XWalk node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected CrossLinkTheoristNodeDialog() {
        super();
        
        int visibleRowCount = 8;
        
        // Start with IO
        this.setDefaultTabTitle("INPUT/OUTPUT");
        
        
        this.createNewGroup("Input PDB File");
        
        
       // File Chooser for the PDB MODEL
        DialogComponentFileChooser fileChooser = new DialogComponentFileChooser(
        		new SettingsModelString(
        				CrossLinkTheoristNodeModel.INPUT_CFGKEY,
        				CrossLinkTheoristNodeModel.INPUT_DEFAULT),
        		CrossLinkTheoristNodeModel.INPUT_HISTORY,
        		"pdb");
        
        // chain Selection C1
        DialogComponentStringListSelection chain1Selection = new DialogComponentStringListSelection(
        		new SettingsModelStringArray(
        				CrossLinkTheoristNodeModel.C1_CFGKEY,
        				CrossLinkTheoristNodeModel.C1_DEFAULT),
        		CrossLinkTheoristNodeModel.C1_LABEL, 
        		new String[]{"X"});
        chain1Selection.setVisibleRowCount(8);
        
        // chain Selection C1
        DialogComponentStringListSelection chain2Selection = new DialogComponentStringListSelection(
        		new SettingsModelStringArray(
        				CrossLinkTheoristNodeModel.C2_CFGKEY,
        				CrossLinkTheoristNodeModel.C2_DEFAULT),
        		CrossLinkTheoristNodeModel.C2_LABEL, 
        		new String[]{"X"});
        chain2Selection.setVisibleRowCount(8);
        
        
        // Make changeListener in case a different PDB file is selected
        LineParserChangeListener changeListener = new LineParserChangeListener();
        changeListener.addParser(new PDBChainStringSelection(chain1Selection));
        changeListener.addParser(new PDBChainStringSelection(chain2Selection));
        fileChooser.getModel().addChangeListener(changeListener);
        this.addDialogComponent(fileChooser);
        
        
        this.createNewGroup("Further Options");
        
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				CrossLinkTheoristNodeModel.XSC_CFGKEY,
        				CrossLinkTheoristNodeModel.XSC_DEFAULT),
        		CrossLinkTheoristNodeModel.XSC_LABEL));
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				CrossLinkTheoristNodeModel.BB_CFGKEY,
        				CrossLinkTheoristNodeModel.BB_DEFAULT),
        		CrossLinkTheoristNodeModel.BB_LABEL));
        
        
        /*
         * RESIDUE/ATOM Selection
         */
        this.createNewTab("RESIDUE/ATOM SELECTION");
        
        // Group Residue Selection
        this.createNewGroup("Residue Selection");
        
        // Residues
        this.setHorizontalPlacement(true);
        
        this.addDialogComponent(new DialogComponentStringListSelection(
        		new SettingsModelStringArray(
        				CrossLinkTheoristNodeModel.AA1_CFGKEY,
        				CrossLinkTheoristNodeModel.AA1_DEFAULT),
        		CrossLinkTheoristNodeModel.AA1_LABEL,
        		Residue.values(), 
        		ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, 
        		true, 
        		visibleRowCount));
        
        this.addDialogComponent(new DialogComponentStringListSelection(
        		new SettingsModelStringArray(
        				CrossLinkTheoristNodeModel.AA2_CFGKEY,
        				CrossLinkTheoristNodeModel.AA2_DEFAULT),
        		CrossLinkTheoristNodeModel.AA2_LABEL,
        		Residue.values(), 
        		ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, 
        		true, 
        		visibleRowCount));
        
        this.createNewGroup("Chain Selection");
        this.addDialogComponent(chain1Selection);
        this.addDialogComponent(chain2Selection);
        
        this.createNewGroup("Further Options");
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				CrossLinkTheoristNodeModel.INTRA_CFGKEY,
        				CrossLinkTheoristNodeModel.INTRA_DEFAULT),
        		CrossLinkTheoristNodeModel.INTRA_LABEL));
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				CrossLinkTheoristNodeModel.INTER_CFGKEY,
        				CrossLinkTheoristNodeModel.INTER_DEFAULT),
        		CrossLinkTheoristNodeModel.INTER_LABEL));
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				CrossLinkTheoristNodeModel.HOMO_CFGKEY,
        				CrossLinkTheoristNodeModel.HOMO_DEFAULT),
        		CrossLinkTheoristNodeModel.HOMO_LABEL));
        
        this.createNewTab("DIGESTION");
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				CrossLinkTheoristNodeModel.TRYPSIN_CFGKEY,
        				CrossLinkTheoristNodeModel.TRYPSIN_DEFAULT),
        		CrossLinkTheoristNodeModel.TRYPSIN_LABEL));
        
        this.createNewTab("DISTANCE");
        
        this.setHorizontalPlacement(false);
        
        this.addDialogComponent(new DialogComponentNumber(
        		new SettingsModelDoubleBounded(
        				CrossLinkTheoristNodeModel.MAXDIST_CFGKEY,
        				CrossLinkTheoristNodeModel.MAXDIST_DEFAULT,
        				CrossLinkTheoristNodeModel.MAXDIST_MIN,
        				CrossLinkTheoristNodeModel.MAXDIST_MAX),
        		CrossLinkTheoristNodeModel.MAXDIST_LABEL,1));
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				CrossLinkTheoristNodeModel.EUCLIDEAN_CFGKEY,
        				CrossLinkTheoristNodeModel.EUCLIDEAN_DEFAULT),
        		CrossLinkTheoristNodeModel.EUCLIDEAN_LABEL));
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				CrossLinkTheoristNodeModel.PROB_CFGKEY,
        				CrossLinkTheoristNodeModel.PROB_DEFAULT),
        		CrossLinkTheoristNodeModel.PROB_LABEL));      
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				CrossLinkTheoristNodeModel.BFACTOR_CFGKEY,
        				CrossLinkTheoristNodeModel.BFACTOR_DEFAULT),
        		CrossLinkTheoristNodeModel.BFACTOR_LABEL));            
               
       this.createNewTab("SOLVENT-PATH-DISTANCE");
       
       // Radius
       this.addDialogComponent(new DialogComponentNumber(
       		new SettingsModelDoubleBounded(
       				CrossLinkTheoristNodeModel.RADIUS_CFGKEY,
       				CrossLinkTheoristNodeModel.RADIUS_DEFAULT,
       				CrossLinkTheoristNodeModel.RADIUS_MIN,
       				CrossLinkTheoristNodeModel.RADIUS_MAX),
       		CrossLinkTheoristNodeModel.RADIUS_LABEL,1));
       
       // Space
       this.addDialogComponent(new DialogComponentNumber(
          		new SettingsModelDoubleBounded(
          				CrossLinkTheoristNodeModel.SPACE_CFGKEY,
          				CrossLinkTheoristNodeModel.SPACE_DEFAULT,
          				CrossLinkTheoristNodeModel.SPACE_MIN,
          				CrossLinkTheoristNodeModel.SPACE_MAX),
          		CrossLinkTheoristNodeModel.SPACE_LABEL,1));
          
       
    
        
               
    }
}
