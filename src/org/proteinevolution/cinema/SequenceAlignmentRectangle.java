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
 * SequenceAlignmentRectangle.java
 *
 * Defines a rectangular region in a sequence alignment. NB: objects
 * of this class perform no checks as to the validity of the region
 * defined. 
 *
 * Created: Mon Mar 27 12:41:18 2000
 *
 * @author J Selley
 * @version $Id: SequenceAlignmentRectangle.java,v 1.6 2001/04/11 17:04:43 lord Exp $
 */

public class SequenceAlignmentRectangle implements SequenceAlignmentShape, Comparable<SequenceAlignmentRectangle> {

	// TODO Inline class SequenceAlignmentPoint here 
	private SequenceAlignmentPoint location;
	
	// TODO Inline class SequenceAlignmentDimension here
	private SequenceAlignmentDimension size;

	public SequenceAlignmentRectangle() {
		this( 1, 1, 1, 1 );
	}

	public SequenceAlignmentRectangle(final int x, final int y, final int width, final int height) {

		this.setBounds(x, y, width, height);
	}

	public SequenceAlignmentRectangle(final SequenceAlignmentRectangle rect) {
		this.setBounds(rect);
	}

	public SequenceAlignmentRectangle(final int x, final int y, final SequenceAlignmentDimension dim) {

		this.setBounds(x, y, dim.getWidth(), dim.getHeight());
	}

	public SequenceAlignmentRectangle(final SequenceAlignmentPoint point, int width, int height) {

		this.setBounds(point.getX(), point.getY(), width, height);
	}

	/**
	 * Determines whether a point is contained in this region.
	 *
	 * @param x the position in the sequence
	 * @param y the sequence index in the alignment
	 * @return whether a point is contained in this region
	 */
	public boolean contains(final int x, final int y) {
		/*
		 * if x and y are greater than the corresponding location to this
		 * rectangle, but less than the corresponding location +
		 * dimension, then the point is contained within this rectangle.
		 * The - 1's are because a SARhas a size of at least 1
		 */
		return (((x >= this.location.getX()) && (y >= this.location.getY())) &&
				((x <= (this.location.getX() + this.size.getWidth() - 1 )) &&
						(y <= (this.location.getY() + this.size.getHeight() - 1 ))));
	}

	/**
	 * Determines whether a point is contained in this region.
	 *
	 * @param point the point
	 * @return whether a point is contained in this region
	 */
	public boolean contains(final SequenceAlignmentPoint point) {
		/*
		 * Delegates to the method contains(int, int) to allow easy update
		 * of the procedure
		 */
		return this.contains(point.getX(), point.getY());
	}

	/**
	 * Compares two SARectangles for equality (ie: whether they have the
	 * same dimension and location).
	 *
	 * @param obj the SARectangle for comparison
	 * @return the equality of the objects
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			
			return true;
		}
		if ( obj == null || ( ! (obj instanceof SequenceAlignmentRectangle) )) {
			
			return false;
		}
		
		/*
		 * if the object is an instance of SARectangle, and the point and
		 * dimensions are equal, then the objects are said to be
		 * equivelent.
		 */
		SequenceAlignmentRectangle other = (SequenceAlignmentRectangle) obj;
		return other.getLocation().equals(this.location) && other.getSize().equals(this.size);
	}


	/**
	 * Sets the boundry defined by this region.
	 *
	 * @param x the position in the sequence
	 * @param y the sequence index in the alignment
	 * @param width the width of the SARectangle
	 * @param height the height of the SARectangle
	 */
	public void setBounds(final int x, final int y, final int width, final int height)  {
		
		this.location = new SequenceAlignmentPoint(x, y);
		this.size = new SequenceAlignmentDimension(width, height);
	}

	/**
	 * Sets the boundary defined by this region.
	 *
	 * @param point the origin location of the new SARectangle
	 * @param dim the size of the new SARectangle
	 */
	public void setBounds(final SequenceAlignmentPoint point, final SequenceAlignmentDimension dim) {
		/*
		 * Delegates action to setBounds(int, int, int, int) to enable
		 * easy update of procedures
		 */
		this.setBounds(point.getX(), point.getY(), dim.getWidth(), dim.getHeight());
	}

	/**
	 * Sets the boundary defined by this region.
	 *
	 * @param rect the rectangle
	 */
	public void setBounds(final SequenceAlignmentRectangle rect) {
		/*
		 * Delegates action to setBounds(int, int, int, int) to enable
		 * easy update of procedures
		 */
		this.setBounds(rect.getLocation().getX(), rect.getLocation().getY(), 
				rect.getSize().getWidth(), rect.getSize().getHeight());
	}

	/**
	 * Returns the rectangle which defines the boundries of the region
	 * defined by this object.
	 * @see SequenceAlignmentShape
	 *
	 * @return the rectangle
	 */
	public SequenceAlignmentRectangle getBounds() {
		
		// from SequenceAlignmentShape
		return new SequenceAlignmentRectangle(this);
	}

	/**
	 * Sets the progom location of the rectangle. X corresponds to the
	 * position in the sequence, and Y corresponds to the index of the
	 * sequence in the alignment.
	 *
	 * @param x the new X location of the rectangle
	 * @param y the new Y location of the rectangle
	 */
	public void setLocation(final int x, final int y)  {
		
		this.location = new SequenceAlignmentPoint(x, y);
	}

	/**
	 * Sets the origin location of the rectangle.
	 *
	 * @param point the new location
	 */
	public void setLocation(final SequenceAlignmentPoint point) {
		
		/*
		 * Delegates action to setLocation(int, int) so that if
		 * modifications to code are necessary, they can be made in one
		 * place.
		 */
		this.setLocation(point.getX(), point.getY());
	}

	/**
	 * Returns the location of the rectangle
	 *
	 * @return the location
	 */
	public SequenceAlignmentPoint getLocation()  {
		return new SequenceAlignmentPoint(this.location);
	}

	/**
	 * Sets the size of the rectangle.
	 *
	 * @param width the width of the new rectangle
	 * @param height the height of the new rectangle
	 */
	public void setSize(final int width, final int height)  {
		this.size = new SequenceAlignmentDimension(width, height);
	}

	/**
	 * Sets the size of the rectangle.
	 *
	 * @param dim the new dimension
	 */
	public void setSize(final SequenceAlignmentDimension dim) {
		/*
		 * Delegates action to setSize(int, int) so that if modifications
		 * to code are necessary, they can be made in one place.
		 */
		this.setSize(dim.getWidth(), dim.getHeight());
	}

	/**
	 * Returns the size of the rectangle.
	 *
	 * @return the dimensions of the rectangle
	 */
	public SequenceAlignmentDimension getSize() {

		return new SequenceAlignmentDimension(this.size);
	}

	public int getX() {

		return this.location.getX();
	}

	public int getY() {

		return this.location.getY();
	}

	public int getHeight() {

		return this.size.getHeight();
	}

	public int getWidth() {

		return this.size.getWidth();
	}

	public void add( SequenceAlignmentPoint point ) {

		this.add( point.getX(), point.getY() );
	}

	public void add( final int newx, final int newy ) {
		// this code is culled from java.awt.Rectangle
		// (PENDING:- PL) Does this not suggest that all of the geom
		// classes should be re-written to simply wrap the awt geom
		// classes? Probably.
		int x = location.getX();
		int y = location.getY();
		int width  = size.getWidth();
		int height = size.getHeight();

		// the some what counter intuitive + 1's here are because the
		// minimum size of a Rectangle is 1, 1...
		int x1 = Math.min( x, newx );
		int x2 = Math.max( x + width, newx + 1);
		int y1 = Math.min( y, newy );
		int y2 = Math.max( y + height, newy + 1 );

		this.setLocation( x1, y1 );
		this.setSize( (x2 - x1), (y2 - y1) );
	}


	@Override
	public String toString()
	{
		return super.toString() + " Location [" + location.getX() + "x" + location.getY() +
				"+" + size.getWidth() + "+" + size.getHeight() + "]";
	}

	/**
	 * Compares another rectangle to this one. Comparison is done by
	 * comparing the location point. If this is equal then the width,
	 * then the height are compared. Throws a class cast if obj is
	 * not a SequenceAlignmentRectangle
	 */
	@Override
	public int compareTo(final SequenceAlignmentRectangle rect) {

		// get rid of the equals case first. 
		if( this.equals( rect ) ) {

			return 0;
		}

		// now compare locations
		int comp = location.compareTo( rect.location );

		if( comp != 0 ) {
			return comp;
		}
		// so locations are the same, compare width, and then height
		if(    size.getWidth() <  rect.size.getWidth() 
				|| size.getHeight() < rect.size.getHeight() ){
			return -1;
		} 
		return 1;
	}
}



/*
 * ChangeLog
 * $Log: SequenceAlignmentRectangle.java,v $
 * Revision 1.6  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.5  2001/01/23 17:58:00  lord
 * Support for comparable interface
 *
 * Revision 1.4  2001/01/19 19:50:23  lord
 * This class now uses exclusive numbers. This makes it behave more
 * intuitively. At least in some circumstances. In others it makes it
 * less so.
 *
 * Revision 1.3  2000/06/13 11:03:24  lord
 * Added getX, getY methods
 *
 * Revision 1.2  2000/04/13 15:37:19  lord
 * Added "add" method. Fiddled with "contains" method
 *
 * Revision 1.1  2000/03/27 13:29:48  jns
 * o initial coding of a SA rectangle which may be used to define a region in
 * a MSA.
 *
 */


