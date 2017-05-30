package org.proteinevolution.knime.nodes.psipred.psipred;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PSIPRED" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PSIPREDNodeFactory 
        extends NodeFactory<PSIPREDNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PSIPREDNodeModel createNodeModel() {
    	
    	try {
        return new PSIPREDNodeModel();
    	} catch (InvalidSettingsException e) {
    		
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
    public NodeView<PSIPREDNodeModel> createNodeView(final int viewIndex,
            final PSIPREDNodeModel nodeModel) {
    	
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return null;
    }
}

