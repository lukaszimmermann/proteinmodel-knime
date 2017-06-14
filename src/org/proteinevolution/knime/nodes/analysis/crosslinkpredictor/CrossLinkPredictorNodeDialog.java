package org.proteinevolution.knime.nodes.analysis.crosslinkpredictor;

import javax.swing.ListSelectionModel;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
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
	protected CrossLinkPredictorNodeDialog() {
        super();
        
        int visibleRowCount = 8;
                        
        /*
         * RESIDUE/ATOM Selection
         */
        this.setDefaultTabTitle("EUCLIDEAN");
        
        
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
    }
}
