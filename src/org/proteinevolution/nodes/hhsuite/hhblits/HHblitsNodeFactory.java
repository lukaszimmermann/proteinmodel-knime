package org.proteinevolution.nodes.hhsuite.hhblits;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "HHblits" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class HHblitsNodeFactory 
        extends NodeFactory<HHblitsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public HHblitsNodeModel createNodeModel(){
    	
        try {
			return new HHblitsNodeModel();
			
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
    public NodeView<HHblitsNodeModel> createNodeView(final int viewIndex,
            final HHblitsNodeModel nodeModel) {
        return new HHblitsNodeView(nodeModel);
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
        return new HHblitsNodeDialog();
    }

}

