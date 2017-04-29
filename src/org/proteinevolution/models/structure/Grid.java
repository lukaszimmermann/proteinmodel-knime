package org.proteinevolution.models.structure;



/*
 *  Class which is used to calculate an orthogonal grid of a 3D structure by adding atoms successively
 *  
 *  The grid is assumed to be triclinic, so it can be represented by two points (lower, upper)
 *  
 *  @author: lzimmermann
 */
public class Grid {

	
	// lower
	private double lower_x;
	private double lower_y;
	private double lower_z;

	// Upper
	private double upper_x;
	private double upper_y;
	private double upper_z;

	
	public Grid(double lower_x, double lower_y, double lower_z, double upper_x, double upper_y, double upper_z) {
		
		// lower end
		this.lower_x = lower_x;
		this.lower_y = lower_y;
		this.lower_z = lower_z;
		
		// upper end
		this.upper_x = upper_x;
		this.upper_y = upper_y;
		this.upper_z = upper_z;
	}
}
