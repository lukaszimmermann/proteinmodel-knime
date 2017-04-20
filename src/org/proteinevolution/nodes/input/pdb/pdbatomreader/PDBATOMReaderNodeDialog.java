package org.proteinevolution.nodes.input.pdb.pdbatomreader;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "PDBATOMReader" Node.
 * Reads ATOM records from a PDB file and lists them in KNIME table.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class PDBATOMReaderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring PDBATOMReader node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected PDBATOMReaderNodeDialog() {
        super();
     
        this.addDialogComponent(new DialogComponentFileChooser(
        		new SettingsModelString(
        				PDBATOMReaderNodeModel.INPUT_CFGKEY,
        				PDBATOMReaderNodeModel.INPUT_DEFAULT),
        		PDBATOMReaderNodeModel.INPUT_HISTORY,
        		"pdb"));
    }
}
