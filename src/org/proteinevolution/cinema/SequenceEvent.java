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


// TODO I think this class is useless

/**
 * SequenceEvent.java
 *
 * Instances of this class represent an event to a sequence. It
 * includes reference to the location of the event, as well as
 * its type.
 *
 * Created: Tue Feb 15 19:12:00 2000
 *
 * @author J Selley
 * @version $Id: SequenceEvent.java,v 1.4 2001/04/11 17:04:43 lord Exp $
 */

public class SequenceEvent extends java.util.EventObject {


	private static final long serialVersionUID = -4905869264962800679L;
	
	private int start;  // the start location of the event
	private int length;  // the length of this event
	private SequenceEventType type;  // the type of event (an enum model)

  /**
     * Constructor to set the event source, location and type.
     * @param src the source of the event (i.e: the sequence)
     * @param location the location of the event
     * @param type the sequence event type
     */
  public SequenceEvent(Object src, int location, SequenceEventType type)
  {
    super(src);  // set the source of the event by calling the parent
    this.start = location;
    this.length = 1;
    this.type = type;
  }
  
  /**
     * Constructor to set the event source, location and type, which allows
     * the definition of the start and end of an event.
     * @param src the source of the event (i.e: the sequence)
     * @param start the start location of the event
     * @param end the end location of the event
     * @param type the sequence event type
     */
  public SequenceEvent(Object src, int start, int length, SequenceEventType type)
  {
    super(src);  // set the source of the event by calling the parent
    this.start = start;
    this.length = length;
    this.type = type;
  }
  
  /**
     * Returns the start location of the event.
     * @return the start
     */
  public int getStart()
  {
    return this.start;
  }
  
  /**
     * Returns the end location of the event.
     * @return the end
     */
  public int getLength()
  {
    return this.length;
  }

  /**
     * Returns the seqyence event type.
     * @return the sequence event type
     */
  public SequenceEventType getType()
  {
    return this.type;
  }
  
  public String toString()
  {
    return super.toString() + " of type " + getType() + " @ " + getStart() + " for " + getLength();
  }
} // SequenceEvent



/*
 * ChangeLog
 * $Log: SequenceEvent.java,v $
 * Revision 1.4  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.3  2000/03/08 17:26:35  lord
 * To many changes to document
 *
 * Revision 1.2  2000/03/01 14:32:27  lord
 * cosmetic
 *
 * Revision 1.1  2000/02/26 18:28:44  jns
 * Initial coding of file
 *
 *
 */
