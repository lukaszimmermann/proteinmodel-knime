package org.proteinevolution.nodes.input.xquestreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "XQuestReader" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class XQuestReaderNodeFactory 
        extends NodeFactory<XQuestReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public XQuestReaderNodeModel createNodeModel() {
        return new XQuestReaderNodeModel();
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
    public NodeView<XQuestReaderNodeModel> createNodeView(final int viewIndex,
            final XQuestReaderNodeModel nodeModel) {
        return new XQuestReaderNodeView(nodeModel);
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
        return new XQuestReaderNodeDialog();
    }

}

