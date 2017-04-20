package org.proteinevolution.nodes.input.pdb.theoreticalcrosslinks;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "TheoreticalCrossLinks" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class TheoreticalCrossLinksNodeFactory 
        extends NodeFactory<TheoreticalCrossLinksNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public TheoreticalCrossLinksNodeModel createNodeModel() {
        return new TheoreticalCrossLinksNodeModel();
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
    public NodeView<TheoreticalCrossLinksNodeModel> createNodeView(final int viewIndex,
            final TheoreticalCrossLinksNodeModel nodeModel) {
        return new TheoreticalCrossLinksNodeView(nodeModel);
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
        return new TheoreticalCrossLinksNodeDialog();
    }

}

