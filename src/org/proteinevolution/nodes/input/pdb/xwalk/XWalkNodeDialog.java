package org.proteinevolution.nodes.input.pdb.xwalk;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "XWalk" Node.
 * This node represents an adaption of the XWalk program originally developed by Kahraman et al. [1]. * n * n[1] Abdullah Kahraman, Lars Malmström, Ruedi Aebersold; Xwalk: computing and visualizing distances in cross-linking experiments. Bioinformatics 2011; 27 (15): 2163-2164. doi: 10.1093/bioinformatics/btr348
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class XWalkNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring XWalk node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected XWalkNodeDialog() {
        super();
        
        this.addDialogComponent(new DialogComponentFileChooser(
        		new SettingsModelString(
        				XWalkNodeModel.INPUT_CFGKEY,
        				XWalkNodeModel.INPUT_DEFAULT),
        		XWalkNodeModel.INPUT_HISTORY,
        		"pdb"));
    }
}
