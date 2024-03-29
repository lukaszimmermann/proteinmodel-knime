package org.proteinevolution.knime.nodes.analysis.pdbcompare;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "PDBCompare" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class PDBCompareNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring PDBCompare node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected PDBCompareNodeDialog() {
        super();
    }
}
