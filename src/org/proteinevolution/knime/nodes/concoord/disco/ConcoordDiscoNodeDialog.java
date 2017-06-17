package org.proteinevolution.knime.nodes.concoord.disco;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.proteinevolution.knime.nodes.concoord.ConcoordBaseNodeModel;

/**
 * <code>NodeDialog</code> for the "ConcoordDisco" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class ConcoordDiscoNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring ConcoordDisco node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected ConcoordDiscoNodeDialog() {
        super();
     
        
		this.addDialogComponent(
				new DialogComponentStringSelection(
						ConcoordDiscoNodeModel.getParamAtomsMargin(), 
						"Select Van-der-Waals parameters", 
						ConcoordBaseNodeModel.getAtomsMarginsList()));
		
		this.addDialogComponent(
				new DialogComponentStringSelection(
						ConcoordDiscoNodeModel.getParamBonds(),
						"Select bond/angle parameters",
						ConcoordBaseNodeModel.getBondsList()));
        
        
        this.addDialogComponent(new DialogComponentNumber(
        		ConcoordDiscoNodeModel.getParamNoStructures(),
        		"Nr. of structures to be generated. Default: 500", 
        		1));
    }
}
