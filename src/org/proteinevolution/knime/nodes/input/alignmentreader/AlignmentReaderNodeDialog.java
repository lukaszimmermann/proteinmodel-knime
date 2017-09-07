package org.proteinevolution.knime.nodes.input.alignmentreader;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;

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
final class AlignmentReaderNodeDialog extends DefaultNodeSettingsPane {
	
    protected AlignmentReaderNodeDialog() {
    	
        this.addDialogComponent(
        		new DialogComponentFileChooser(
        				AlignmentReaderNodeModel.getInput(),
        				"INPUT_HISTORY",
        				"fasta|fas|fa"));
    }
}
