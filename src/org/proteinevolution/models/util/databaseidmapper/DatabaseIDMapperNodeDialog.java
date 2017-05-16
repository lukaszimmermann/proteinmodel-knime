package org.proteinevolution.models.util.databaseidmapper;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "DatabaseIDMapper" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class DatabaseIDMapperNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring DatabaseIDMapper node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected DatabaseIDMapperNodeDialog() {
        super();
        
        
        this.addDialogComponent(new DialogComponentColumnNameSelection(
        		new SettingsModelString(
        				DatabaseIDMapperNodeModel.INPUT_COLUMN_CFGKEY,
        				DatabaseIDMapperNodeModel.INPUT_COLUMN_DEFAULT), 
        		"Accession Column",
        		0,
        		true,
        		StringValue.class));           
    }
}
