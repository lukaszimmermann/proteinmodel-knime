package org.proteinevolution.knime.nodes.analysis.annotatealignment;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "AnnotateAlignment" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class AnnotateAlignmentNodeFactory 
        extends NodeFactory<AnnotateAlignmentNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotateAlignmentNodeModel createNodeModel() {
        return new AnnotateAlignmentNodeModel();
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
    public NodeView<AnnotateAlignmentNodeModel> createNodeView(final int viewIndex,
            final AnnotateAlignmentNodeModel nodeModel) {
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
        return new AnnotateAlignmentNodeDialog();
    }

}

