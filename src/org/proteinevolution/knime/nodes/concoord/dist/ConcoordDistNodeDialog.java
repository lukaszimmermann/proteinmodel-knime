package org.proteinevolution.knime.nodes.concoord.dist;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;

/**
 * <code>NodeDialog</code> for the "ConcoordDist" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class ConcoordDistNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring ConcoordDist node dialog.
	 * This is just a suggestion to demonstrate possible default dialog
	 * components.
	 */
	protected ConcoordDistNodeDialog() {
		super();

		this.addDialogComponent(
				new DialogComponentStringSelection(
						ConcoordDistNodeModel.getParamAtomsMargin(), 
						"Select Van-der-Waals parameters", 
						ConcoordDistNodeModel.getAtomsMarginsList()));
		
		this.addDialogComponent(
				new DialogComponentStringSelection(
						ConcoordDistNodeModel.getParamBonds(),
						"Select bond/angle parameters",
						ConcoordDistNodeModel.getBondsList()));
		
		this.addDialogComponent(new DialogComponentBoolean(
				ConcoordDistNodeModel.getParamRetainHydrogenAtoms(),
				"Retain hydrogen atoms"));

		this.addDialogComponent(new DialogComponentBoolean(
				ConcoordDistNodeModel.getParamFindAlternativeContacts(),
				"Try to find alternatives for non-bonded interactions (by default the native contacts will be preserved). Warning: EXPERIMENTAL!"));

		this.addDialogComponent(new DialogComponentNumber(
				ConcoordDistNodeModel.getParamCutOffRadius(),
				"Cut-off radius (Angstroms) for non-bonded interacting pairs (default 4.0)", 1));
	}
}
