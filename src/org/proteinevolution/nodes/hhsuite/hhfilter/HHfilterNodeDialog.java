package org.proteinevolution.nodes.hhsuite.hhfilter;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.proteinevolution.models.knime.hhsuitedb.DialogComponentHHsuiteDBSelection;
import org.proteinevolution.nodes.hhsuite.hhblits.HHblitsNodeModel;

/**
 * <code>NodeDialog</code> for the "HHfilter" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class HHfilterNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring HHfilter node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected HHfilterNodeDialog() {
        super();

        this.addDialogComponent(
      		   new DialogComponentNumberEdit(
      				   new SettingsModelDoubleBounded(
      						   HHfilterNodeModel.ID_CFGKEY,
      						   HHfilterNodeModel.ID_DEFAULT,
      						   HHfilterNodeModel.ID_MIN,
      						   HHfilterNodeModel.ID_MAX), 
      				   "Maximum pairwise sequence identity (%)"));
         
        this.addDialogComponent(
     		   new DialogComponentNumberEdit(
     				   new SettingsModelDoubleBounded(
     						   HHfilterNodeModel.QID_CFGKEY,
     						   HHfilterNodeModel.QID_DEFAULT,
     						   HHfilterNodeModel.QID_MIN,
     						   HHfilterNodeModel.QID_MAX), 
     				   "Minimum sequence identity with query (%)"));
        
       this.addDialogComponent(
    		   new DialogComponentNumberEdit(
    				   new SettingsModelDoubleBounded(
    						   HHfilterNodeModel.COV_CFGKEY,
    						   HHfilterNodeModel.COV_DEFAULT,
    						   HHfilterNodeModel.COV_MIN,
    						   HHfilterNodeModel.COV_MAX), 
    				   "Minimum coverage with query (%)"));
       
       this.addDialogComponent(
    		   new DialogComponentNumberEdit(
    				   new SettingsModelIntegerBounded(
    						   HHfilterNodeModel.DIFF_CFGKEY,
    						   HHfilterNodeModel.DIFF_DEFAULT,
    						   HHfilterNodeModel.DIFF_MIN,
    						   HHfilterNodeModel.DIFF_MAX), 
    				   "Min. number of diverse sequences."));
    }
}

