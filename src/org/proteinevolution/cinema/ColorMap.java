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

import java.awt.Color;



// TODO This interface is very spurious (various things?)

/**
 * ColorMap.java
 *
 * The interface to support color mapping of elements in a sequence
 * alignment. NB: Objects of this type should be immutable.
 *
 * Created: Thu Mar 23 18:32:32 2000
 *
 * @author J Selley
 * @version $Id: ColorMap.java,v 1.4 2001/04/11 17:04:42 lord Exp $ 
 */

public interface ColorMap  {
  /**
   * Returns the Color at a specific position in the alignment. This
   * method will do various things depending on the actual color map
   * implemented.
   *
   * @param sa the sequence alignment
   * @param elem the element
   * @param point the location within the SA
   * @return the color
   */
  public Color getColorAt(ISequenceAlignment sa, Element elem, SequenceAlignmentPoint point);
  
  /**
   * Returns the name of this color scheme.
   *
   * @return the name
   */
  public String getName();
}// ColorMap


/*
 * ChangeLog
 * $Log: ColorMap.java,v $
 * Revision 1.4  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.3  2000/11/09 16:42:13  lord
 * Package update
 *
 * Revision 1.2  2000/08/01 12:47:05  jns
 * o removed references to BioInterface and BioObject.
 *
 * Revision 1.1  2000/04/11 18:29:06  jns
 * o inial coding for color mapping.
 * 
 */

