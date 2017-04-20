package org.proteinevolution.nodes.input.pdb.theoreticalcrosslinks;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "TheoreticalCrossLinks" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class TheoreticalCrossLinksNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring TheoreticalCrossLinks node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected TheoreticalCrossLinksNodeDialog() {
        super();
    
        this.addDialogComponent(new DialogComponentFileChooser(
        		new SettingsModelString(
        				TheoreticalCrossLinksNodeModel.INPUT_CFGKEY,
        				TheoreticalCrossLinksNodeModel.INPUT_DEFAULT),
        		TheoreticalCrossLinksNodeModel.INPUT_HISTORY,
        		"pdb"));
    }
}

