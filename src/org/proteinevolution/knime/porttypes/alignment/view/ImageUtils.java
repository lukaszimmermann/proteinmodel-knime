package org.proteinevolution.knime.porttypes.alignment.view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


public final class ImageUtils {


	private ImageUtils() {

		throw new AssertionError();
	}


	public static void insertRGBArrayAt(
			final int targetX,
			final int targetY,
			final RGBArray newPiece, 
			final RGBArray origPiece) {

		//UnSafe unsafe = Unsafe.getUnsafe();
		int offset = targetY * origPiece.getScanWidth() + targetX; // first pos in target array

		// Outer loop is each scanline
		for(int scanCount = 0; scanCount < newPiece.getHeight(); scanCount ++){
			// Inner loop is each pixel per scan
			//	int posOrig = offset + scanCount * origPiece.getScanWidth();
			//	int posNewPiece = scanCount * newPiece.getScanWidth();
			//System.arraycopy(origPiece.backend, posOrig, newPiece.backend, posNewPiece, newPiece.getScanWidth());

			for(int n = 0; n < newPiece.getScanWidth(); n++){
				int posOrig = offset + scanCount * origPiece.getScanWidth() + n;
				int posNewPiece = scanCount * newPiece.getScanWidth() + n;
				origPiece.backend[posOrig] = newPiece.backend[posNewPiece];		
			}	
		}
	}

	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img)
	{
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	public static void darkerRGBArrayColumn(RGBArray clipRGB, int colPos) {

		for(int posInArray = colPos; posInArray < clipRGB.getLength(); posInArray += clipRGB.getScanWidth()){		
			int colVal = clipRGB.getBackend()[posInArray];
			clipRGB.getBackend()[posInArray] = ColorUtils.darkerRGB(colVal);
		}	
	}
}
