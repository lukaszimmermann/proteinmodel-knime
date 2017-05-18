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

package uk.ac.man.bioinf.sequence.event; // Package name inserted by JPack

import uk.ac.man.bioinf.sequence.event.SequenceListener;
import uk.ac.man.bioinf.sequence.event.VetoableSequenceListener;


/**
 * SequenceEventProvider.java
 *
 * An interface designed to enforce the methods necessary to listen to a
 * sequence.
 *
 * Created: Thu Mar  2 18:22:26 2000
 *
 * @author J Selley
 * @version $Id: SequenceEventProvider.java,v 1.2 2001/04/11 17:04:43 lord Exp $ 
 */

public interface SequenceEventProvider 
{
  /**
   * Adds the specified sequence listener.
   *
   * @param listener the sequence listener
   */
  public void addSequenceListener(SequenceListener listener);

  /**
   * Removes the specified sequence listener.
   *
   * @param listener the sequence listener
   */
  public void removeSequenceListener(SequenceListener listener);

  /**
   * Adds the specified vetoable sequence listener.
   *
   * @param listener the vetoable sequence listener
   */
  public void addVetoableSequenceListener(VetoableSequenceListener listener);

  /**
   * Removes the specified vetoable sequence listener.
   *
   * @param listener the vetoable sequence listener
   */
  public void removeVetoableSequenceListener(VetoableSequenceListener listener);

}// SequenceEventProvider


/*
 * ChangeLog
 * $Log: SequenceEventProvider.java,v $
 * Revision 1.2  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/03/02 18:34:43  jns
 * initial code. Generated from the code of MutableSequence, which has now
 * become a marker class.
 * 
 */

