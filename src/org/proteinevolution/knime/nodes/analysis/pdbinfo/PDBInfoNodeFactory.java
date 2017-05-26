package org.proteinevolution.knime.nodes.analysis.pdbinfo;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PDBInfo" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBInfoNodeFactory 
        extends NodeFactory<PDBInfoNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PDBInfoNodeModel createNodeModel() {
        return new PDBInfoNodeModel();
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
    public NodeView<PDBInfoNodeModel> createNodeView(final int viewIndex,
            final PDBInfoNodeModel nodeModel) {
        return new PDBInfoNodeView(nodeModel);
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
        return new PDBInfoNodeDialog();
    }

}

