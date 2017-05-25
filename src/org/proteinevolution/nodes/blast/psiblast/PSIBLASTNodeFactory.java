package org.proteinevolution.nodes.blast.psiblast;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PSIBLAST" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PSIBLASTNodeFactory 
extends NodeFactory<PSIBLASTNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PSIBLASTNodeModel createNodeModel() {

		try {
			return new PSIBLASTNodeModel();
			
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
	public NodeView<PSIBLASTNodeModel> createNodeView(final int viewIndex,
			final PSIBLASTNodeModel nodeModel) {
		return new PSIBLASTNodeView(nodeModel);
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
		return new PSIBLASTNodeDialog();
	}

}

