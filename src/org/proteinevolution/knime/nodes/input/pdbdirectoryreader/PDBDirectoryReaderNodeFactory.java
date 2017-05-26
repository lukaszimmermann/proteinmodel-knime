package org.proteinevolution.knime.nodes.input.pdbdirectoryreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PDBDirReader" Node.
 * Reads PDB files from a directory into a table with the corresponding file path.
 *
 * @author Lukas Zimmermann
 */
public class PDBDirectoryReaderNodeFactory 
        extends NodeFactory<PDBDirectoryReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PDBDirectoryReaderNodeModel createNodeModel() {
        return new PDBDirectoryReaderNodeModel();
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
    public NodeView<PDBDirectoryReaderNodeModel> createNodeView(final int viewIndex,
            final PDBDirectoryReaderNodeModel nodeModel) {
        return new PDBDirectoryReaderNodeView(nodeModel);
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
        return new PDBDirectoryReaderNodeDialog();
    }

}

