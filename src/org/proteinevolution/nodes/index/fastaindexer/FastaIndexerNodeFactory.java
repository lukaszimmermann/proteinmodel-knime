package org.proteinevolution.nodes.index.fastaindexer;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "FastaIndexer" Node.
 * Indexes cross-link identifications with a FASTA database and adds information on the absolute position and flanking residues
 *
 * @author Lukas Zimmermann
 */
public class FastaIndexerNodeFactory 
        extends NodeFactory<FastaIndexerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FastaIndexerNodeModel createNodeModel() {
        return new FastaIndexerNodeModel();
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
    public NodeView<FastaIndexerNodeModel> createNodeView(final int viewIndex,
            final FastaIndexerNodeModel nodeModel) {
        return new FastaIndexerNodeView(nodeModel);
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
        return new FastaIndexerNodeDialog();
    }

}

