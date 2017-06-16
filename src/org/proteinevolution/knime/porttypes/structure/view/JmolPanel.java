package org.proteinevolution.knime.porttypes.structure.view;

import java.awt.Graphics;

import javax.swing.JPanel;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolViewer;
import org.jmol.viewer.Viewer;


public class JmolPanel extends JPanel {


	private static final long serialVersionUID = 2120845537629532143L;
	
	// statics
	private static final int WIDTH = 1500;
	private static final int HEIGHT = 1000;
	
	private final JmolViewer viewer;

	public JmolPanel() {
	
		this.viewer = Viewer.allocateViewer(this, new SmarterJmolAdapter());		
	}
	
	public JmolViewer getViewer() {
	       return this.viewer;
	}
	
	@Override
	public void paintComponent(final Graphics g) {
		
		super.paintComponent(g);
		
		this.viewer.renderScreenImage(g, WIDTH, HEIGHT);
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
