package org.proteinevolution.nodes.input.pdb.crosslinkpredictor;

import javax.swing.ListSelectionModel;

import org.knime.core.data.blob.BinaryObjectDataValue;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.proteinevolution.models.knime.LineParserChangeListener;
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
public class CrossLinkPredictorNodeDialog extends DefaultNodeSettingsPane {


    // the logger instance
	@SuppressWarnings("unused")
    private static final NodeLogger logger = NodeLogger
            .getLogger(CrossLinkPredictorNodeDialog.class);
	
    /**
     * New pane for configuring XWalk node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected CrossLinkPredictorNodeDialog() {
        super();
        
        int visibleRowCount = 8;
        
        // Start with IO
        this.setDefaultTabTitle("INPUT/OUTPUT");
        
        
        this.createNewGroup("Input PDB File");
        
        
       // File Chooser for the PDB MODEL
        DialogComponentFileChooser fileChooser = new DialogComponentFileChooser(
        		new SettingsModelString(
        				CrossLinkPredictorNodeModel.INPUT_CFGKEY,
        				CrossLinkPredictorNodeModel.INPUT_DEFAULT),
        		CrossLinkPredictorNodeModel.INPUT_HISTORY,
        		"pdb");
              
        // Make changeListener in case a different PDB file is selected
        LineParserChangeListener changeListener = new LineParserChangeListener();
        //changeListener.addParser(new PDBChainStringSelection(chain1Selection));
        //changeListener.addParser(new PDBChainStringSelection(chain2Selection));
        fileChooser.getModel().addChangeListener(changeListener);
        this.addDialogComponent(fileChooser);
          
        /*
         * RESIDUE/ATOM Selection
         */
        this.createNewTab("EUCLIDEAN");
        
        
        this.createNewGroup("Attention");
        this.addDialogComponent(new DialogComponentLabel("Distances are currently always calculated between CB atoms."));
        
        // Group Residue Selection
        this.createNewGroup("Residue Selection");
        
        // Residues
        this.setHorizontalPlacement(true);
        
        this.addDialogComponent(new DialogComponentStringListSelection(
        		new SettingsModelStringArray(
        				CrossLinkPredictorNodeModel.EUC_DONORS_CFGKEY,
        				CrossLinkPredictorNodeModel.EUC_DONORS_DEFAULT),
        		CrossLinkPredictorNodeModel.EUC_DONORS_LABEL,
        		Residue.values(), 
        		ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, 
        		true, 
        		visibleRowCount));
        
        this.addDialogComponent(new DialogComponentStringListSelection(
        		new SettingsModelStringArray(
        				CrossLinkPredictorNodeModel.EUC_ACCEPTORS_CFGKEY,
        				CrossLinkPredictorNodeModel.EUC_ACCEPTORS_DEFAULT),
        		CrossLinkPredictorNodeModel.EUC_ACCEPTORS_LABEL,
        		Residue.values(), 
        		ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, 
        		true, 
        		visibleRowCount));
    
        
    
        this.createNewTab("SASD");
     
        
        // Distance to be calculated are determined by the grid
        this.addDialogComponent(new DialogComponentColumnNameSelection(
        		new SettingsModelString(
        				CrossLinkPredictorNodeModel.GRID_SELECTION_CFGKEY,
        				CrossLinkPredictorNodeModel.GRID_SELECTION_DEFAULT), 
        		CrossLinkPredictorNodeModel.GRID_SELECTION_LABEL, 
        		0,
        		true, 
        		false,
        		BinaryObjectDataValue.class));
        
           
    }
}
