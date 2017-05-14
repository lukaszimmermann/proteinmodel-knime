package org.proteinevolution.nodes.input.hhsuitedbreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "HHsuiteDBReader" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class HHsuiteDBReaderNodeFactory 
        extends NodeFactory<HHsuiteDBReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public HHsuiteDBReaderNodeModel createNodeModel() {
        return new HHsuiteDBReaderNodeModel();
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
    public NodeView<HHsuiteDBReaderNodeModel> createNodeView(final int viewIndex,
            final HHsuiteDBReaderNodeModel nodeModel) {
        return new HHsuiteDBReaderNodeView(nodeModel);
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
        return new HHsuiteDBReaderNodeDialog();
    }

}

