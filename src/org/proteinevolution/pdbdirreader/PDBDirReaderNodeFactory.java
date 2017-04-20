package org.proteinevolution.pdbdirreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PDBDirReader" Node.
 * Reads PDB files from a directory into a table with the corresponding file path.
 *
 * @author Lukas Zimmermann
 */
public class PDBDirReaderNodeFactory 
        extends NodeFactory<PDBDirReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PDBDirReaderNodeModel createNodeModel() {
        return new PDBDirReaderNodeModel();
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
    public NodeView<PDBDirReaderNodeModel> createNodeView(final int viewIndex,
            final PDBDirReaderNodeModel nodeModel) {
        return new PDBDirReaderNodeView(nodeModel);
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
        return new PDBDirReaderNodeDialog();
    }

}

