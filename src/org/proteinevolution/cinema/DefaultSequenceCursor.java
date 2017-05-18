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
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


/**
 * DefaultSequenceCursor.java
 *
 *
 * Created: Tue Apr  4 12:49:30 2000
 *
 * @author Phillip Lord
 * @version $Id: DefaultSequenceCursor.java,v 1.3 2001/04/11 17:04:42 lord Exp $
 */

public class DefaultSequenceCursor implements SequenceCursor
{
  // listeners
  private ChangeListenerSupport listenerList = new ChangeListenerSupport();
  
  // point and mark
  private SequenceAlignmentPoint point = new SequenceAlignmentPoint();
  private SequenceAlignmentPoint mark  = new SequenceAlignmentPoint();
  
  public void addChangeListener( ChangeListener listener )
  {
    listenerList.addChangeListener( listener );
  }
  
  public void removeChangeListener( ChangeListener listener )
  {
    listenerList.removeChangeListener( listener );
  }
  
  public void setPoint( SequenceAlignmentPoint position )
  {
    point = position;
    listenerList.fireChangeEvent( new ChangeEvent( this ) );
  }
  
  public void setMark()
  {
    mark = point;
    listenerList.fireChangeEvent( new ChangeEvent( this ) );
  }
  
  public SequenceAlignmentPoint getPoint()
  {
    try{
      return (SequenceAlignmentPoint)point.clone();
    }
    catch( CloneNotSupportedException cnse ){
      return null;
    }
  }
  
  public SequenceAlignmentPoint getMark()
  {
    try{
      return (SequenceAlignmentPoint)mark.clone();
    }
    catch( CloneNotSupportedException cnse ){
      return null;
    }
  }
} // DefaultSequenceCursor



/*
 * ChangeLog
 * $Log: DefaultSequenceCursor.java,v $
 * Revision 1.3  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.2  2001/01/04 14:56:31  lord
 * Fixed bug with getting Mark
 *
 * Revision 1.1  2000/04/18 17:43:56  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 * Revision 1.1  2000/04/06 15:46:25  lord
 * Changes to support a cursor, with proper cursor movement
 *
 */
