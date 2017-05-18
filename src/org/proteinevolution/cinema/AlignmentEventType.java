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


// TODO Probably also a meaningless class



/**
 * AlignmentEventType.java
 *
 * This class provides an enumerated type for the AlignmentEvent class
 * to pass around as an identifier
 *
 * Created: Tue Feb 15 20:26:54 2000
 *
 * @author Phillip Lord
 * @version $Id: AlignmentEventType.java,v 1.6 2001/04/11 17:04:43 lord Exp $
 */

public final class AlignmentEventType 
{ 
  //Enum support
  private String toString;
  private AlignmentEventType( String toString )
  {
    this.toString = toString;
  }

  public String toString()
  {
    return super.toString() + " : Type " + toString;
  }
  
  //EventTypes

  /**
   * Event type indicating a new sequence has been added to the
   * sequence alignment
   */
  public static final AlignmentEventType INSERT = new AlignmentEventType( "Insertion" );
  
  /**
   * Event type indicated a sequence has been removed from the
   * sequence alignment 
   */
  public static final AlignmentEventType DELETE = new AlignmentEventType( "Deletion" );
  
  /**
   * Event type indicating a change in the inset/preceeding number of gaps
   */
  public static final AlignmentEventType INSET_CHANGE = new AlignmentEventType( "Inset changed" );

  /**
   * Event type indicating that the length of the alignment has
   * changed.
   */
  public static final AlignmentEventType LENGTH_CHANGE = new AlignmentEventType( "Length changed" );
  
  /**
   * This type says that the alignment has changed in some unknown
   * way. This is very useful when its hard to work out what has
   * changed. Its probably going to be fairly inefficient as well, and
   * should be removed when feasible so that optimisations are possible.
   */
  public static final AlignmentEventType UNSPECIFIED = new AlignmentEventType( "Unspecified change" );
  
}// AlignmentEventType


/*
 * ChangeLog
 * $Log: AlignmentEventType.java,v $
 * Revision 1.6  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.5  2001/03/12 16:49:45  lord
 * Added documentation and new type
 *
 * Revision 1.4  2000/06/13 11:06:35  lord
 * Added new event type
 *
 * Revision 1.3  2000/03/27 18:48:34  lord
 * Added Length change event
 *
 * Revision 1.2  2000/03/17 18:25:21  jns
 * adding a new event to indicate the change of the inset.
 *
 * Revision 1.1  2000/03/02 19:22:30  jns
 * initial code.
 *
 * 
 */

