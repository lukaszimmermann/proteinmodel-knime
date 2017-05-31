package org.proteinevolution.knime.porttypes.alignment.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.MemoryImageSource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;

import org.knime.core.node.NodeLogger;
import org.proteinevolution.models.interfaces.ISequenceAlignmentAnnotated;

public class JAlignmentPane extends JComponent {

	private static final long serialVersionUID = 1L;
	public static final int MAX_CHARSIZE_TO_DRAW = 6;

	// The Alignment to render
	private final ISequenceAlignmentAnnotated alignment;

	private static final NodeLogger logger = NodeLogger
			.getLogger(JAlignmentPane.class);

	
	// Dimensions
	private int charWidth = 12;
	private int charHeight = 14;

	// Character Pixels
	private CharPixels[] charPixels;

	// The color map
	private ColorMap colorMap = ColorMap.TAYLOR;

	public JAlignmentPane(final ISequenceAlignmentAnnotated alignment) {

		this.alignment = alignment;
		this.charPixels = new CharPixels[256];


		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {

				if (e.isControlDown()) {

					int clicks = e.getWheelRotation();

					JAlignmentPane.this.charHeight += clicks;
					JAlignmentPane.this.charWidth += clicks;
					JAlignmentPane.this.repaint();
				}
			}
		});

	}

	public void setColorMap(final ColorMap colorMap) {

		this.colorMap = colorMap;
		this.repaint();
	}

	@Override
	protected void paintComponent(final Graphics g) {

		super.paintComponent(g);

		int nSequences = this.alignment.getNumberSequences();
		
		for(int n = 0; n < this.charPixels.length; n++){	

			this.charPixels[n] = new CharPixels(
					(char)n,
					this.charWidth,
					this.charHeight,
					Color.BLACK,
					this.colorMap.getColor((char) n), 
					new Font(Font.MONOSPACED, Font.PLAIN, (int)charWidth), 
					MAX_CHARSIZE_TO_DRAW);
		}
		Graphics2D g2d = (Graphics2D) g;
		Rectangle clip = g2d.getClipBounds();

		int matrixMinX = Math.max(0, (int) Math.floor(clip.getMinX()/this.charWidth)); // always round down
		int matrixMinY = Math.max(0, (int) Math.floor(clip.getMinY()/this.charHeight)); // always round down

		Rectangle matrixClip = new Rectangle(
				matrixMinX,
				matrixMinY,
				Math.max(0, (int) Math.floor(clip.getMaxX()/this.charWidth)) - matrixMinX,
				Math.max(0, (int) Math.floor(clip.getMaxY()/this.charHeight)) - matrixMinY);

		int xMin = matrixClip.x - 1;
		int yMin = matrixClip.y - 1;
		int xMax = (int) matrixClip.getMaxX() + 1;
		int yMax = (int) matrixClip.getMaxY() + 1;

		
		int yMinAnn = Math.min(alignment.getNumAnnotations(), yMin);
		yMinAnn = Math.max(0, yMinAnn);

		int yMaxAnn = Math.min(alignment.getNumAnnotations(), yMax);
		
		// adjust for part of matrix that exists (TODO Simplify)
		xMin = Math.min(alignment.getLength(), xMin);
		xMin = Math.max(0, xMin);

		yMin = Math.min(alignment.getNumberSequences(), yMin);
		yMin = Math.max(0, yMin);

		xMax = Math.min(alignment.getLength(), xMax);
		yMax = Math.min(alignment.getNumberSequences(), yMax);

		
		// Extra because pixelCopyDraw
		int height = ((yMax - yMin) + (yMaxAnn - yMinAnn)) * this.charHeight;
		int width = (xMax - xMin) * this.charWidth;

		RGBArray clipRGB = new RGBArray(new int[width * height], width, height);

		// TODO One might want to treat small characters differently

		// Set the Executor
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

		// Draw Sequence Alignment
		for(int y = yMin; y < yMax; y = y + 1) {
			SequencePainter seqPainter = new SequencePainter(
					this.alignment.getSequenceAt(y), 
					y,      // seqYPos
					xMin,   // xposStart
					xMax,   // xPos End
					1,      // normalCharSeqPerPix
					charWidth, 
					charHeight, 
					clipRGB, 
					this.charPixels);	
			executor.execute(seqPainter);
		}

		// Draw Annotation
		
		for(int y = yMinAnn; y < yMaxAnn; y = y + 1) {
			
			SequencePainter seqPainter = new SequencePainter(
					this.alignment.getAnnotationAt(y), 
					nSequences + y,      // seqYPos
					xMin,   // xposStart
					xMax,   // xPos End
					1,      // normalCharSeqPerPix
					charWidth, 
					charHeight, 
					clipRGB, 
					this.charPixels);	
			executor.execute(seqPainter);
		}
		
		executor.shutdown();

		try {
			executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//		// Draw Excludes by manipulating pixelColor			
		//		if(! isShowTranslationOnePos()){
		//			// Two versions depending on if it is small chars or not
		//			if(charWidth < 1){
		//				for(int x = clip.x; x < clip.getMaxX() ; x++){
		//					int xPos =(int)((double)x * (1/(double)charWidth));
		//					if(alignment.isExcluded(xPos) == true){	
		//						logger.info("is excl");
		//						ImageUtils.darkerRGBArrayColumn(clipRGB, x);
		//					}
		//				}
		//			}else{
		//				for(int x = xMin; x < xMax ; x++){
		//					if(alignment.isExcluded(x) == true){
		//
		//						for(int col = x; col < charWidth; col++){
		//							logger.info("is excl");
		//							ImageUtils.darkerRGBArrayColumn(clipRGB, col);
		//						}
		//
		//					}
		//				}
		//			}
		//		}


		// Now draw the pixels onto the image
		Image img = createImage(
				new MemoryImageSource(clipRGB.getScanWidth(),
						clipRGB.getHeight(),
						clipRGB.getBackend(),
						0, 
						clipRGB.getScanWidth()));

		// First fill background
		g2d.setColor(this.getBackground());
		g2d.fill(clip);

		int clipRGBXPos = clip.x;
		int clipRGBYPos = clip.y;

		// Adjust because we start always on exact char upp to one pos before
		if(charWidth > 1){
			clipRGBXPos = (int)(xMin * charWidth);
			clipRGBYPos = (int)(yMin * charHeight);
		}

		if (img != null){	
			// Mac retina screen
			g2d.drawImage(img, clipRGBXPos, clipRGBYPos, null);

		}
	}
}


