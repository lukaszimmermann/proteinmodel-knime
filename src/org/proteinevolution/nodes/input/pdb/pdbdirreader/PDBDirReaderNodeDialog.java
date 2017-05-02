package org.proteinevolution.nodes.input.pdb.pdbdirreader;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "PDBDirReader" Node.
 * Reads PDB files from a directory into a table with the corresponding file path.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class PDBDirReaderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring PDBDirReader node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected PDBDirReaderNodeDialog() {
        super();
        
        
        this.addDialogComponent(new DialogComponentFileChooser(
        		new SettingsModelString(
        				PDBDirReaderNodeModel.INPUT_CFGKEY,
        				PDBDirReaderNodeModel.INPUT_DEFAULT),
        		PDBDirReaderNodeModel.INPUT_HISTORY,
        		JFileChooser.OPEN_DIALOG,
        		true)); 
        
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				PDBDirReaderNodeModel.ATOM_CFGKEY,
        				PDBDirReaderNodeModel.ATOM_DEFAULT),
        		PDBDirReaderNodeModel.ATOM_LABEL));
    }	
}
