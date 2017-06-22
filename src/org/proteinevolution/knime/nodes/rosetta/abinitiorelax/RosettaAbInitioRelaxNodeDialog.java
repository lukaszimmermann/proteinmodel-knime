package org.proteinevolution.knime.nodes.rosetta.abinitiorelax;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

/**
 * <code>NodeDialog</code> for the "RosettaAbInitioRelax" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Lukas Zimmermann
 */
public class RosettaAbInitioRelaxNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring RosettaAbInitioRelax node dialog.
	 * This is just a suggestion to demonstrate possible default dialog
	 * components.
	 */
	protected RosettaAbInitioRelaxNodeDialog() {
		super();

		this.setDefaultTabTitle("Ab Initio");

		this.addDialogComponent(
				new DialogComponentBoolean(
						RosettaAbInitioRelaxNodeModel.getParamRelax(),
						"Do a relax after abinitio = abrelax"));

		this.addDialogComponent(new DialogComponentNumber(
				RosettaAbInitioRelaxNodeModel.getParamIncCycles(),
				"Increase number of cycles at each stage of fold_abinitio (or pose_abinitio) by this factor",
				1));

		this.addDialogComponent(
				new DialogComponentNumberEdit(
						RosettaAbInitioRelaxNodeModel.getParamRgReweight(),
						"Reweight contribution of radius of gyration to total score by this scale factor"));

		this.addDialogComponent(
				new DialogComponentNumberEdit(
						RosettaAbInitioRelaxNodeModel.getParamRsdWtHelix(),
						"Reweight env, pair, and cb scores for helix residues by this factor"));

		this.addDialogComponent(
				new DialogComponentNumberEdit(
						RosettaAbInitioRelaxNodeModel.getParamRsdWtLoop(),
						"Reweight env, pair, and cb scores for loop residues by this factor"));

		this.createNewTab("Relax");

		this.addDialogComponent(
				new DialogComponentBoolean(
						RosettaAbInitioRelaxNodeModel.getParamRelaxFast(),
						"At the end of the de novo protein_folding, do a relax step of type \"FastRelax\"."));

	}
}

// -rg_reweight 0.5        # Reweight contribution of radius of gyration to total score by this scale factor