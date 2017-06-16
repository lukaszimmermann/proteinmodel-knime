package org.proteinevolution.knime.nodes.output.pdbwriter;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.proteinevolution.models.spec.FileExtensions;

/**
 * <code>NodeDialog</code> for the "PDBWriter" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class PDBWriterNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring PDBWriter node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected PDBWriterNodeDialog() {
        super();
        
        this.addDialogComponent(
        		new DialogComponentFileChooser(
        				PDBWriterNodeModel.getParamOutput(),
        				"OUTPUT_HISTORY",
        				JFileChooser.SAVE_DIALOG,
        				FileExtensions.PDB));
        this.addDialogComponent(
        		new DialogComponentBoolean(
        				PDBWriterNodeModel.getParamOmitHetero(),
        				"Omit Hetero Groups (non-polymer)"));
    }
}
