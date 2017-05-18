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

package uk.ac.man.bioinf.sequence.alignment.event; // Package name inserted by JPack



/**
 * AlignmentEventProvider.java
 *
 *
 * Created: Mon Apr 24 16:59:55 2000
 *
 * @author Phillip Lord
 * @version $Id: AlignmentEventProvider.java,v 1.2 2001/04/11 17:04:43 lord Exp $
 */

public interface AlignmentEventProvider 
{
  /**
   * Add a listener specifically interested in events connected with the MSA.
   *
   * @param listener a multiple sequence alignment listener
   */
  public void addAlignmentListener( AlignmentListener listener );
  
  /**
   * Removes a multiple sequence alignment listener.
   *
   * @param listener the listener to be removed
   */
  public void removeAlignmentListener( AlignmentListener listener );
  
  /**
   * Adds a listener interested in vetoing multiple sequence alignment
   * specific events.
   *
   * @param listener a vetoable multiple sequence alignment listener
   */
  public void addVetoableAlignmentListener
    ( VetoableAlignmentListener listener );

  /**
   * Removes a vetoable multiple sequence alignment listener.
   *
   * @param listener the listener to be removed
   */
  public void removeVetoableAlignmentListener 
    ( VetoableAlignmentListener listener );
} // AlignmentEventProvider



/*
 * ChangeLog
 * $Log: AlignmentEventProvider.java,v $
 * Revision 1.2  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/05/08 16:23:25  lord
 * Initial checkin
 *
 */
