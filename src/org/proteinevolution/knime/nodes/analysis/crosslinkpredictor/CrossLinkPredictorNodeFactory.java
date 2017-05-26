package org.proteinevolution.knime.nodes.analysis.crosslinkpredictor;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "XWalk" Node.
 * This node represents an adaption of the XWalk program originally developed by Kahraman et al. [1]. * n * n[1] Abdullah Kahraman, Lars Malmström, Ruedi Aebersold; Xwalk: computing and visualizing distances in cross-linking experiments. Bioinformatics 2011; 27 (15): 2163-2164. doi: 10.1093/bioinformatics/btr348
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
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<CrossLinkPredictorNodeModel> createNodeView(final int viewIndex,
            final CrossLinkPredictorNodeModel nodeModel) {
        return new CrossLinkPredictorNodeView(nodeModel);
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

