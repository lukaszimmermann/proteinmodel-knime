package org.proteinevolution.knime.nodes.input.pdbatomreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PDBATOMReader" Node.
 * Reads ATOM records from a PDB file and lists them in KNIME table.
 *
 * @author Lukas Zimmermann
 */
public class PDBATOMReaderNodeFactory 
        extends NodeFactory<PDBATOMReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PDBATOMReaderNodeModel createNodeModel() {
        return new PDBATOMReaderNodeModel();
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
    public NodeView<PDBATOMReaderNodeModel> createNodeView(final int viewIndex,
            final PDBATOMReaderNodeModel nodeModel) {
        return new PDBATOMReaderNodeView(nodeModel);
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
        return new PDBATOMReaderNodeDialog();
    }

}

