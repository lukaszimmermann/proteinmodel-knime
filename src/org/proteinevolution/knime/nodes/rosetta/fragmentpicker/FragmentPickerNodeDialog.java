package org.proteinevolution.knime.nodes.rosetta.fragmentpicker;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "FragmentPicker" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class FragmentPickerNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring FragmentPicker node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected FragmentPickerNodeDialog() {
        super();
                
        this.addDialogComponent(
        		new DialogComponentString(
        				new SettingsModelString(
        						FragmentPickerNodeModel.CFG_SSNAME,
        						"predA"),
        				"Name of Secondary Structure", 
        				true,
        				20));
    }
}

