package org.proteinevolution.nodes.input.pdb.crosslinktheorist;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "XWalk" Node.
 * This node represents an adaption of the XWalk program originally developed by Kahraman et al. [1]. * n * n[1] Abdullah Kahraman, Lars Malmström, Ruedi Aebersold; Xwalk: computing and visualizing distances in cross-linking experiments. Bioinformatics 2011; 27 (15): 2163-2164. doi: 10.1093/bioinformatics/btr348
 *
 * @author Lukas Zimmermann
 */
public class CrossLinkTheoristNodeFactory 
        extends NodeFactory<CrossLinkTheoristNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CrossLinkTheoristNodeModel createNodeModel() {
        return new CrossLinkTheoristNodeModel();
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
    public NodeView<CrossLinkTheoristNodeModel> createNodeView(final int viewIndex,
            final CrossLinkTheoristNodeModel nodeModel) {
        return new CrossLinkTheoristNodeView(nodeModel);
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
        return new CrossLinkTheoristNodeDialog();
    }

}

