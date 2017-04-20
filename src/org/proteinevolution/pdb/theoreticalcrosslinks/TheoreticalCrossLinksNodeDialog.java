package org.proteinevolution.pdb.theoreticalcrosslinks;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "TheoreticalCrossLinks" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class TheoreticalCrossLinksNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring TheoreticalCrossLinks node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected TheoreticalCrossLinksNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    TheoreticalCrossLinksNodeModel.CFGKEY_COUNT,
                    TheoreticalCrossLinksNodeModel.DEFAULT_COUNT,
                    Integer.MIN_VALUE, Integer.MAX_VALUE),
                    "Counter:", /*step*/ 1, /*componentwidth*/ 5));
                    
    }
}

