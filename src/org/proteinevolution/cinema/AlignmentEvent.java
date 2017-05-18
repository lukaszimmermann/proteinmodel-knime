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
 * This software was written by Julian Selley (j.selley@man.ac.uk)
 * whilst at the University of Manchester as a Research Assistant. 
 *
 * The initial code base is copyright by the University of Manchester. 
 * Modifications to the initial code base are copyright of their 
 * respective authors, or their employers as appropriate. Authorship 
 * of the modifications may be determined from the ChangeLog placed 
 * at the end of this file
 */

package org.proteinevolution.cinema; // Package name inserted by JPack

import java.util.EventObject;


/**
 * AlignmentEvent.java
 *
 * Instances of this class represent an event to a MSA. It includes
 * reference to the location of the event, as well as its type.
 *
 * Created: Tue Feb 15 19:57:49 2000
 *
 * @author J Selley
 * @version $Id: AlignmentEvent.java,v 1.3 2001/04/11 17:04:43 lord Exp $
 */

public class AlignmentEvent extends EventObject
{

  private int start;  // the start location of the event
  private int end;  // the end location of the even
  private AlignmentEventType type;  // the type of event (an enum model)
  
  /**
   * Constructor to set the event source, location and type.
   *
   * @param src the source of the event (i.e: the MSA)
   * @param location the location of the event, or zero if not known
   * @param type the multiple sequence event type
   */
  public AlignmentEvent(Object src, int location, AlignmentEventType type)
  {
    super(src);  // set the source of the event by calling the parent
    this.start = location;
    this.end = location;
    this.type = type;
  }
  
  /**
   * Constructor to set the event source, location and type. The location
   * is split into a start and end location.
   *
   * @param src the source of the event (i.e: the MSA)
   * @param start the start location of the event
   * @param end the end location of the event
   * @param type the multiple sequence event type
   */
  public AlignmentEvent(Object src, int start, int end, AlignmentEventType type)
  {
    super(src);  // set the source of the event by calling the parent
    this.start = start;
    this.end = end;
    this.type = type;
  }
  
  /**
   * Returns the start location of the event.
   *
   * @return the start
   */
  public int getStart()
  {
    return this.start;
  }
  
  /**
   * Returns the end location of the event.
   *
   * @return the end
   */
  public int getEnd()
  {
    return this.end;
  }
  
  /**
   * Returns the multiple sequence event type.
   *
   * @return the event type
   */
  public AlignmentEventType getType()
  {
    return this.type;
  }
} // AlignmentEvent



/*
 * ChangeLog
 * $Log: AlignmentEvent.java,v $
 * Revision 1.3  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.2  2000/10/31 15:50:28  lord
 * Improved documentation
 *
 * Revision 1.1  2000/03/02 19:22:13  jns
 * initial code.
 *
 *
 */
