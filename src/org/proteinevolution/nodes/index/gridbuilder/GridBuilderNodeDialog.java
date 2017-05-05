package org.proteinevolution.nodes.index.gridbuilder;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.proteinevolution.models.spec.pdb.PDB;

/**
 * <code>NodeDialog</code> for the "GridBuilder" Node.
 * Build a grid with a certain grid spacing around a pdb structure. This can currently be used for SASD calculation.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class GridBuilderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring GridBuilder node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected GridBuilderNodeDialog() {
        super();

        // Input PDB file
        this.addDialogComponent(new DialogComponentFileChooser(
        		new SettingsModelString(
        				GridBuilderNodeModel.INPUT_CFGKEY,
        				GridBuilderNodeModel.INPUT_DEFAULT),
        		GridBuilderNodeModel.INPUT_HISTORY,
        		PDB.extensions));            
        
        //
        
        
        
        /* Currently barely useful
        this.addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				GridBuilderNodeModel.SASD_CFGKEY,
        				GridBuilderNodeModel.SASD_DEFAULT),
        		GridBuilderNodeModel.SASD_LABEL));
         */
    }
}
