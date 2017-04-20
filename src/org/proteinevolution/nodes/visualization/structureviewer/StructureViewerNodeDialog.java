package org.proteinevolution.nodes.visualization.structureviewer;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "StructureViewer" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class StructureViewerNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring StructureViewer node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected StructureViewerNodeDialog() {
        super();
        
        
        /*
        this.addDialogComponent(new DialogComponentColumnNameSelection(
        		new SettingsModelString(
        				StructureViewerNodeModel.STRUC_CFGKEY,
        				StructureViewerNodeModel.STRUC_DEFAULT),
        		StructureViewerNodeModel.STRUC_LABEL,
        		0,
        		true, // required
        		false,// add none col
        		PdbValue.class));
        */
        
        this.addDialogComponent(new DialogComponentNumber(
        		new SettingsModelInteger(
        				StructureViewerNodeModel.ROW_CFGKEY,
        				StructureViewerNodeModel.ROW_DEFAULT),
        		StructureViewerNodeModel.ROW_LABEL,
        		1));
               
        this.addDialogComponent(new DialogComponentFileChooser(
        		new SettingsModelString(
        				StructureViewerNodeModel.INPUT_CFGKEY,
        				StructureViewerNodeModel.INPUT_DEFAULT),
        		StructureViewerNodeModel.INPUT_HISTORY,
        		"pdb"));
        
    }
}

