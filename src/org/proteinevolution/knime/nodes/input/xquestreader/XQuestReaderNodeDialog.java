package org.proteinevolution.knime.nodes.input.xquestreader;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

/**
 * <code>NodeDialog</code> for the "XQuestReader" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class XQuestReaderNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring XQuestReader node dialog.
	 * This is just a suggestion to demonstrate possible default dialog
	 * components.
	 */
	protected XQuestReaderNodeDialog() {
		super();

		this.addDialogComponent(new DialogComponentFileChooser(
				XQuestReaderNodeModel.getParamInputFile(),
				"INPUT_FILE_HISTORY",
				"xml"));

		this.addDialogComponent(
				new DialogComponentString(
						XQuestReaderNodeModel.getDecoyString(),
						"Decoy String"));
	}
}
