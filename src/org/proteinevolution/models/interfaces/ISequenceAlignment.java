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


package org.proteinevolution.models.interfaces; // Package name inserted by JPack


/**
 * SequenceAlignment.java
 *
 * This interface is designed to mimic a biological sequence alignment.
 * As a consequence, the sequences contained within should be
 * GappedSequence.
 * @see GappedSequence
 *
 *
 * Created: Tue Feb 15 16:24:23 2000
 *
 * @author J Selley
 * @version $Id: SequenceAlignment.java,v 1.17 2001/04/11 17:04:43 lord Exp $
 */

public interface ISequenceAlignment {


  /**
   * Provides the sequence at a particular location in the alignment.
   * The indexing STARTS AT 1, and not 0. 
   * @param index the index of the target sequence
   * @return      the sequence of interest
   * @throws      if index < 1 or index > getNumberSequences()
   */
  public char[] getSequenceAt(final int index);
  
  /**
   * Returns the total number of sequences in the alignment.
   *
   * @return the number of sequences
   */
  public int getNumberSequences();

  /**
   * Returns the length of the alignment (i.e: the longest sequence).
   *
   * @return length of alignment
   */
  public int getLength();
  
  
  /**
   * Returns in the ith sequence the character at position j
   * 
   * @param i Sequence to look at
   * @param j Column to look at
   * @return
   */
  public char get(final int i, final int j);  
  
  
  /**
   * Returns the complete alignment, but does not make a copy.
   * 
   * @return
   */
  public char[][] getAllUnsafe();
  
  
  
} // SequenceAlignment



/*
 * ChangeLog
 * $Log: SequenceAlignment.java,v $
 * Revision 1.17  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.16  2001/01/23 17:58:57  lord
 * Added getSubAlignment( SequenceAlignmentRectangle ) method because I
 * thought it would be useful.
 *
 * Revision 1.15  2001/01/04 15:05:11  lord
 * getSubAlignment method differed from that of getSubAlignment in
 * DefaultSequenceAlignment (it used start, stop rather than start,
 * length). It was easier to change the interface, rather than the
 * implementation.
 *
 * Revision 1.14  2000/12/05 15:54:37  lord
 * Import rationalisation
 *
 * Revision 1.13  2000/11/27 18:17:52  lord
 * Have removed Identifier methods, now extends Identifiable
 *
 * Revision 1.12  2000/10/26 12:42:49  jns
 * o added editing facilities to SA - this includes insertion/deletion of gaps,
 * addition/removal of sequences from an alignment. It involved resolving some
 * conflicts with the group stuff.
 *
 * Revision 1.11  2000/09/11 13:18:13  lord
 * Added identifier support
 *
 * Revision 1.10  2000/08/01 12:47:05  jns
 * o removed references to BioInterface and BioObject.
 *
 * Revision 1.9  2000/06/13 11:07:00  lord
 * Improved documentation
 *
 * Revision 1.8  2000/06/05 14:45:58  lord
 * Made documentation more explicit
 *
 * Revision 1.7  2000/05/30 16:04:18  lord
 * Have rationalised and sorted all the import statements
 *
 * Revision 1.6  2000/05/08 17:06:43  lord
 * Event methods now removed to secondary interface
 *
 * Revision 1.5  2000/04/19 17:15:17  lord
 * Removed extraneous abstract
 *
 * Revision 1.4  2000/03/20 16:47:21  lord
 * Moved getInsert upto SequenceAlignment class
 *
 * Revision 1.3  2000/03/10 17:54:18  lord
 * Now implements SequenceEventProvider
 *
 * Revision 1.2  2000/03/10 12:34:01  jns
 * unknown changes.
 * Sequence -> Gapped Sequence I think.
 *
 * Revision 1.1  2000/03/02 19:21:52  jns
 * initial code.
 *
 *
 */
