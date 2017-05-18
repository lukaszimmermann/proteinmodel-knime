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
import java.util.ArrayList;
import java.util.Iterator;


/**
 * AlignmentSelectionListenerSupport.java
 *
 *
 * Created: Mon Apr 10 12:31:19 2000
 *
 * @author Phillip Lord
 * @version $Id: AlignmentSelectionListenerSupport.java,v 1.2 2001/04/11 17:04:42 lord Exp $
 */

public class AlignmentSelectionListenerSupport 
{
  private ArrayList listenerList = new ArrayList();
  
  public void addAlignmentSelectionListener( AlignmentSelectionListener listener )
  {
    if( !listenerList.contains( listener ) )
      listenerList.add( listener );
  }
  
  public void removeAlignmentSelectionListener( AlignmentSelectionListener listener )
  {
    listenerList.remove( listener );
  }
  
  public void fireAlignmentSelectionEvent( AlignmentSelectionEvent event )
  {
    Iterator iter = listenerList.iterator();
    
    while( iter.hasNext() ){
      ((AlignmentSelectionListener)iter.next()).valueChanged( event );
    }
  }
} // AlignmentSelectionListenerSupport



/*
 * ChangeLog
 * $Log: AlignmentSelectionListenerSupport.java,v $
 * Revision 1.2  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/04/18 17:45:24  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 * Revision 1.1  2000/04/11 16:56:23  lord
 * Initial version. It compiles, but that does not mean it works
 *
 */
