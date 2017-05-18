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
 * SequenceAlignmentPoint.java
 *
 * Represents a point in an alignment, where X is the position in a
 * sequence, and Y is the index of the sequence in the alignment. No 
 * checking is currently written into this object for the validity of 
 * the point, and the opportunity to use listeners is not seized 
 * (although this can easily be added later).
 *
 * Created: Mon Mar 27 11:16:19 2000
 *
 * @author J Selley
 * @version $Id: SequenceAlignmentPoint.java,v 1.9 2001/04/11 17:04:43 lord Exp $
 */

public class SequenceAlignmentPoint implements Cloneable, Comparable<SequenceAlignmentPoint> {
	private int x;
	private int y;

	public SequenceAlignmentPoint() {
		// create a point at (1,1)
		this.setLocation(1, 1);
	}

	public SequenceAlignmentPoint(final int x, final int y) {

		this.setLocation(x, y);
	}

	public SequenceAlignmentPoint(final SequenceAlignmentPoint point) {
		setLocation(point);
	}

	/**
	 * Ascertains the equality of a given point to this
	 * object. Overrides the method in Object.
	 *
	 * @param obj the point
	 * @return the equality of the objects
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == this) {

			return true;
		}

		if (obj == null || ( ! (obj instanceof SequenceAlignmentPoint)  )) {

			return false;
		}

		/*
		 * if the object is an instance of SAPoint, and the coordinates
		 * are equal, the	n the objects are equivelent; otherwise they are
		 * considered not to be equal.
		 */
		SequenceAlignmentPoint other = (SequenceAlignmentPoint) obj;
		return other.x == this.x && other.y == this.y;
	}


	public void setX( final int x ) {
		
		this.x = x;
	}

	public void setY( final int y ) {
		
		this.y = y;
	}

	/**
	 * Sets the location of the point. This method can be further
	 * expanded if listeners are appropriate, and to include error
	 * checking.
	 *
	 * @param x the x position of the point
	 * @param y the y position of the point
	 */
	public void setLocation(final int x, final int y) {
		
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the location of the point. This method will allow a point to
	 * be duplicated. Calls setLocation(int, int) to save on correction
	 * procedures.
	 *
	 * @param point the point in a sequence alignment
	 */
	public void setLocation(final SequenceAlignmentPoint point)  {

		// calls setLocation(int, int) to enable corrections to the
		// procedure to be made in one place
		this.setLocation(point.getX(), point.getY());
	}

	/**
	 * Allows the moving of a point to a new position.
	 *
	 * @param x the X index
	 * @param y the Y index
	 */
	public SequenceAlignmentPoint move(final int x,  final int y) {
		this.setLocation(x, y);
		return this;
	}

	/**
	 * Returns the location in the sequence alignment, represented by
	 * this object.
	 *
	 * @return the location represented by this point
	 */
	public SequenceAlignmentPoint getLocation() {
		// returns a new SAP to avoid problems with the SAP altering at a
		// later date
		return new SequenceAlignmentPoint(this.x, this.y);
	}

	/**
	 * Returns the X index of the point.
	 *
	 * @return the X index
	 */
	public int getX() {

		return this.x;
	}

	/**
	 * Returns the Y index of the point.
	 *
	 * @return the Y index
	 */
	public int getY() {
		
		return this.y;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		return super.clone();
	}

	@Override
	public String toString() {

		return "SequenceAlignmentPoint at ( " + x + ", " + y + " )";
	}

	@Override
	public int compareTo(final SequenceAlignmentPoint point) {

		// compare X
		if( x > point.x ){
			return 1;
		}
		else if ( x < point.x ){
			return -1;
		}
		// so x is equal. Now compare y
		else if ( y > point.y ){
			return 1;
		}
		else if( y < point.y ){
			return -1;
		}

		// we are equal
		return 0;
	}
} // SequenceAlignmentPoint




/*
 * ChangeLog
 * $Log: SequenceAlignmentPoint.java,v $
 * Revision 1.9  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.8  2001/01/23 17:58:00  lord
 * Support for comparable interface
 *
 * Revision 1.7  2001/01/19 19:50:37  lord
 * Some new accessor methods
 *
 * Revision 1.6  2000/04/06 15:45:32  lord
 * A few changes to speed up the equals method
 *
 * Revision 1.5  2000/04/05 14:26:34  lord
 * Added toString, and clone
 *
 * Revision 1.4  2000/04/04 17:48:03  lord
 * Now clonable
 *
 * Revision 1.3  2000/03/29 15:39:53  lord
 * Moved default location to 1,1 rather than 0,0 as the latter
 * isnt valid!
 *
 * Revision 1.2  2000/03/27 11:19:38  jns
 * altering getLocation() to make thread safe.
 *
 * Revision 1.1  2000/03/27 10:57:35  jns
 * o initial coding of a SA point [based on a java.awt.point].
 *
 */



