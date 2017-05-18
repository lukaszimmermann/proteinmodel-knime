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
import java.awt.Graphics;
import uk.ac.man.bioinf.gui.viewer.JAlignmentViewer;
import uk.ac.man.bioinf.sequence.Element;
import uk.ac.man.bioinf.sequence.geom.SequenceAlignmentPoint;
import java.awt.Color;
import java.awt.FontMetrics;
import uk.ac.man.bioinf.sequence.alignment.Gap;


/**
 * DefaultFastAlignmentViewerCellRenderer.java
 *
 *
 * Created: Mon Apr 17 20:11:46 2000
 *
 * @author Phillip Lord
 * @version $Id: DefaultFastAlignmentViewerCellRenderer.java,v 1.6 2001/04/11 17:04:42 lord Exp $
 */

public class DefaultFastAlignmentViewerCellRenderer implements FastAlignmentViewerCellRenderer
{
  public char[] charArray = new char[ 1 ];
  
  public void renderAlignmentViewerCell
    ( Graphics g, int x, int y, int width, int height, 
      JAlignmentViewer viewer, Element element, 
      SequenceAlignmentPoint location, Color bgColor, 
      boolean isSelected, boolean hasFocus, boolean isAtPoint )
  {
    if( bgColor == null || element == Gap.GAP )
      bgColor = viewer.getBackground();
    
    Color borderColor = Color.black;
    
    if( element != null ){
      
      
      //draw the contents colour
      g.setColor( bgColor );
      g.fillRect( x + 1, y + 1, width - 2, height - 2 );
      
      
      
      // now draw the char. 
      // (PENDING:- PL) Need to work out the metrics properly. 
      g.setColor( Color.black );
      FontMetrics mets = g.getFontMetrics();
      int  yOff, xOff;
      int charWidth = mets.charWidth( element.toChar() );
      xOff = (width - charWidth ) / 2;
      yOff = (height + mets.getHeight() - mets.getDescent()) / 2;
      
      charArray[ 0 ] = element.toChar();
      g.drawChars( charArray, 0, 1, x + xOff, y + yOff );
      //if( Debug.debug ) Debug.message( "Drawing cell within " + x + " " + y + " " 
      //			       + width + " " + height + " .Char is at " + x
      //			       + " " + (y + yOff) );
      
    }
    else{
      borderColor = bgColor;
      g.setColor( viewer.getBackground() );
      g.fillRect( x, y, width, height );
    }

    // choose the border colour
    if( isSelected ){
      borderColor = Color.red;
    }
    if( isAtPoint ){
      borderColor = Color.green;
    }
    //draw the border
    g.setColor( borderColor );
    g.drawRect( x, y, width - 1, height - 1 );
  }
} // DefaultFastAlignmentViewerCellRenderer



/*
 * ChangeLog
 * $Log: DefaultFastAlignmentViewerCellRenderer.java,v $
 * Revision 1.6  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.5  2001/01/31 17:54:43  lord
 * Now draws selection borders even if there is not element.
 *
 * Revision 1.4  2000/09/18 17:55:42  jns
 * o change of function name to make more sense
 *
 * Revision 1.3  2000/05/24 15:38:41  lord
 * Various fiddling. This class is in serious need to a complete rewrite
 *
 * Revision 1.2  2000/04/20 14:15:38  lord
 * Can now cope with null colour map
 *
 * Revision 1.1  2000/04/18 17:43:56  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 */
