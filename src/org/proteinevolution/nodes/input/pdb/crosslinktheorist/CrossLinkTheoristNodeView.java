package org.proteinevolution.nodes.input.pdb.crosslinktheorist;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "XWalk" Node.
 * This node represents an adaption of the XWalk program originally developed by Kahraman et al. [1]. * n * n[1] Abdullah Kahraman, Lars Malmström, Ruedi Aebersold; Xwalk: computing and visualizing distances in cross-linking experiments. Bioinformatics 2011; 27 (15): 2163-2164. doi: 10.1093/bioinformatics/btr348
 *
 * @author Lukas Zimmermann
 */
public class CrossLinkTheoristNodeView extends NodeView<CrossLinkTheoristNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link CrossLinkTheoristNodeModel})
     */
    protected CrossLinkTheoristNodeView(final CrossLinkTheoristNodeModel nodeModel) {
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
        CrossLinkTheoristNodeModel nodeModel = 
            (CrossLinkTheoristNodeModel)getNodeModel();
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

