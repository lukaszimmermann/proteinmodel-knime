/*
 *This library is free software; you can redistribute it and/or
 *modify it under the terms of the GNU Lesser General Public
 *License as published by the Free Software Foundation; either
 *version 2.1 of the License, or (at your option) any later version.
 *
 *This library is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *Lesser General Public License for more details.
 *
 *You should have received a copy of the GNU Lesser General Public
 *License along with this library; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

/* 
 * This software was written by Phillip Lord (p.lord@hgmp.mrc.ac.uk)
 * whilst at the University of Manchester as a Pfizer post-doctoral 
 * Research Fellow. 
 *
 * The initial code base is copyright by Pfizer, or the University
 * of Manchester. Modifications to the initial code base are copyright
 * of their respective authors, or their employers as appropriate. 
 * Authorship of the modifications may be determined from the ChangeLog
 * placed at the end of this file
 */

package org.proteinevolution.cinema; // Package name inserted by JPack
import java.awt.Color;
import java.awt.Graphics;


/**
 * FastAlignmentViewerCellRenderer.java
 *
 * Although the JAlignmentViewer provides a pluggable renderer
 * architecture with the AlignmentViewerCellRenderer it has been found
 * that this has a fairly disasterous effect on performance of the
 * viewer particularly with respect to scrolling, due to the large
 * number of method calls and events to render a cell. This class
 * circumvents all of that, and still provides a degree of
 * plugability. The disadvantage of using this class is that its much
 * lower level. You have to do all of the drawing directly onto the
 * Graphics context provided, and you can not just simply extend a JComponent.
 * 
 * This interface is the same as for the AlignmentViewerCellRenderer
 * with the additional information that is required, which is the
 * graphics context, where to draw on it, and the size to draw. 
 *
 * Created: Mon Apr 17 20:03:52 2000
 *
 * @author Phillip Lord
 * @version $Id: FastAlignmentViewerCellRenderer.java,v 1.4 2001/04/11 17:04:42 lord Exp $
 */

public interface FastAlignmentViewerCellRenderer 
{
  /**
   * This method renders the cell at the given location in a graphics
   * object, with reference to the JAlignmentViewer. It makes use of
   * certain parameters, such as the color of the element being
   * drawn.
   *
   * @param g the graphics object in use
   * @param x the 'x' location in the graphical display
   * @param y the 'y' location in the graphical display
   * @param width the width of the cell
   * @param height the height of the cell
   * @param viewer the JAlignmentViewer object
   * @param element the element being drawn
   * @param location the sequence alignment location of the element
   * @param bgColor the background color of the element
   * @param isSelected whether the element is selected
   * @param hasFocus whether the element has focus
   * @param isAtPoint whether the cursor is at the elements point
   */
  public void renderAlignmentViewerCell
    ( Graphics g, int x, int y, int width, int height, 
      JAlignmentViewer viewer, Element element, 
      SequenceAlignmentPoint location, Color bgColor, 
      boolean isSelected, boolean hasFocus, boolean isAtPoint );
} // FastAlignmentViewerCellRenderer



/*
 * ChangeLog
 * $Log: FastAlignmentViewerCellRenderer.java,v $
 * Revision 1.4  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.3  2001/03/12 16:34:52  lord
 * Import rationalisation
 *
 * Revision 1.2  2000/09/18 17:55:42  jns
 * o change of function name to make more sense
 *
 * Revision 1.1  2000/04/18 17:43:56  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 */
