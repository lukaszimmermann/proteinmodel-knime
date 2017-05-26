package org.proteinevolution.knime.nodes.hhsuite.hhsearch;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.proteinevolution.knime.porttypes.hhsuitedb.DialogComponentHHsuiteDBSelection;

/**
 * <code>NodeDialog</code> for the "HHsearch" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class HHsearchNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring HHsearch node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected HHsearchNodeDialog() {
        super();
    
        // HHsuite database
        this.addDialogComponent(
        		new DialogComponentHHsuiteDBSelection(
        				new SettingsModelStringArray(
        						HHsearchNodeModel.HHSUITEDB_CFGKEY,
        						HHsearchNodeModel.HHSUITEDB_DEFAULT), 
        				"Select HHsuite database", 
        				1)
        		);
       
       
       this.addDialogComponent(
    		   new DialogComponentNumberEdit(
    				   new SettingsModelDoubleBounded(
    						   HHsearchNodeModel.EVALUE_CFGKEY,
    						   HHsearchNodeModel.EVALUE_DEFAULT,
    						   HHsearchNodeModel.EVALUE_MIN,
    						   HHsearchNodeModel.EVALUE_MAX), 
    				   "E-value cutoff"));
       this.addDialogComponent(
    		   new DialogComponentNumberEdit(
    				   new SettingsModelDoubleBounded(
    						   HHsearchNodeModel.QID_CFGKEY,
    						   HHsearchNodeModel.QID_DEFAULT,
    						   HHsearchNodeModel.QID_MIN,
    						   HHsearchNodeModel.QID_MAX), 
    				   "Minimum sequence identity with master sequence (%)"));
       
       this.addDialogComponent(
    		   new DialogComponentNumberEdit(
    				   new SettingsModelDoubleBounded(
    						   HHsearchNodeModel.COV_CFGKEY,
    						   HHsearchNodeModel.COV_DEFAULT,
    						   HHsearchNodeModel.COV_MIN,
    						   HHsearchNodeModel.COV_MAX), 
    				   "Minimum coverage with master sequence (%)"));
    }
}
