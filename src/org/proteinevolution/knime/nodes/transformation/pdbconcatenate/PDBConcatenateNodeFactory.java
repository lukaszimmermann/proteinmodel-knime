package org.proteinevolution.knime.nodes.transformation.pdbconcatenate;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PDBConcatenate" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBConcatenateNodeFactory 
        extends NodeFactory<PDBConcatenateNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PDBConcatenateNodeModel createNodeModel() {
        return new PDBConcatenateNodeModel();
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
    public NodeView<PDBConcatenateNodeModel> createNodeView(final int viewIndex,
            final PDBConcatenateNodeModel nodeModel) {
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

