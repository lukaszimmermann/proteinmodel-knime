package org.proteinevolution.nodes.index.gridbuilder;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "GridBuilder" Node.
 * Build a grid with a certain grid spacing around a pdb structure. This can currently be used for SASD calculation.
 *
 * @author Lukas Zimmermann
 */
public class GridBuilderNodeFactory 
        extends NodeFactory<GridBuilderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GridBuilderNodeModel createNodeModel() {
        return new GridBuilderNodeModel();
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
    public NodeView<GridBuilderNodeModel> createNodeView(final int viewIndex,
            final GridBuilderNodeModel nodeModel) {
        return new GridBuilderNodeView(nodeModel);
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
        return new GridBuilderNodeDialog();
    }

}

