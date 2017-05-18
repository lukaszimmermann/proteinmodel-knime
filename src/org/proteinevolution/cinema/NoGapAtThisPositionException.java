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


// TODO Remove this Exception

/**
 * NoGapAtThisPositionException.java
 *
 *
 * Created: Sat Mar  4 14:04:07 2000
 *
 * @author Phillip Lord
 * @version $Id: NoGapAtThisPositionException.java,v 1.3 2001/04/11 17:04:43 lord Exp $
 */

public class NoGapAtThisPositionException extends RuntimeException {
	
  private GappedSequence source;
  private int index;
  
  public NoGapAtThisPositionException( String message ) {
    super( message );
  }

  /**
   * Creates an object signalling an exception for no gap at a
   * particular position. This constructor stores the source and index
   * within the source where the exception was raised.
   * @param message the message to use
   * @param source the gapped sequence or source
   * @param index the index in the source of the raised 
   */
  public NoGapAtThisPositionException(String message, GappedSequence source, int index) 
  {
    super(message);
    this.source = source;
    this.index = index;
  }
  
  public NoGapAtThisPositionException(GappedSequence source, int index) 
  {
    this("No gap was found at " + index + " in sequence " + source,
	 source, index);
  }

  public GappedSequence getSource() 
  {
    return this.source;
  }
  
  public int getIndex() 
  {
    return this.index;
  }
} // NoGapAtThisPositionException



/*
 * ChangeLog
 * $Log: NoGapAtThisPositionException.java,v $
 * Revision 1.3  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.2  2000/11/02 14:56:37  jns
 * o changed creation of NoGapAt...Exception to store information about
 * where the exception was raised and by what.
 *
 * Revision 1.1  2000/03/08 17:26:35  lord
 * To many changes to document
 *
 */
