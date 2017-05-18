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

package org.proteinevolution.cinema;


/**
 * SequenceAlignmentShape.java
 *
 * Defines a shape used to identify a region in a sequence alignment.
 *
 * Created: Mon Mar 27 12:38:04 2000
 *
 * @author J Selley
 * @version $Id: SequenceAlignmentShape.java,v 1.2 2001/04/11 17:04:43 lord Exp $ 
 */

public interface SequenceAlignmentShape  {
  /**
   * Returns the boundaries of the shape used to identify a region in
   * a sequence alignment.
   *
   * @return the rectangle that defines the boundries
   */
  public SequenceAlignmentRectangle getBounds();
}// SequenceAlignmentShape


/*
 * ChangeLog
 * $Log: SequenceAlignmentShape.java,v $
 * Revision 1.2  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/03/27 13:27:07  jns
 * o initial coding of a SA shape which can be used to define a shape in a MSA.
 * 
 */

