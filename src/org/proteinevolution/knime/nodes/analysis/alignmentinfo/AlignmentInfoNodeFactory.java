package org.proteinevolution.knime.nodes.analysis.alignmentinfo;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "AlignmentInfo" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class AlignmentInfoNodeFactory 
        extends NodeFactory<AlignmentInfoNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AlignmentInfoNodeModel createNodeModel() {
        return new AlignmentInfoNodeModel();
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
    public NodeView<AlignmentInfoNodeModel> createNodeView(final int viewIndex,
            final AlignmentInfoNodeModel nodeModel) {
    	
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

