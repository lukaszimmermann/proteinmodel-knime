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
import java.awt.Component;
import java.awt.Color;

/**
 * AlignmentViewerCellRenderer.java
 *
 * Initial interface for the AlignmentViewerCellRender. This defines a
 * component which is used to render an individual cell for a
 * JAlignmentViewer. Its not complete yet. Its needs information about
 * the colour model to be used for this cell. And possible info about
 * the difference between the alignment position, and the viewer
 * position, Im not sure. 
 *
 * Created: Mon Mar 20 15:50:14 2000
 *
 * @author Phillip Lord
 * @version $Id: AlignmentViewerCellRenderer.java,v 1.2 2001/04/11 17:04:42 lord Exp $ 
 */

public interface AlignmentViewerCellRenderer {
  /**
   * This method should be able to cope with null values for element
   * which will be passed if we are off the front or the end of the sequence
   * @param viewer
   * @param element
   * @param sequenceIndex
   * @param bgColor the background color of the cell (can be null)
   * @param isSelected
   * @param cellHasFocus
   * @return
   */
  public Component getAlignmentViewerCellRendererComponent( 
      JAlignmentViewer viewer, 
      Element element, 
      SequenceAlignmentPoint location,
      Color bgColor,
      boolean isSelected,
      boolean hasFocus,
      boolean isAtPoints );
}// AlignmentViewerCellRenderer


/*
 * ChangeLog
 * $Log: AlignmentViewerCellRenderer.java,v $
 * Revision 1.2  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/04/18 17:43:55  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 * Revision 1.4  2000/04/12 13:41:13  jns
 * o added in color mapping code
 *
 * Revision 1.3  2000/04/06 15:46:25  lord
 * Changes to support a cursor, with proper cursor movement
 *
 * Revision 1.2  2000/03/29 15:50:47  lord
 * Updated to use new sequence.geom package
 *
 * Revision 1.1  2000/03/21 14:41:39  lord
 * Initial checkin
 * 
 */

