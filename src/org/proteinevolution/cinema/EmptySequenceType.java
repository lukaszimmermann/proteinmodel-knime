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
 * EmptySequenceType.java
 *
 *
 * Created: Tue Aug  1 17:22:00 2000
 *
 * @author Phillip Lord
 * @version $Id: EmptySequenceType.java,v 1.2 2001/04/11 17:04:43 lord Exp $
 */

public class EmptySequenceType implements SequenceType
{
  public static final EmptySequenceType INSTANCE = new EmptySequenceType();
  
  private  EmptySequenceType()
  {
  }

  public static SequenceType getInstance()
  {
    return INSTANCE;
  }
  
  public int size()
  {
    return 0;
  }
  
  public int getIntForElement( Element element )
  {
    throw new IllegalArgumentException( "EmptySequenceType has no elements to convert to an int" );
  }
  
  
  public Element getElementForInt( int index )
  {
    return null;
  }
  
  public Element[] getElements()
  {
    return new Element[ 0 ];
  }
  
  public String getName()
  {
    return "EmptySequenceType";
  }
  
  public boolean isElement(char element)
  {
    return false;
  }

  public boolean isElement(Element element)
  {
    return false;
  }

  public boolean isElement(char[] elements)
  {
    return false;
  }

  public boolean isElement(Element[] elements)
  {
    return false;
  }
} // EmptySequenceType



/*
 * ChangeLog
 * $Log: EmptySequenceType.java,v $
 * Revision 1.2  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/08/01 17:18:53  lord
 * Intial checkin
 *
 */
