package org.proteinevolution.knime.nodes.analysis.crosslinkpredictor;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "CrossLinkPredictor" Node.
 *
 * @author Lukas Zimmermann
 */
public class CrossLinkPredictorNodeFactory 
        extends NodeFactory<CrossLinkPredictorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CrossLinkPredictorNodeModel createNodeModel() {
        return new CrossLinkPredictorNodeModel();
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
    public NodeView<CrossLinkPredictorNodeModel> createNodeView(final int viewIndex,
            final CrossLinkPredictorNodeModel nodeModel) {
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
        return new CrossLinkPredictorNodeDialog();
    }

}

