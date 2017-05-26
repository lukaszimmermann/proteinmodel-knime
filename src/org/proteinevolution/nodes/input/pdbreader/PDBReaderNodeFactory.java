package org.proteinevolution.nodes.input.pdbreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PDBFetcher" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBReaderNodeFactory 
        extends NodeFactory<PDBReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PDBReaderNodeModel createNodeModel() {
        return new PDBReaderNodeModel();
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
    public NodeView<PDBReaderNodeModel> createNodeView(final int viewIndex,
            final PDBReaderNodeModel nodeModel) {
        return new PDBReaderNodeView(nodeModel);
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
        return new PDBReaderNodeDialog();
    }

}

