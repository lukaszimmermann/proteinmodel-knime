package org.proteinevolution.nodes.input.pdb.xwalk;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
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
       
        this.createNewTab("INPUT/OUTPUT");
        
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
        
        

        // Make changeListener in case a different PDB file is selected
        LineParserChangeListener changeListener = new LineParserChangeListener();
        changeListener.addParser(new PDBChainStringSelection(chain1Selection));
        
        
        
        fileChooser.getModel().addChangeListener(changeListener);
        
        
        this.addDialogComponent(fileChooser);
        
        
        // END GROUP INPUT FILE
    
        this.addDialogComponent(chain1Selection);
        
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
        
        this.createNewTab("RESIDUE/ATOM SELECTION");
        
      
        
      
      
        this.addDialogComponent( new DialogComponentStringSelection(
        		new SettingsModelString(
        				XWalkNodeModel.AA1_CFGKEY,
        				XWalkNodeModel.AA1_DEFAULT.toString()),
        		XWalkNodeModel.AA1_LABEL, 
        		Residue.values()));
        
        
        this.addDialogComponent(new DialogComponentStringSelection(
        		new SettingsModelString(
        				XWalkNodeModel.AA2_CFGKEY,
        				XWalkNodeModel.AA2_DEFAULT.toString()),
        		XWalkNodeModel.AA2_LABEL, 
        		Residue.values()));
        
        
        
               
    }
}
