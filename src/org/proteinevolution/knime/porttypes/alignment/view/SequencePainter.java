package org.proteinevolution.knime.porttypes.alignment.view;

public class SequencePainter implements Runnable {

	private final char[] seq;
	private final int clipPosY;
	private final int xMinSeqPos;
	private final int xMaxSeqPos;
	private final double seqPerPix;
	private final double charWidth;
	private final double charHeight;
	private final RGBArray clipRGB;
	private final CharPixels[] charPixels;


	public  SequencePainter(
			final char[] seq,
			final int clipPosY,
			final int xMinSeqPos,
			final int xMaxSeqPos,
			final double seqPerPix,
			final double charWidth,
			final double charHeight,
			final RGBArray clipRGB,
			final CharPixels[] charPixels) {

		this.seq = seq;
		this.clipPosY = clipPosY;
		this.xMinSeqPos = xMinSeqPos;
		this.xMaxSeqPos = xMaxSeqPos;
		this.seqPerPix = seqPerPix;
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		this.clipRGB = clipRGB;
		this.charPixels = charPixels;
	}



	public void run() {

		if (seq != null){

			// Make sure not outside length of seq
			int seqLength = seq.length;
			int clipPosX = 0;

			for(int x = this.xMinSeqPos; x < this.xMaxSeqPos && x >=0 ; ++x) {

				int seqXPos = (int)((double)x * this.seqPerPix);

				if(seqXPos >=0 && seqXPos < seqLength){

					int pixelPosX = (int)(clipPosX*charWidth);
					int pixelPosY = (int)(clipPosY*charHeight);

					if(pixelPosX < clipRGB.getScanWidth() && pixelPosY < clipRGB.getHeight()){

						ImageUtils.insertRGBArrayAt(pixelPosX, pixelPosY, charPixels[seq[seqXPos]].getRGBArray(), clipRGB);
					}
				}
				clipPosX ++;
			}
		}
	}
}
