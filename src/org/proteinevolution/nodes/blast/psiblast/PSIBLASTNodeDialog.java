package org.proteinevolution.nodes.blast.psiblast;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "PSIBLAST" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class PSIBLASTNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring PSIBLAST node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected PSIBLASTNodeDialog() {
        super();
        
        // Database pal file
        this.addDialogComponent(
        		new DialogComponentFileChooser(
        				new SettingsModelString(
        						PSIBLASTNodeModel.DATABASE_CFGKEY,
        						PSIBLASTNodeModel.DATABASE_DEFAULT),
        				PSIBLASTNodeModel.DATABASE_HISTORY, 
        				"pal"));
        
        // Inclusion etresh
        this.addDialogComponent(
        		new DialogComponentNumberEdit(
        				new SettingsModelDoubleBounded(
        						PSIBLASTNodeModel.INCLUSION_ETRESH_CFGKEY,
        						PSIBLASTNodeModel.INCLUSION_ETRESH_DEFAULT,
        						PSIBLASTNodeModel.INCLUSION_ETRESH_MIN,
        						PSIBLASTNodeModel.INCLUSION_ETRESH_MAX),
        				"Inclusion E-value threshold (-inclusion_ethresh)"));
        
        // No of iterations
        this.addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				PSIBLASTNodeModel.N_ITERATIONS_CFGKEY,
        				PSIBLASTNodeModel.N_ITERATIONS_DEFAULT,
        				PSIBLASTNodeModel.N_ITERATIONS_MIN,
        				PSIBLASTNodeModel.N_ITERATIONS_MAX),
        		"No. of iterations (-num_iterations)",
        		1));
        
        // No of alignments
        this.addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				PSIBLASTNodeModel.N_ALIGNMENTS_CFGKEY,
        				PSIBLASTNodeModel.N_ALIGNMENTS_DEFAULT,
        				PSIBLASTNodeModel.N_ALIGNMENTS_MIN,
        				PSIBLASTNodeModel.N_ALIGNMENTS_MAX),
        		"No. of alignments (-num_alignments)",
        		1));
     
        // No. of descriptions
        this.addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				PSIBLASTNodeModel.N_DESCRIPTIONS_CFGKEY,
        				PSIBLASTNodeModel.N_DESCRIPTIONS_DEFAULT,
        				PSIBLASTNodeModel.N_DESCRIPTIONS_MIN,
        				PSIBLASTNodeModel.N_DESCRIPTIONS_MAX),
        		"No. of descriptions (-num_descriptions)",
        		1));
    }
}

