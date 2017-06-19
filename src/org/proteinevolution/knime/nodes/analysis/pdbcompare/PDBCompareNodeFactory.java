package org.proteinevolution.knime.nodes.analysis.pdbcompare;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PDBCompare" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PDBCompareNodeFactory 
        extends NodeFactory<PDBCompareNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PDBCompareNodeModel createNodeModel() {
        return new PDBCompareNodeModel();
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
    public NodeView<PDBCompareNodeModel> createNodeView(final int viewIndex,
            final PDBCompareNodeModel nodeModel) {
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
        return new PDBCompareNodeDialog();
    }
}

