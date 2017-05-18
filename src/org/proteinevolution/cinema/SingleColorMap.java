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

import org.proteinevolution.models.interfaces.ISequenceAlignment;


/**
 * SingleColorMap.java
 *
 * This class is designed to return one color, regardless of the
 * element being displayed. Thus, this is able to provide no color
 * scheme, by being set to the background color at run time.
 *
 * Created: Tue Apr 11 17:42:19 2000
 *
 * @author J Selley
 * @version $Id: SingleColorMap.java,v 1.4 2001/04/11 17:04:42 lord Exp $
 */

public class SingleColorMap implements ColorMap
{
  private String name;
  private Color color;

  public SingleColorMap( Color color )
  {
    this( color, "SingleColorMap" );
  }
  
  public SingleColorMap( Color color, String name )
  {
    this.name = name;
    this.color = color;
  }
  
  public Color getColorAt
    (ISequenceAlignment sa, Element element, SequenceAlignmentPoint point) 
  {
    return this.color;
  }
  
  public String getName() 
  {
    return this.name;
  }
} // SingleColorMap



/*
 * ChangeLog
 * $Log: SingleColorMap.java,v $
 * Revision 1.4  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.3  2001/02/19 17:40:32  lord
 * Now takes a colour in the constructor
 *
 * Revision 1.2  2000/08/01 12:47:05  jns
 * o removed references to BioInterface and BioObject.
 *
 * Revision 1.1  2000/04/11 18:30:34  jns
 * o initial coding for color mapping.
 *
 */
