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

package uk.ac.man.bioinf.sequence.event; // Package name inserted by JPack
import uk.ac.man.bioinf.util.AbstractEnumeration;


/**
 * SequenceEventType.java
 *
 * This class provides an enumerated type for the SequenceEvent class
 * to pass around as an identifier
 *
 * Created: Tue Feb 15 20:10:48 2000
 *
 * @author Phillip Lord
 * @version $Id: SequenceEventType.java,v 1.4 2001/04/11 17:04:43 lord Exp $
 */

public final class SequenceEventType extends AbstractEnumeration
{
  private SequenceEventType( String toString )
  {
    super( toString );
  }
  
  //EventTypes
  /**
   * Indicates a insertion event
   */
  public static final SequenceEventType INSERT = new SequenceEventType( "Insertion" );
  public static final SequenceEventType DELETE = new SequenceEventType( "Deletion" );
  public static final SequenceEventType SET = new SequenceEventType( "Set" );
  public static final SequenceEventType GAPINSERT = new SequenceEventType( "GapInsert" );
  public static final SequenceEventType GAPDELETE = new SequenceEventType( "GapDelete" );
  public static final SequenceEventType UNSPECIFIED = new SequenceEventType( "Unspecified change" );
} // SequenceEventType



/*
 * ChangeLog
 * $Log: SequenceEventType.java,v $
 * Revision 1.4  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.3  2000/06/27 16:08:44  lord
 * Added generic "Unspecified" category of event
 *
 * Revision 1.2  2000/03/08 17:26:36  lord
 * To many changes to document
 *
 * Revision 1.1  2000/02/26 18:28:44  jns
 * Initial coding of file
 *
 *
 */
