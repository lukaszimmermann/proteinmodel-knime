package org.proteinevolution.knime.nodes.input.pdbreader;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.proteinevolution.models.spec.pdb.PDB;

/**
 * <code>NodeDialog</code> for the "PDBFetcher" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class PDBReaderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring PDBFetcher node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected PDBReaderNodeDialog() {
        super();
     
        this.addDialogComponent(
        		new DialogComponentFileChooser(
        				new SettingsModelString(
        						PDBReaderNodeModel.INPUT_CFGKEY,
        						PDBReaderNodeModel.DEFAULT),
        				PDBReaderNodeModel.INPUT_HISTORY,
        				PDB.extensions));
    }
}
