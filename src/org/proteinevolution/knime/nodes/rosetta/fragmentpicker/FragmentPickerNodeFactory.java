package org.proteinevolution.knime.nodes.rosetta.fragmentpicker;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "FragmentPicker" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class FragmentPickerNodeFactory 
        extends NodeFactory<FragmentPickerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FragmentPickerNodeModel createNodeModel() {
    	
    	try {
        return new FragmentPickerNodeModel();
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
    public NodeView<FragmentPickerNodeModel> createNodeView(final int viewIndex,
            final FragmentPickerNodeModel nodeModel) {
    	
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
        return new FragmentPickerNodeDialog();
    }
}

