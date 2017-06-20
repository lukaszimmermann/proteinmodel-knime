package org.proteinevolution.knime.nodes.transformation.pdbtotable;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PDBToTable" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBToTableNodeFactory 
        extends NodeFactory<PDBToTableNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PDBToTableNodeModel createNodeModel() {
        return new PDBToTableNodeModel();
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
    public NodeView<PDBToTableNodeModel> createNodeView(final int viewIndex,
            final PDBToTableNodeModel nodeModel) {
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

