package org.proteinevolution.nodes.hhsuite.hhblits;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
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
       
        // HHsuite database
        this.addDialogComponent(
        		new DialogComponentHHsuiteDBSelection(
        				new SettingsModelStringArray(
        						HHblitsNodeModel.HHSUITEDB_CFGKEY,
        						HHblitsNodeModel.HHSUITEDB_DEFAULT), 
        				"Select HHsuite database", 
        				1)
        		);
       
       // No. of iterations
       this.addDialogComponent(
    		   new DialogComponentStringSelection(
    				   new SettingsModelString(
    						   HHblitsNodeModel.NITERATIONS_CFGKEY,
    						   HHblitsNodeModel.NITERATIONS_DEFAULT),
    				   "Number of Iterations", 
    				   "1", "2", "3", "4", "5", "6", "7", "8"));
       
       // E-value cut-off
       //  -e     [0,1]   E-value cutoff for inclusion in result alignment (def=0.001)  
       
       this.addDialogComponent(
    		   new DialogComponentNumberEdit(
    				   new SettingsModelDoubleBounded(
    						   HHblitsNodeModel.EVALUE_CFGKEY,
    						   HHblitsNodeModel.EVALUE_DEFAULT,
    						   HHblitsNodeModel.EVALUE_MIN,
    						   HHblitsNodeModel.EVALUE_MAX), 
    				   "E-value cutoff"));
    }
}
