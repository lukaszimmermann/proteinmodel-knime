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



/**
 * SingleAlignmentSelectionModel.java
 *
 *
 * Created: Mon Apr 10 11:59:44 2000
 *
 * @author Phillip Lord
 * @version $Id: SingleAlignmentSelectionModel.java,v 1.5 2001/04/11 17:04:42 lord Exp $
 */

public class SingleAlignmentSelectionModel implements AlignmentSelectionModel
{
  private boolean isSelecting = false;
  // cached rectangle
  private SequenceAlignmentRectangle currentSelection = new SequenceAlignmentRectangle();
  private AlignmentSelectionListenerSupport list = new AlignmentSelectionListenerSupport();
  // points, start and stop
  private SequenceAlignmentPoint start, stop;

  public boolean isSelecting()
  {
    return isSelecting;
  }

  public SequenceAlignmentRectangle getCurrentSelection()
  {
    if( start == null ) return null;
    return currentSelection;
  }
  
  public int getNumberSelections()
  {
    return (start == null)? 0: 1;
  }
  
  public SequenceAlignmentRectangle getSelectionAt( int index )
    throws ArrayIndexOutOfBoundsException
  {
    if( start == null || index > 0 )
      throw new ArrayIndexOutOfBoundsException( "No such selection rectangle" );
    
    return getCurrentSelection();
  }
  
  public boolean isPointSelected( SequenceAlignmentPoint point )
  {
    SequenceAlignmentRectangle select = getCurrentSelection();
    
    return ( select != null ) ? select.contains( point ): false;
  }
    
  public void clearSelection()
  {
    SequenceAlignmentRectangle rect = getCurrentSelection();
    
    start = stop = null;
    list.fireAlignmentSelectionEvent
      ( new AlignmentSelectionEvent( this, rect, false ) );
    isSelecting = false;
  }
  
  public SequenceAlignmentRectangle getRectangleAtPoint( SequenceAlignmentPoint point )
  {
    SequenceAlignmentRectangle select = getCurrentSelection();
    if( select.contains( point ) ){
      return select;
    }
    return null;
  }
  
  public SequenceAlignmentRectangle[] getRectanglesAtPoint( SequenceAlignmentPoint point )
  {
    SequenceAlignmentRectangle retn = getRectangleAtPoint( point );
    if( retn == null ) return null;
    SequenceAlignmentRectangle[] retnArray ={ retn };
    return retnArray;
  }
  
  private void recalcSelectionRectangle()
  {
    currentSelection.setLocation( start );
    currentSelection.setSize( 1, 1 );
    currentSelection.add( stop );
  }
  
  public void extendSelection( SequenceAlignmentPoint point )
  {
    if( start == null || !isSelecting ){
      start = point;
      isSelecting = true;
    }
    stop = point;
    
    recalcSelectionRectangle();
    list.fireAlignmentSelectionEvent
      ( new AlignmentSelectionEvent( this, getCurrentSelection(), isSelecting ) );
  }
  
  public void stopSelection( SequenceAlignmentPoint point )
  {
    if( start == null || !isSelecting ){
      start = point;
    }
    stop = point;
    recalcSelectionRectangle();
    isSelecting = false;
    list.fireAlignmentSelectionEvent
      ( new AlignmentSelectionEvent( this, getCurrentSelection(), isSelecting ) );
  }

  public void addAlignmentSelectionListener( AlignmentSelectionListener listener )
  {
    list.addAlignmentSelectionListener( listener );
  }
  
  public void removeAlignmentSelectionListener( AlignmentSelectionListener listener )
  {
    list.addAlignmentSelectionListener( listener );
  }
} // SingleAlignmentSelectionModel



/*
 * ChangeLog
 * $Log: SingleAlignmentSelectionModel.java,v $
 * Revision 1.5  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.4  2001/01/19 19:53:00  lord
 * Removed references to variable called changed which was doing nothing
 * at all.
 *
 * Revision 1.3  2001/01/15 18:55:40  lord
 * Improved event handling.
 * Removed one method due to interface change.
 *
 * Revision 1.2  2000/12/18 12:13:27  jns
 * o getting rid of system.out.println to avoid noisy output out of debug
 * mode
 *
 * Revision 1.1  2000/04/18 17:43:56  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 * Revision 1.2  2000/04/13 15:38:06  lord
 * Too many changes to document. Essentially a complete re-implementation
 *
 * Revision 1.1  2000/04/11 16:56:23  lord
 * Initial version. It compiles, but that does not mean it works
 *
 */



