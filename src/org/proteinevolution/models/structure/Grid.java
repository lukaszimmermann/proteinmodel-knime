package org.proteinevolution.models.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.knime.core.node.NodeLogger;
import org.proteinevolution.models.spec.pdb.Element;

/*
 *  Class which is used to calculate an orthogonal grid of a 3D structure by adding atoms successively
 *   
 *  @author: lzimmermann
 */
public class Grid implements Serializable {
	
	// the logger instance
	private static final NodeLogger logger = NodeLogger
	.getLogger(Grid.class);

	private static final long serialVersionUID = 6931511384915737370L;

	//  Flag-like attribute of the grid
	private Set<GridFlag> flags;

	// Lower left corner of the grid
	private final double x_min;
	private final double y_min;
	private final double z_min;

	// TODO Might be parameterized
	private static final double solvent_buffer = 0.7;
	private static final int margin = 2; // No. of columns of solvent at the edge of the grid

	// the actual grid
	private byte[][][] grid;
	
	// stores the chain of the residue for each gridcell
	private List<List<Character>> chains; 

	// stores the resid of the residue
	private List<List<Integer>> resid;

	

	// Number of cells of the grid in each dimension (triclinic)
	private final int x_dim;
	private final int y_dim;
	private final int z_dim;

	public Grid(final double lower_x, 
			final double lower_y, 
			final double lower_z, 
			final double upper_x, 
			final double upper_y, 
			final double upper_z) {

		this.flags = new HashSet<GridFlag>();

		this.x_dim = (int) (Math.ceil(upper_x - lower_x) + 2 * margin);
		this.y_dim = (int) (Math.ceil(upper_y - lower_y) + 2 * margin);
		this.z_dim = (int) (Math.ceil(upper_z - lower_z) + 2 * margin);
		this.x_min = lower_x;
		this.y_min = lower_y;
		this.z_min = lower_z;
		
		this.grid = new byte[x_dim][y_dim][z_dim];
		
		// Keeps track of the residues that are associated with each grid cell
		this.chains = new ArrayList<List<Character>>(x_dim * y_dim * z_dim);
		this.resid = new ArrayList<List<Integer>>(x_dim * y_dim * z_dim);
	}
 
	private int translate(final double value, final double min) {
		return (int) Math.floor(value - min) + margin;
	}


	/**
	 * Sets the cell in the grid having the provided coordinates to true
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 */
	private void flagCell(final double x, final double y, final double z) {

		// Increase the value in the grid
		int index_x = this.translate(x, x_min);
		int index_y = this.translate(y, y_min);
		int index_z = this.translate(z, z_min);
		
		this.grid[index_x][index_y][index_z]++;
		
		
		// make the nested index flat for the list
		int large_index = index_z * x_dim * y_dim + x_dim * index_y + index_x;
				
		List<Character> current_chains = this.chains.get(large_index);
		if(current_chains == null) {
			
			this.chains.set(large_index, new ArrayList<Character>());
		}
		
		
		List<Integer> current_resids = this.resid.get(large_index);
		
			
	}

	/**
	 * Returns the int value of the grid that is associated with the provided coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public int getValue(final double x, final double y, final double z) {

		return this.grid[this.translate(x, x_min)]
				[this.translate(y, y_min)]
						[this.translate(z, z_min)];
	}

	public int getXDim() {

		return this.x_dim;
	}

	public int getYDim() {

		return this.y_dim;
	}

	public int getZDim() {

		return this.z_dim;
	}


	public void addAtom(
			final double x,
			final double y,
			final double z,
			final Element element) {

		// Set grid cells occupied by that atom to true
		double radius = element.vdWRadius + solvent_buffer;

		for (double x_iter = 0; x_iter <= radius; x_iter += 1) {

			// Radius of the x slice
			double x_radius = Math.sin(Math.acos(x_iter/radius)) * radius;

			for (double y_iter = 0; y_iter <= x_radius; y_iter += 1) {

				double y_radius = Math.sin(Math.acos(y_iter/x_radius)) * x_radius;

				for (double z_iter = 0; z_iter <= y_radius; z_iter += 1) {

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

	/**
	 * Adds a new flag to the grid. Setting the flag multiple times has further effect.
	 * 
	 * @param flag Which flag to set.
	 */
	public void setFlag(GridFlag flag) {

		this.flags.add(flag);
	}

	/**
	 * Removes flag from the grid.
	 * 
	 * @param flag Removes flag from the set
	 */
	public void unsetFlag(GridFlag flag) {

		this.flags.remove(flag);
	}

	/**
	 * Checks whether the grid flag is set
	 * 
	 * @param flag Which flag to check
	 * @return Whether the flag is set
	 */
	public boolean isFlagSet(GridFlag flag) {

		return this.flags.contains(flag);
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
