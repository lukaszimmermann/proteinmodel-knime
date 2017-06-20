package org.proteinevolution.knime.nodes.transformation.tabletopdb;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.proteinevolution.knime.porttypes.structure.StructureCell;

/**
 * <code>NodeDialog</code> for the "TableToPDB" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class TableToPDBNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring TableToPDB node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected TableToPDBNodeDialog() {
        super();            
        
        this.addDialogComponent(new DialogComponentColumnNameSelection(
        		TableToPDBNodeModel.getParamInput(), 
        		"Select column with structures",
        		0,
        		true,
        		StructureCell.TYPE.getPreferredValueClass()));
    }
}
