package org.proteinevolution.knime.nodes.psipred.chkparse;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Chkparse" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class ChkparseNodeFactory extends NodeFactory<ChkparseNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChkparseNodeModel createNodeModel() {

		try {
			return new ChkparseNodeModel();

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
	public NodeView<ChkparseNodeModel> createNodeView(final int viewIndex,
			final ChkparseNodeModel nodeModel) {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {

		return null;
	}
}
