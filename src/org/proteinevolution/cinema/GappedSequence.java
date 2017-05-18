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
 * GappedSequence.java
 *
 *
 * Created: Wed Mar  1 18:28:44 2000
 *
 * @author Phillip Lord
 * @version $Id: GappedSequence.java,v 1.5 2001/04/11 17:04:43 lord Exp $ 
 */

public interface GappedSequence extends MutableSequence
{
  public GappedSequence getGappedSubSequence( int from, int length );
  
  public Element getGappedElementAt( int position );
  
  public char getGappedElementAtAsChar( int position );

  public Element[] getGappedSequenceAsElements();
  
  public char[] getGappedSequenceAsChars();

  /**
   * Returns the gapped length of this sequence. That is the length of
   * the sequence including its gaps. Conversely this means that the
   * getLength method of the Sequence interface returns the length of
   * the sequence ignoring the gaps.
   * @return the length
   */
  public int getGappedLength();
  
  /**
   * Translate between the gapped index, and the ungapped index
   * @param index the ungapped position
   * @return the gapped position of the equivalent element
   */
  public int getGappedPositionOf( int index );
  
  /**
   * Translate between the gapped position and the ungapped
   * position. I havent quite decided what to do at the moment if the
   * index refers to a gap. One idea is to throw a
   * NoSuchSequenceElementException which makes a certain amount of
   * sense but isnt really that useful. Perhaps a better idea is to
   * run -(insertion-point) where the insertion point is the nearest
   * element before the current requested position which is not a
   * gap. That way a value less than 0 would indicate a gap and would
   * also return a useful value
   * @param index
   * @return
   */
  public int getUngappedPositionOf( int index );
  
  public void insertGapAt( int index ) throws NoSuchSequenceElementException, SequenceVetoException;
  
  /**
   * Inserts the elements at this position. If the index is one longer
   * than the length of the sequence the sequence will be extended.
   * @param element the elements to insert
   * @param index the index at which to insert
   * @throws NoSuchSequenceElementException if the element does not
   * exist, which will be the case if index is less than 1 or greater
   * than sequence length + 1
   */
  public void insertGapAt( int index, int length ) throws NoSuchSequenceElementException, SequenceVetoException;
  
  /**
   * Delete the element at index. 
   * @param index the index to delete
   * @return the element which has just been deleted
   * @exception NoSuchSequenceElementException if index is less than 1
   * or greater than the length of the sequence
   */
  public void deleteGapAt( int index ) throws NoGapAtThisPositionException, NoSuchSequenceElementException,
  SequenceVetoException;

  /**
   * Delete the elements starting at index for the specified length
   * @param index the index to start at
   * @param length the length to delete
   * @return the elements just deleted
   * @throws NoSuchSequenceElementException if the elements do not all
   * exist, so if index is less than 1, or index + length is greater
   * than the length of the sequence
   */
  public void deleteGapAt( int index, int length ) throws NoGapAtThisPositionException,
  NoSuchSequenceElementException, SequenceVetoException;

}// GappedSequence


/*
 * ChangeLog
 * $Log: GappedSequence.java,v $
 * Revision 1.5  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.4  2001/01/04 15:05:51  lord
 * Changed getGappedSubAlignment to start, length, rather than start, stop.
 *
 * Revision 1.3  2000/06/13 11:07:12  lord
 * Improved documentation
 *
 * Revision 1.2  2000/03/08 17:24:34  lord
 * *** empty log message ***
 *
 * Revision 1.1  2000/03/01 20:17:14  lord
 * Initial checkin
 *
 * Revision 1.1  2000/03/01 20:14:06  lord
 * Initial revision
 * 
 */

