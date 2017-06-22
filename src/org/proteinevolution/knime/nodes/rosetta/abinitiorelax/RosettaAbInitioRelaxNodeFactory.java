package org.proteinevolution.knime.nodes.rosetta.abinitiorelax;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RosettaAbInitioRelax" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class RosettaAbInitioRelaxNodeFactory 
extends NodeFactory<RosettaAbInitioRelaxNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RosettaAbInitioRelaxNodeModel createNodeModel() {

		try {
			return new RosettaAbInitioRelaxNodeModel();

		} catch (InvalidSettingsException e) {

			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<RosettaAbInitioRelaxNodeModel> createNodeView(final int viewIndex,
			final RosettaAbInitioRelaxNodeModel nodeModel) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {
		return new RosettaAbInitioRelaxNodeDialog();
	}

}

