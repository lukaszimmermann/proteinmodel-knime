package org.proteinevolution.knime.nodes.util.databaseidmapper;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "DatabaseIDMapper" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class DatabaseIDMapperNodeFactory 
        extends NodeFactory<DatabaseIDMapperNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseIDMapperNodeModel createNodeModel() {
        return new DatabaseIDMapperNodeModel();
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
    public NodeView<DatabaseIDMapperNodeModel> createNodeView(final int viewIndex,
            final DatabaseIDMapperNodeModel nodeModel) {
        return new DatabaseIDMapperNodeView(nodeModel);
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
        return new DatabaseIDMapperNodeDialog();
    }

}

