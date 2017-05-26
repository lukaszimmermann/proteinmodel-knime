package org.proteinevolution.knime.nodes.visualization.structureviewer.components;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolViewer;
import org.jmol.viewer.Viewer;


public class JmolPanel extends JPanel {


	private static final long serialVersionUID = 2120845537629532143L;
	
	// statics
	private static final int WIDTH = 1500;
	private static final int HEIGHT = 1000;
	
	
	// instance variables
	private final JmolAdapter adapter;
	private final JmolViewer viewer;
	private Dimension currentSize;
	private Rectangle rectClip;
	

	public JmolPanel() {
	
		// Define viewer and initial dimensions
		this.currentSize = new Dimension(WIDTH, HEIGHT);
		this.rectClip = new Rectangle(WIDTH, HEIGHT);
		this.adapter = new SmarterJmolAdapter();
		this.viewer = Viewer.allocateViewer(this, this.adapter);		
	}
	
	public JmolViewer getViewer() {
	       return this.viewer;
	}
	
	@Override
	public void paintComponent(final Graphics g) {
		
		super.paintComponent(g);
		this.getSize(this.currentSize);
		g.getClipBounds(this.rectClip);
		
		//this.setCartoon();
		this.viewer.renderScreenImage(g, WIDTH, HEIGHT);
	}
	
	
	@Override
	public void setSize(Dimension d) {
		
		super.setSize(d);
		
		this.currentSize = d;
		this.rectClip.setSize(d);
	}
	
	public void setCartoon() {
		
		this.viewer.evalString("select protein, nucleic");
		this.viewer.evalString("cartoon only");
		this.viewer.evalString("color cartoon structure");		
	}
}

/*
public class JmolViewerPanel extends JPanel {

	@Override
	public void paintComponent(final Graphics g) {

		super.paintComponent(g);
		getSize(currentSize);
		g.getClipBounds(rectClip);
		viewer.renderScreenImage(g, currentSize, rectClip);
		lastZoomFactorInt = viewer.getZoomPercent();
	}


	public void setMolecules(final IAtomContainerSet molecules) {
		
		ChemModel model = new ChemModel();
		model.setMoleculeSet(molecules);
		ChemSequence sequence = new ChemSequence();
		sequence.addChemModel(model);
		ChemFile chemFile = new ChemFile();
		chemFile.addChemSequence(sequence);

		synchronized (viewer) {
			viewer.openClientFile("", "", chemFile);
		}
		
		JMolViewerKNIMEUtils.zoomToPercent(viewer, lastZoomFactorInt);
		
		repaint();
	}
}
*/
