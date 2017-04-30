package org.proteinevolution.models.structure;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import org.proteinevolution.models.spec.pdb.Element;

/*
 *  Class which is used to calculate an orthogonal grid of a 3D structure by adding atoms successively
 *   
 *  @author: lzimmermann
 */
public class Grid implements Serializable {

	private static final long serialVersionUID = 6931511384915737370L;
	private final double x_min;
	private final double y_min;
	private final double z_min;

	// TODO Might be parameterized
	private static final double solvent_buffer = 0.7;
	private static final double spacing = 1;
	private static final int margin = 2;

	// the actual grid
	private int[][][] grid;

	public Grid(final double lower_x, 
			final double lower_y, 
			final double lower_z, 
			final double upper_x, 
			final double upper_y, 
			final double upper_z) {

		this.grid = new int[(int) (Math.ceil(upper_x - lower_x) + 2 * margin * spacing)]
				[(int) (Math.ceil(upper_y - lower_y) + 2 * margin * spacing)]
						[(int) (Math.ceil(upper_z - lower_z) + 2 * margin * spacing)];
		this.x_min = lower_x;
		this.y_min = lower_y;
		this.z_min = lower_z;
	}

	/**
	 * Sets the cell in the grid having the provided coordinates to true
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 */
	private void flagCell(final double x, final double y, final double z) {

		this.grid[(int) Math.floor(x - x_min) + margin]
				 [(int) Math.floor(y - y_min) + margin]
				 [(int) Math.floor(z - z_min) + margin]++;
	}
		
	public void addAtom(final double x, final double y, final double z, final Element element) {

		// Set grid cells occupied by that atom to true
		double radius = element.vdWRadius + solvent_buffer;

		for (double x_iter = 0; x_iter <= radius; x_iter += spacing) {

			// Radius of the x slice
			double x_radius = Math.sin(Math.acos(x_iter/radius)) * radius;

			for (double y_iter = 0; y_iter <= x_radius; y_iter += spacing) {

				double y_radius = Math.sin(Math.acos(y_iter/x_radius)) * x_radius;

				for (double z_iter = 0; z_iter <= y_radius; z_iter += spacing) {
										
					// points to update
					double coord_x_1 = x + x_iter;
					double coord_x_2 = x - x_iter;
					double coord_y_1 = y + y_iter;
					double coord_y_2 = y - y_iter;
					double coord_z_1 = z + z_iter;
					double coord_z_2 = z - z_iter;
					
					this.flagCell(coord_x_1, coord_y_1, coord_z_1);
					this.flagCell(coord_x_1, coord_y_1, coord_z_2);
					this.flagCell(coord_x_1, coord_y_2, coord_z_1);
					this.flagCell(coord_x_1, coord_y_2, coord_z_2);
					this.flagCell(coord_x_2, coord_y_1, coord_z_1);
					this.flagCell(coord_x_2, coord_y_1, coord_z_2);
					this.flagCell(coord_x_2, coord_y_2, coord_z_1);
					this.flagCell(coord_x_2, coord_y_2, coord_z_2);
				}
			}	
		}
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o == null || ! (o instanceof Grid)) {
			
			return false;
		}
		Grid grid = (Grid) o;

		
		// TODO Implement me (Compare grid settings and the minimum values)
		return false;
	}	
	
	@Override
	public int hashCode() {
		
		return Objects.hash(Arrays.deepHashCode(this.grid), this.x_min, this.y_min, this.z_min);
	}
}
