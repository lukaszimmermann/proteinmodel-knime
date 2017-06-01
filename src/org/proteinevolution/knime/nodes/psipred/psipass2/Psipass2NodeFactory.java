package org.proteinevolution.knime.nodes.psipred.psipass2;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Psipass2" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class Psipass2NodeFactory 
        extends NodeFactory<Psipass2NodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Psipass2NodeModel createNodeModel() {
    	
    	try {
        return new Psipass2NodeModel();
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
    public NodeView<Psipass2NodeModel> createNodeView(final int viewIndex,
            final Psipass2NodeModel nodeModel) {
    	
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
        return new Psipass2NodeDialog();
    }

}

