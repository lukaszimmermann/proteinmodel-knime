package org.proteinevolution.nodes.input.pdb.xwalk;

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
public class XWalkNodeDialog extends DefaultNodeSettingsPane {


    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(XWalkNodeDialog.class);
	
    /**
     * New pane for configuring XWalk node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected XWalkNodeDialog() {
        super();
        
        int visibleRowCount = 8;
        
        // Start with IO
        this.setDefaultTabTitle("INPUT/OUTPUT");
        
        
        this.createNewGroup("Input PDB File");
        
        
       // File Chooser for the PDB MODEL
        DialogComponentFileChooser fileChooser = new DialogComponentFileChooser(
        		new SettingsModelString(
        				XWalkNodeModel.INPUT_CFGKEY,
        				XWalkNodeModel.INPUT_DEFAULT),
        		XWalkNodeModel.INPUT_HISTORY,
        		"pdb");
        
        // chain Selection C1
        DialogComponentStringListSelection chain1Selection = new DialogComponentStringListSelection(
        		new SettingsModelStringArray(
        				XWalkNodeModel.C1_CFGKEY,
        				XWalkNodeModel.C1_DEFAULT),
        		XWalkNodeModel.C1_LABEL, 
        		new String[]{"X"});
        chain1Selection.setVisibleRowCount(8);
        
        // chain Selection C1
        DialogComponentStringListSelection chain2Selection = new DialogComponentStringListSelection(
        		new SettingsModelStringArray(
        				XWalkNodeModel.C2_CFGKEY,
        				XWalkNodeModel.C2_DEFAULT),
        		XWalkNodeModel.C2_LABEL, 
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
        				XWalkNodeModel.XSC_CFGKEY,
        				XWalkNodeModel.XSC_DEFAULT),
        		XWalkNodeModel.XSC_LABEL));
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				XWalkNodeModel.BB_CFGKEY,
        				XWalkNodeModel.BB_DEFAULT),
        		XWalkNodeModel.BB_LABEL));
        
        
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
        				XWalkNodeModel.AA1_CFGKEY,
        				XWalkNodeModel.AA1_DEFAULT),
        		XWalkNodeModel.AA1_LABEL,
        		Residue.values(), 
        		ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, 
        		true, 
        		visibleRowCount));
        
        this.addDialogComponent(new DialogComponentStringListSelection(
        		new SettingsModelStringArray(
        				XWalkNodeModel.AA2_CFGKEY,
        				XWalkNodeModel.AA2_DEFAULT),
        		XWalkNodeModel.AA2_LABEL,
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
        				XWalkNodeModel.INTRA_CFGKEY,
        				XWalkNodeModel.INTRA_DEFAULT),
        		XWalkNodeModel.INTRA_LABEL));
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				XWalkNodeModel.INTER_CFGKEY,
        				XWalkNodeModel.INTER_DEFAULT),
        		XWalkNodeModel.INTER_LABEL));
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				XWalkNodeModel.HOMO_CFGKEY,
        				XWalkNodeModel.HOMO_DEFAULT),
        		XWalkNodeModel.HOMO_LABEL));
        
        this.createNewTab("DIGESTION");
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				XWalkNodeModel.TRYPSIN_CFGKEY,
        				XWalkNodeModel.TRYPSIN_DEFAULT),
        		XWalkNodeModel.TRYPSIN_LABEL));
        
        this.createNewTab("DISTANCE");
        
        this.setHorizontalPlacement(false);
        
        this.addDialogComponent(new DialogComponentNumber(
        		new SettingsModelDoubleBounded(
        				XWalkNodeModel.MAXDIST_CFGKEY,
        				XWalkNodeModel.MAXDIST_DEFAULT,
        				XWalkNodeModel.MAXDIST_MIN,
        				XWalkNodeModel.MAXDIST_MAX),
        		XWalkNodeModel.MAXDIST_LABEL,1));
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				XWalkNodeModel.EUCLIDEAN_CFGKEY,
        				XWalkNodeModel.EUCLIDEAN_DEFAULT),
        		XWalkNodeModel.EUCLIDEAN_LABEL));
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				XWalkNodeModel.PROB_CFGKEY,
        				XWalkNodeModel.PROB_DEFAULT),
        		XWalkNodeModel.PROB_LABEL));      
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				XWalkNodeModel.BFACTOR_CFGKEY,
        				XWalkNodeModel.BFACTOR_DEFAULT),
        		XWalkNodeModel.BFACTOR_LABEL));            
               
       this.createNewTab("SOLVENT-PATH-DISTANCE");
       
       // Radius
       this.addDialogComponent(new DialogComponentNumber(
       		new SettingsModelDoubleBounded(
       				XWalkNodeModel.RADIUS_CFGKEY,
       				XWalkNodeModel.RADIUS_DEFAULT,
       				XWalkNodeModel.RADIUS_MIN,
       				XWalkNodeModel.RADIUS_MAX),
       		XWalkNodeModel.RADIUS_LABEL,1));
       
       // Space
       this.addDialogComponent(new DialogComponentNumber(
          		new SettingsModelDoubleBounded(
          				XWalkNodeModel.SPACE_CFGKEY,
          				XWalkNodeModel.SPACE_DEFAULT,
          				XWalkNodeModel.SPACE_MIN,
          				XWalkNodeModel.SPACE_MAX),
          		XWalkNodeModel.SPACE_LABEL,1));
          
       
    
        
               
    }
}
