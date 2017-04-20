package org.proteinevolution.structureviewer;



import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.biojava.nbio.structure.Structure;
import org.jmol.util.Logger;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeView;
import org.proteinevolution.structureviewer.components.JmolPanel;



/**
 * <code>NodeView</code> for the "StructureViewer" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class StructureViewerNodeView extends NodeView<StructureViewerNodeModel> {

	
	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(StructureViewerNodeView.class);
	
	private final JPanel mainPanel;
	private final JmolPanel jmolPanel;
	private final JPanel controlPanel;
	
    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link StructureViewerNodeModel})
     */
    protected StructureViewerNodeView(final StructureViewerNodeModel nodeModel) {
    	
        super(nodeModel);
        
        // mainPanel
        this.mainPanel = new JPanel(new GridBagLayout());
        
        // Control Panel
        this.controlPanel = new JPanel();
        
        
        // Fetch the Structure from the Model
        Structure struc = nodeModel.getStructure();
        // Initialize Panel
        this.jmolPanel = new JmolPanel();
        this.jmolPanel.getViewer().openStringInline(struc.toPDB());
        
        this.mainPanel.add(this.controlPanel);
        this.mainPanel.add(this.jmolPanel);
        
       
        this.setComponent(this.jmolPanel); 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        StructureViewerNodeModel nodeModel = 
            (StructureViewerNodeModel)getNodeModel();
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

