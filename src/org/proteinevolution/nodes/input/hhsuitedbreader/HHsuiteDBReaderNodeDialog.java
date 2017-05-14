package org.proteinevolution.nodes.input.hhsuitedbreader;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "HHsuiteDBReader" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class HHsuiteDBReaderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring HHsuiteDBReader node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected HHsuiteDBReaderNodeDialog() {
        super();

        this.addDialogComponent(
        		new DialogComponentFileChooser(
        				new SettingsModelString(
        						HHsuiteDBReaderNodeModel.INPUT_CFGKEY,
        						HHsuiteDBReaderNodeModel.INPUT_DEFAULT),
        				HHsuiteDBReaderNodeModel.INPUT_HISTORY,
        				JFileChooser.OPEN_DIALOG,
        				true));
                    
    }
}
