package org.proteinevolution.nodes.input.alignmentreader;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "AlignmentReader" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class AlignmentReaderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring AlignmentReader node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected AlignmentReaderNodeDialog() {
        super();

        this.addDialogComponent(
        		new DialogComponentFileChooser(
        				new SettingsModelString(
        						AlignmentReaderNodeModel.INPUT_CFGKEY,
        						AlignmentReaderNodeModel.INPUT_DEFAULT),
        				AlignmentReaderNodeModel.INPUT_HISTORY,
        				"fasta|fas|fa"));
    }
}

