package org.proteinevolution.knime.nodes.input.alignmentreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "AlignmentReader" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class AlignmentReaderNodeFactory 
        extends NodeFactory<AlignmentReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AlignmentReaderNodeModel createNodeModel() {
        return new AlignmentReaderNodeModel();
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
    public NodeView<AlignmentReaderNodeModel> createNodeView(final int viewIndex,
            final AlignmentReaderNodeModel nodeModel) {
        return new AlignmentReaderNodeView(nodeModel);
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
        return new AlignmentReaderNodeDialog();
    }
}
