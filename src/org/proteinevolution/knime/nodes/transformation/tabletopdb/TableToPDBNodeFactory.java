package org.proteinevolution.knime.nodes.transformation.tabletopdb;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "TableToPDB" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class TableToPDBNodeFactory 
        extends NodeFactory<TableToPDBNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public TableToPDBNodeModel createNodeModel() {
        return new TableToPDBNodeModel();
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
    public NodeView<TableToPDBNodeModel> createNodeView(final int viewIndex,
            final TableToPDBNodeModel nodeModel) {
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
        return new TableToPDBNodeDialog();
    }

}

