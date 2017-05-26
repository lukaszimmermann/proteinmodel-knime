package org.proteinevolution.knime.nodes.input.plinkreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PLinkReader" Node.
 * 
 *
 * @author 
 */
public class PLinkReaderNodeFactory 
        extends NodeFactory<PLinkReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PLinkReaderNodeModel createNodeModel() {
        return new PLinkReaderNodeModel();
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
    public NodeView<PLinkReaderNodeModel> createNodeView(final int viewIndex,
            final PLinkReaderNodeModel nodeModel) {
        return new PLinkReaderNodeView(nodeModel);
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
        return new PLinkReaderNodeDialog();
    }

}

