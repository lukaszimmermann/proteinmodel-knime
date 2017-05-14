package org.proteinevolution.nodes.hhsuite.hhblits;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.proteinevolution.models.knime.hhsuitedb.DialogComponentHHsuiteDBSelection;

/**
 * <code>NodeDialog</code> for the "HHblits" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class HHblitsNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring HHblits node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected HHblitsNodeDialog() {
        super();
       
        
        this.addDialogComponent(
        		new DialogComponentHHsuiteDBSelection(
        				new SettingsModelStringArray(
        						HHblitsNodeModel.HHSUITEDB_CFGKEY,
        						HHblitsNodeModel.HHSUITEDB_DEFAULT), 
        				"Select HHsuite database", 
        				1)
        		);
    }
}
