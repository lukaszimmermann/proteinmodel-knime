package org.proteinevolution.knime.nodes.hhsuite.hhfilter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "HHfilter" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class HHfilterNodeFactory 
extends NodeFactory<HHfilterNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HHfilterNodeModel createNodeModel() {

		try {
			return new HHfilterNodeModel();
		} catch (InvalidSettingsException e) {
			
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<HHfilterNodeModel> createNodeView(final int viewIndex,
			final HHfilterNodeModel nodeModel) {
		return new HHfilterNodeView(nodeModel);
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
		return new HHfilterNodeDialog();
	}

}

