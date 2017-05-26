package org.proteinevolution.knime.nodes.input.pdbdirectoryreader;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "PDBDirReader" Node.
 * Reads PDB files from a directory into a table with the corresponding file path.
 *
 * @author Lukas Zimmermann
 */
public class PDBDirectoryReaderNodeView extends NodeView<PDBDirectoryReaderNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link PDBDirectoryReaderNodeModel})
     */
    protected PDBDirectoryReaderNodeView(final PDBDirectoryReaderNodeModel nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        PDBDirectoryReaderNodeModel nodeModel = 
            (PDBDirectoryReaderNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

