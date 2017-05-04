package org.proteinevolution.models.structure;


/**
 *  Objects implementing this interface have coordinates in  3D space
 * @author lzimmermann
 *
 */
public interface Point3D {

	/**
	 * Returns the x-coordinate of this point
	 * 
	 * @return x-coordinate in double precision of this point.
	 */
	public double getX();
	
	/**
	 * Returns the y-coordinate of this point
	 * 
	 * @return y-coordinate in double precision of this point.
	 */
	public double getY();
	
	
	/**
	 * Returns the z-coordinate of this point
	 * 
	 * @return z-coordinate in double precision of this point
	 */
	public double getZ();
}
