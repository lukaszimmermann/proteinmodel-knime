package org.proteinevolution.knime.nodes.output.alignmentwriter;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "AlignmentWriter" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class AlignmentWriterNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring AlignmentWriter node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected AlignmentWriterNodeDialog() {
        super();
        
        this.addDialogComponent(
        		new DialogComponentFileChooser(
        				new SettingsModelString(
        						AlignmentWriterNodeModel.OUTPUT_CFGKEY,
        						AlignmentWriterNodeModel.OUTPUT_DEFAULT),
        				AlignmentWriterNodeModel.OUTPUT_HISTORY,
        				JFileChooser.SAVE_DIALOG,
        				"fas|fasta|fa"));
    }
}
