package org.proteinevolution.knime.nodes.index.gridbuilder;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "GridBuilder" Node.
 * Build a grid with a certain grid spacing around a pdb structure. This can currently be used for SASD calculation.
 *
 * @author Lukas Zimmermann
 */
public class GridBuilderNodeView extends NodeView<GridBuilderNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link GridBuilderNodeModel})
     */
    protected GridBuilderNodeView(final GridBuilderNodeModel nodeModel) {
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
        GridBuilderNodeModel nodeModel = 
            (GridBuilderNodeModel)getNodeModel();
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

