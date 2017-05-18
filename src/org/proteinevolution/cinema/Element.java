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


// TODO Utterly meaningless (why not simply a char for now?)

/**
 * Element.java
 *
 * This interface defines an element within a sequence. Implementations
 * of this will be immutable after definition. The interface is designed
 * to be as thin as possible. Two equal elements should represent the
 * same biological element.
 *
 * Created: Sat Feb 26 18:29:35 2000
 *
 * @author J Selley
 * @version $Id: Element.java,v 1.3 2001/04/11 17:04:43 lord Exp $ 
 */

public interface Element {
  /**
   * Returns a character representation of this element. This may be
   * non-alphanumeric, and should be unique within a SequenceType.
   * @see SequenceType
   *
   * @return the char representation
   */
  public char toChar();
}// Element


/*
 * ChangeLog
 * $Log: Element.java,v $
 * Revision 1.3  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.2  2000/08/01 12:47:05  jns
 * o removed references to BioInterface and BioObject.
 *
 * Revision 1.1  2000/02/26 18:42:26  jns
 * Initial code for Element.
 * 
 */
