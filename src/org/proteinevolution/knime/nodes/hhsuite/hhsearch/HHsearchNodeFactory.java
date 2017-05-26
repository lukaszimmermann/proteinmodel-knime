package org.proteinevolution.knime.nodes.hhsuite.hhsearch;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "HHsearch" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class HHsearchNodeFactory 
        extends NodeFactory<HHsearchNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public HHsearchNodeModel createNodeModel() {
    	
    	try {
    		return new HHsearchNodeModel();
    		
    	} catch( InvalidSettingsException e) {
    		
    		throw new RuntimeException(e.getMessage());
    	}
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
    public NodeView<HHsearchNodeModel> createNodeView(final int viewIndex,
            final HHsearchNodeModel nodeModel) {
        return new HHsearchNodeView(nodeModel);
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
        return new HHsearchNodeDialog();
    }

}

