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
 * SequenceAlignmentDimension.java
 *
 * Represents a dimension within a SA. It is not a vast amount of use
 * without a point of reference (SequenceAlignmentPoint).
 * @see SequenceAlignmentPoint
 *
 * Created: Mon Mar 27 12:01:25 2000
 *
 * @author J Selley
 * @version $Id: SequenceAlignmentDimension.java,v 1.2 2001/04/11 17:04:43 lord Exp $
 */

public class SequenceAlignmentDimension  {
	
	
  private int width;
  private int height;
  
  public SequenceAlignmentDimension() {
	  
    // generate a dimension of width=1, and height=1
    this.setSize(1, 1);
  }
  
  public SequenceAlignmentDimension(final int w, final int h)  {
	  
    this.setSize(w, h);
  }
  
  public SequenceAlignmentDimension(final SequenceAlignmentDimension dim) {
	  
    this.setSize(dim);
  }
  
  /**
   * Ascertains the equality of a given dimension to this
   * object. Overrides the method in Object.
   *
   * @param obj the dimension
   * @return the equality of the objects
   */
  @Override
  public boolean equals(final Object obj)  {
    /*
     * if the object is an instance of SADimension, and the width and
     * heights are equal, then the objects are said to be equivalent.
     */
	 if (obj == this) {
		 
		 return true;
	 }
	 if (obj == null || ( ! (obj instanceof SequenceAlignmentDimension))) {
		 
		 return false;
	 }
	 SequenceAlignmentDimension other = (SequenceAlignmentDimension) obj;
	 return other.width == this.width && other.height == this.height;
  }
  
  /**
   * Sets the size of the dimension. This method may further be
   * expanded if listeneers are appropriate, and to include any
   * necessary error checking.
   *
   * @param w the width of the dimension
   * @param h the height of the dimension
   */
  public void setSize(int w, int h) 
  {
    this.width = w;
    this.height = h;
  }
  
  /**
   * Sets the size of the dimension. This method will allow a
   * dimension to be duplicated. Calls setSize(int, int) to save on
   * correction procedures.
   *
   * @param dim a value of type 'SequenceAlignmentDimension'
   */
  public void setSize(SequenceAlignmentDimension dim) 
  {
    // calls setDimension(int, int) to enable corrections to the
    // procedure to be made in one place
    setSize(dim.getWidth(), dim.getHeight());
  }
  
  /**
   * Allows the resize of the dimension
   *
   * @param w the new width of the dimension
   * @param h the new height of the dimension
   */
  public void resize(int w, int h) 
  {
    setSize(w, h);
  }
  
  /**
   * Returns the dimensions of this dimension (que????).
   *
   * @return the SA dimension
   */
  public SequenceAlignmentDimension getSize() 
  {
    // returning a new SAD to avoid problems of SAD being altered at a
    // later stage
    return new SequenceAlignmentDimension
      (this.width, this.height);
  }
  
  /**
   * Returns the width of this dimension.
   *
   * @return the width
   */
  public int getWidth() 
  {
    return this.width;
  }
  
  /**
   * Returns the height of this dimension.
   *
   * @return the height
   */
  public int getHeight() 
  {
    return this.height;
  }
} // SequenceAlignmentDimension



/*
 * ChangeLog
 * $Log: SequenceAlignmentDimension.java,v $
 * Revision 1.2  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/03/27 11:27:57  jns
 * o initial coding of a sequence alignment dimension.
 *
 */
