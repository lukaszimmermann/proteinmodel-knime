package org.proteinevolution.nodes.index.fastaindexer;

import java.util.Arrays;

import org.knime.core.data.IntValue;
import org.knime.core.data.StringValue;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "FastaIndexer" Node.
 * Indexes cross-link identifications with a FASTA database and adds information on the absolute position and flanking residues
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class FastaIndexerNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring FastaIndexer node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected FastaIndexerNodeDialog() {
        super();
        
        this.addDialogComponent(new DialogComponentFileChooser(
        			new SettingsModelString(FastaIndexerNodeModel.INPUT_CFGKEY,
        					FastaIndexerNodeModel.INPUT_DEFAULT),
        		FastaIndexerNodeModel.INPUT_HISTORYKEY,
        		FastaIndexerNodeModel.INPUT_VALIDEXT));
        
 
        // seq
        this.addDialogComponent(new DialogComponentColumnNameSelection(
        		new SettingsModelColumnName(
        				FastaIndexerNodeModel.SEQ_CFGKEY,
        				FastaIndexerNodeModel.SEQ_DEFAULT),
        		"Peptide sequence (X is varmod)",
        		0, true, false, StringValue.class));
        
        // pos
        this.addDialogComponent(new DialogComponentColumnNameSelection(
        		new SettingsModelColumnName(
        				FastaIndexerNodeModel.POS_CFGKEY,
        				FastaIndexerNodeModel.POS_DEFAULT),
        		"Column with XL position (1-based)",
        		0, true, false, IntValue.class));
        
        //  prot
        this.addDialogComponent(new DialogComponentColumnNameSelection(
        		new SettingsModelColumnName(
        				FastaIndexerNodeModel.PROT_CFGKEY,
        				FastaIndexerNodeModel.PROT_DEFAULT),
        		"String Set with ProtIDs. <none> for exhaustive searching.",
        		0, true, true, SetCell.getCollectionType(StringCell.TYPE).getPreferredValueClass()));
        
        // modification
        this.addDialogComponent(new DialogComponentStringSelection(
        		new SettingsModelString(
        				FastaIndexerNodeModel.MOD_CFGKEY,
        				FastaIndexerNodeModel.MOD_DEFAULT), 
        		"Variable Modification",
        		Arrays.asList("M", "Q", "E", "W", "R", "T", "I", "P", "A", "S", "D", "F", "G", "K", "L", "Y", "C", "V", "N", "H"), false));
        
        
    }
}

