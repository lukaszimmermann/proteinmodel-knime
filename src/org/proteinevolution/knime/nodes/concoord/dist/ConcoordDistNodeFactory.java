package org.proteinevolution.knime.nodes.concoord.dist;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ConcoordDist" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class ConcoordDistNodeFactory 
        extends NodeFactory<ConcoordDistNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ConcoordDistNodeModel createNodeModel() {
    	
    	try {
    		return new ConcoordDistNodeModel();
    		
    	} catch(InvalidSettingsException e) {
    		
    		throw new RuntimeException(e.getMessage());
    	}
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
    public NodeView<ConcoordDistNodeModel> createNodeView(final int viewIndex,
            final ConcoordDistNodeModel nodeModel) {
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
        return new ConcoordDistNodeDialog();
    }
}
