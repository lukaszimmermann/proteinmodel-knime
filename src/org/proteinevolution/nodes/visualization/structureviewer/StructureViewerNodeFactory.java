package org.proteinevolution.nodes.visualization.structureviewer;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "StructureViewer" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class StructureViewerNodeFactory 
        extends NodeFactory<StructureViewerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public StructureViewerNodeModel createNodeModel() {
        return new StructureViewerNodeModel();
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
    public NodeView<StructureViewerNodeModel> createNodeView(final int viewIndex,
            final StructureViewerNodeModel nodeModel) {
        return new StructureViewerNodeView(nodeModel);
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
        return new StructureViewerNodeDialog();
    }

}

