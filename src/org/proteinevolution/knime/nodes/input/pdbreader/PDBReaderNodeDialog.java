package org.proteinevolution.knime.nodes.input.pdbreader;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.proteinevolution.models.spec.pdb.PDB;

/**
 * <code>NodeDialog</code> for the "PDBReader" Node.
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

    protected PDBReaderNodeDialog() {
        super();
     
        // Add file chooser for the PDB file
        this.addDialogComponent(
        		new DialogComponentFileChooser(
        				new SettingsModelString(
        						PDBReaderNodeModel.INPUT_CFGKEY,
        						PDBReaderNodeModel.DEFAULT),
        				PDBReaderNodeModel.INPUT_HISTORY,
        				PDB.extensions));
    }
}
