package org.proteinevolution.nodes.input.xquestreader;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

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
        		new SettingsModelString(
        				XQuestReaderNodeModel.INPUT_CFGKEY, 
        				XQuestReaderNodeModel.INPUT_DEFAULT),
        		XQuestReaderNodeModel.INPUT_HISTORYKEY,
        		XQuestReaderNodeModel.INPUT_VALIDEXT));
        
        this.addDialogComponent(new DialogComponentString(
        		new SettingsModelString(
        				XQuestReaderNodeModel.DECOY_CFGKEY,
        				XQuestReaderNodeModel.DECOY_DEFAULT), 
        		"Decoy String"));
    }
}
