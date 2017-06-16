package org.proteinevolution.knime.nodes.input.pdbdirectoryreader;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;

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
public class PDBDirectoryReaderNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring PDBDirReader node dialog.
	 * This is just a suggestion to demonstrate possible default dialog
	 * components.
	 */
	protected PDBDirectoryReaderNodeDialog() {
		super();

		this.addDialogComponent(new DialogComponentFileChooser(
				PDBDirectoryReaderNodeModel.getParamInput(),
				"INPUT_HISTORY",
				JFileChooser.OPEN_DIALOG,
				true)); 
	}	
}
