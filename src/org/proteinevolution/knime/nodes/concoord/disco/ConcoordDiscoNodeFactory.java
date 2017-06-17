package org.proteinevolution.knime.nodes.concoord.disco;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ConcoordDisco" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class ConcoordDiscoNodeFactory 
extends NodeFactory<ConcoordDiscoNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConcoordDiscoNodeModel createNodeModel() {

		try {
			return new ConcoordDiscoNodeModel();
		} catch(InvalidSettingsException e) {

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
	public NodeView<ConcoordDiscoNodeModel> createNodeView(final int viewIndex,
			final ConcoordDiscoNodeModel nodeModel) {
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
		return new ConcoordDiscoNodeDialog();
	}

}

