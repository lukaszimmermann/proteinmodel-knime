package org.proteinevolution.knime.nodes.output.pdbwriter;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PDBWriter" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBWriterNodeFactory 
        extends NodeFactory<PDBWriterNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PDBWriterNodeModel createNodeModel() {
        return new PDBWriterNodeModel();
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
    public NodeView<PDBWriterNodeModel> createNodeView(final int viewIndex,
            final PDBWriterNodeModel nodeModel) {
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
        return new PDBWriterNodeDialog();
    }

}

