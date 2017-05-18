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



/**
 * Sequence.java
 *
 * This interface is designed to model a biological sequence. It is
 * non-specific in the manor in which a sequence may be implemented.
 * The default implementation of this sequence will be imutable, and
 * all sequence numbering will start at 1 <b>NOT</b> 0. This may be
 * a set of rules worth following. For a mutable sequence, see the
 * interface MutableSequence.
 * @see MutableSequence
 *
 * Created: Fri Feb 25 19:21:55 2000
 *
 * @author J Selley
 * @version $Id: Sequence.java,v 1.5 2001/04/11 17:04:43 lord Exp $ 
 */

public interface Sequence extends Identifiable
{
  /**
   * Returns a sub-sequence from the current sequence.
   *
   * @param from the initial index of the sub-sequence
   * @param to the 'to' index of the sub-sequence
   * @return the sub-sequence
   */
  public Sequence getSubSequence(int from, int length);

  /**
   * Returns the sequence as an array of characters.
   *
   * @return the sequence
   */
  public char[] getSequenceAsChars();

  /**
   * Returns the sequence as an array of elements.
   *
   * @return the sequence
   */
  public Element[] getSequenceAsElements();

  /**
   * Returns the character of an target element.
   *
   * @param index the location of the target element
   * @return the element character
   */
  public char getElementAtAsChar(int index);

  /**
   * Returns an element at a given location.
   *
   * @param index the location of the target element
   * @return the element
   */
  public Element getElementAt(int index);

  /**
   * Returns the type of sequence.
   *
   * @return the sequence type
   */
  public SequenceType getSequenceType();

  /**
   * Returns the length of the sequence.
   *
   * @return the length of the sequence
   */
  public int getLength();
}// Sequence


/*
 * ChangeLog
 * $Log: Sequence.java,v $
 * Revision 1.5  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.4  2000/07/18 11:13:08  lord
 * Import rationalisation
 * Changes due to BioInterface removal
 *
 * Revision 1.3  2000/03/08 17:26:35  lord
 * To many changes to document
 *
 * Revision 1.2  2000/03/01 20:17:41  lord
 * Updated for new interfaces
 *
 * Revision 1.1.1.1  2000/02/26 16:51:32  jns
 * The repository for the bioinf java classes
 * 
 */

