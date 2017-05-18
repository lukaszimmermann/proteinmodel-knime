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
 * SequenceType.java
 *
 * An interface to represent the type of a sequence (e.g: Protein, DNA).
 *
 * Created: Fri Feb 25 20:12:45 2000
 *
 * @author J Selley
 * @version $Id: SequenceType.java,v 1.7 2001/05/08 17:47:31 lord Exp $ 
 */

public interface SequenceType {
  /**
   * Returns the number of elements in this sequence type.
   * @return the number of elements
   */
  public int size();
  
  /**
   * This method is present as a performance enhancement. For every
   * Element this method should return a unique and repeatable int,
   * starting at 0 and ending at size() - 1 for all possible
   * Elements. This enables the Element to be used as an index for an
   * array which in turn allows implementation of efficient look up
   * tables, rather than using a hashtable. 
   *
   * @param element the element
   * @return a int
   */
  public int getIntForElement( Element element );
  
  /**
   * The inverse of the method getIntForElement. 
   * @param index a value of type 'int'
   * @return a value of type 'Element'
   */
  public Element getElementForInt( int index );

  /**
   * Returns all the elements available in this sequence type.
   * This array should be freely modifiable, and changes should not
   * percolate backwards iunto the internal data of the sequence
   * type.
   *
   * @return the elements
   */
  public Element[] getElements();
 
 /**
   * Returns the human readable form of this sequence type.
   *
   * @return the name
   */
  public String getName();

  /**
   * Returns a boolean as to whether the specified element is part
   * of this sequence type.
   *
   * @param element the element
   * @return whether an element
   */
  public boolean isElement(char element);

  /**
   * Returns a boolean as to whether the specified element is part
   * of this sequence type.
   *
   * @param element the element
   * @return whether an element
   */
  public boolean isElement(Element element);

  /**
   * Returns a boolean as to whether the specified elements are part
   * of this sequence type.
   *
   * @param elements the elements
   * @return whether elements
   */
  public boolean isElement(char[] elements);

  /**
   * Returns a boolean as to whether the specified elements are part
   * of this sequence type.
   *
   * @param elements the elements
   * @return whether elements
   */
  public boolean isElement(Element[] elements);
}// SequenceType


/*
 * ChangeLog
 * $Log: SequenceType.java,v $
 * Revision 1.7  2001/05/08 17:47:31  lord
 * Cosmetic changes
 *
 * Revision 1.6  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.5  2000/10/03 17:14:20  lord
 * Added documentation
 *
 * Revision 1.4  2000/08/01 12:47:05  jns
 * o removed references to BioInterface and BioObject.
 *
 * Revision 1.3  2000/06/27 16:05:09  lord
 * Added getIntForElement and getElementForInt methods
 *
 * Revision 1.2  2000/06/13 11:00:25  lord
 * Added size method
 *
 * Revision 1.1  2000/02/26 16:51:33  jns
 * Initial revision
 *
 * Revision 1.1  2000/02/26 16:51:33  jns
 * Initial revision
 * 
 */

