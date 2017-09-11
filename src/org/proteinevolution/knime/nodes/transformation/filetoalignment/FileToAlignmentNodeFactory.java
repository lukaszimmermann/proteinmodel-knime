package org.proteinevolution.knime.nodes.transformation.filetoalignment;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "FileToAlignment" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class FileToAlignmentNodeFactory 
        extends NodeFactory<FileToAlignmentNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FileToAlignmentNodeModel createNodeModel() {
        return new FileToAlignmentNodeModel();
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
    public NodeView<FileToAlignmentNodeModel> createNodeView(final int viewIndex,
            final FileToAlignmentNodeModel nodeModel) {
    	
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

