package org.proteinevolution.knime.nodes.psipred.psipass2;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;

/**
 * <code>NodeDialog</code> for the "Psipass2" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class Psipass2NodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring Psipass2 node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected Psipass2NodeDialog() {
        super();
                    
        this.addDialogComponent(
        		new DialogComponentNumber(
        				new SettingsModelInteger(
        						Psipass2NodeModel.ITERCOUNT_CFGKEY,
        						1),
        				"No. of iterations",
        				1));
        
        this.addDialogComponent(
        		new DialogComponentNumberEdit(
        				new SettingsModelDouble(Psipass2NodeModel.DCA_CFGKEY, 1.0),
        				"DCA"));
        
        this.addDialogComponent(
        		new DialogComponentNumberEdit(
        				new SettingsModelDouble(Psipass2NodeModel.DCB_CFGKEY, 1.0),
        				"DCB"));
    }
}
