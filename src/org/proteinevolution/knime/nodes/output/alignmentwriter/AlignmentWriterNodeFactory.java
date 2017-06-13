package org.proteinevolution.knime.nodes.output.alignmentwriter;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "AlignmentWriter" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class AlignmentWriterNodeFactory 
        extends NodeFactory<AlignmentWriterNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AlignmentWriterNodeModel createNodeModel() {
        return new AlignmentWriterNodeModel();
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
    public NodeView<AlignmentWriterNodeModel> createNodeView(final int viewIndex,
            final AlignmentWriterNodeModel nodeModel) {
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
        return new AlignmentWriterNodeDialog();
    }

}

