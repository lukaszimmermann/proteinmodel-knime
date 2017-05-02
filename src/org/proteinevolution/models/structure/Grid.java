package org.proteinevolution.models.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jmol.util.Logger;
import org.proteinevolution.models.spec.pdb.Atom;
import org.proteinevolution.models.spec.pdb.Residue;

/*
 *  Class which is used to calculate an orthogonal grid of a 3D structure by adding atoms successively
 *   
 *  @author: lzimmermann
 */
public final class Grid implements Serializable {


	// TODO Might be parameterized
	private static final double solvent_buffer = 0.7;
	private static final int margin = 3; // No. of columns of solvent at the edge of the grid


	// Possible values that grid cell can assume (the behavior of the grid for all other values is undefined)

	// Grid cell is part of the solvent
	public static final byte SOLVENT = 0;

	// Grid is blocked by at least another atom which is no donor or acceptor atom
	public static final byte OCCUPIED = 1;

	// Grid is occupied by only one acceptor atom (which means the atom is reachable)
	public static final byte DONOR = 2;

	// Grid is occupied by only one donor atom (which means the atom is reachable)
	public static final byte ACCEPTOR = 3;

	// Grid occupied by only one atom, which is either donor or acceptor (which means the atom is reachable)
	public static final byte DONOR_ACCEPTOR = 4;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = 6931511384915737370L;

	// End of STATIC //////////////////////////////////////////////////////////////////////////////////////////

	// Counts the number of cells belonging to each type
	private Map<Byte, Integer> cell_counts;

	//  Flag-like attribute of the grid
	private Set<Integer> flags;

	// the actual grid
	private byte[][][] grid;

	// donor and acceptor residues (we are only allowed to set this during construction)
	private final Map<Residue, Set<Atom>> donors;
	private final Map<Residue, Set<Atom>> acceptors;

	// Number of cells of the grid in each dimension (triclinic)
	private final int x_dim;
	private final int y_dim;
	private final int z_dim;

	// Lower left corner of the grid
	private final double x_min;
	private final double y_min;
	private final double z_min;

	// size of the grid
	private final int size;

	public Grid(
			final double lower_x, 
			final double lower_y, 
			final double lower_z, 
			final double upper_x, 
			final double upper_y, 
			final double upper_z,
			final Map<Residue, Set<Atom>> donors,
			final Map<Residue, Set<Atom>> acceptors) {

		this.flags = new HashSet<Integer>();

		// Set grid dimensions
		this.x_dim = (int) (Math.ceil(upper_x - lower_x) + 2 * margin);
		this.y_dim = (int) (Math.ceil(upper_y - lower_y) + 2 * margin);
		this.z_dim = (int) (Math.ceil(upper_z - lower_z) + 2 * margin);

		// Set corner of grid
		this.x_min = lower_x;
		this.y_min = lower_y;
		this.z_min = lower_z;

		this.size = x_dim * y_dim * z_dim;

		// Initialize cell counts
		this.cell_counts = new HashMap<Byte, Integer>();
		this.cell_counts.put(SOLVENT, this.size);
		this.cell_counts.put(DONOR, 0);
		this.cell_counts.put(ACCEPTOR, 0);
		this.cell_counts.put(DONOR_ACCEPTOR, 0);
		this.cell_counts.put(OCCUPIED, 0);

		this.grid = new byte[x_dim][y_dim][z_dim];

		// Must be set after the above constructor call
		this.donors = donors;
		this.acceptors = acceptors;
		this.flags.add(GridFlag.SASD_CALCULATION);
	}



	private int translateX(final double value) {

		int res = (int) Math.floor(value - this.x_min) + margin;

		if (res < 0) {

			Logger.warn("x-value is " + value);
			Logger.warn("XMin is : " + this.x_min);
		}

		return res;
	}

	private int translateY(final double value) {

		int res = (int) Math.floor(value - this.y_min) + margin;

		if (res < 0) {

			Logger.warn("y-value is " + value);
			Logger.warn("YMin is : " + this.y_min);
		}
		return res;
	}


	private int translateZ(final double value) {

		int res = (int) Math.floor(value - this.z_min) + margin;

		if (res < 0) {

			Logger.warn("z-value is " + value);
			Logger.warn("ZMin is : " + this.z_min);
		}
		return res;
	}


	/**
	 * Returns the donor residues of this grid.
	 * Makes a copy, so modifying the returned map will not change the grid
	 * 
	 * @return Copy of the donors
	 */
	public Map<Residue, Set<Atom>> copyDonors() {

		return new HashMap<Residue, Set<Atom>>(this.donors);
	}

	/**
	 * Returns the acceptor residues of this grid.
	 * Makes a copy, so modifying the returned map will not change the grid
	 * 
	 * @return Copy of the acceptors
	 */
	public Map<Residue, Set<Atom>> copyAcceptors() {

		return new HashMap<Residue, Set<Atom>>(this.acceptors);
	}


	/**
	 * Determines whether the residue_atom is acceptor in this grid
	 * 
	 * @param residue Which residue the atom belongs to
	 * @param atom Atom name
	 * @return true if the atom_name is acceptor, false otherwise
	 */
	public boolean isAcceptor(Residue residue, Atom atom) {

		return this.acceptors.containsKey(residue) && this.acceptors.get(residue).contains(atom);
	}

	/**
	 * Determines whether the residue_atom is donor in this grid
	 * 
	 * @param residue Which residue the atom belongs to
	 * @param atom Atom name
	 * @return true if the atom_name is donor, false otherwise
	 */
	public boolean isDonor(Residue residue, Atom atom) {

		return this.donors.containsKey(residue) && this.donors.get(residue).contains(atom);
	}

	public int getCellCounts(byte cell_type) {

		if ( ! this.cell_counts.containsKey(cell_type)) {

			return -1;
		}
		return this.cell_counts.get(cell_type);
	}

	/**
	 * Sets the cell in the grid having the provided coordinates to true
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 */
	private void flagCell(final double x, final double y, final double z, byte value_to_set) {

		// Increase the value in the grid
		int index_x = this.translateX(x);
		int index_y = this.translateY(y);
		int index_z = this.translateZ(z);

		byte current_value = this.grid[index_x][index_y][index_z];

		// Once occupied, always occupied, we ignore value_to_set.
		// Also, setting SOLVENT does not make sense
		if (current_value == OCCUPIED || value_to_set == SOLVENT) {
			return;
		}
		byte final_value = value_to_set;

		if (    (current_value == DONOR && value_to_set == ACCEPTOR)
				||	(current_value == ACCEPTOR && value_to_set == DONOR)
				|| (current_value == DONOR_ACCEPTOR && (   value_to_set == DONOR 
				|| value_to_set == ACCEPTOR))){

			final_value = DONOR_ACCEPTOR;
		}

		// Update cell counts
		this.cell_counts.put(current_value, this.cell_counts.get(current_value) - 1 );
		this.cell_counts.put(final_value,   this.cell_counts.get(final_value) + 1 );

		this.grid[index_x][index_y][index_z] = final_value;
	}

	/**
	 * Returns the byte value of the grid that is associated with the provided coordinates (in A space)
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public int getValue(final double x, final double y, final double z) {

		return this.grid[this.translateX(x)][this.translateY(y)][this.translateZ(z)];
	}

	/**
	 * Returns the byte value of the grid that is associated with the provided coordinates (in index space)
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public int getValue(final int x, final int y, final int z) {

		return this.grid[x][y][z];
	}

	/**
	 * Transforms Angstrom coordinates of the grid to the index domain
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public int[] toIndex(final double x, final double y, final double z) {

		return new int[] {
				this.translateX(x),
				this.translateY(y),
				this.translateZ(z)
		};
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

	public int getSize() {

		return this.size;
	}

	public void addAtom(
			final double x,
			final double y,
			final double z,
			final Residue residue,
			final Atom atom) {

		// See whether the atom is donor or acceptor
		boolean isDonor = this.donors.containsKey(residue) && this.donors.get(residue).contains(atom);
		boolean isAcceptor = this.acceptors.containsKey(residue) && this.acceptors.get(residue).contains(atom);

		// Decide which grid value needs to be set (flagCell will then ultimately decide how to set)
		byte value_to_set = (isDonor && isAcceptor) ? DONOR_ACCEPTOR : (isDonor ? DONOR : (isAcceptor ? ACCEPTOR : OCCUPIED));

		// Set grid cells occupied by that atom to true
		double radius = atom.element.vdWRadius + solvent_buffer;

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

					this.flagCell(coord_x_1, coord_y_1, coord_z_1, value_to_set);
					this.flagCell(coord_x_1, coord_y_1, coord_z_2, value_to_set);
					this.flagCell(coord_x_1, coord_y_2, coord_z_1, value_to_set);
					this.flagCell(coord_x_1, coord_y_2, coord_z_2, value_to_set);
					this.flagCell(coord_x_2, coord_y_1, coord_z_1, value_to_set);
					this.flagCell(coord_x_2, coord_y_1, coord_z_2, value_to_set);
					this.flagCell(coord_x_2, coord_y_2, coord_z_1, value_to_set);
					this.flagCell(coord_x_2, coord_y_2, coord_z_2, value_to_set);					
				}
			}	
		}



	}


	/**
	 * Returns all cell indices around a cartesian center where the grid cells have particular values
	 * TODO Modify such that only the shell is accessible
	 * @param x
	 * @param y
	 * @param z
	 * @param residue
	 * @param atom
	 * @param grid_values
	 * @return
	 */
	public List<int[]>  queryAtom(
			final double x,
			final double y,
			final double z,
			final Residue residue,
			final Atom atom,
			final Set<Byte> grid_values) {

		// traverse the grid cells
		// Set grid cells occupied by that atom to true
		double radius = atom.element.vdWRadius + solvent_buffer;

		List<int[]> res = new ArrayList<int[]>();

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

					// Get the indices
					int[] value0 = this.toIndex(coord_x_1, coord_y_1, coord_z_1);
					int[] value1 = this.toIndex(coord_x_1, coord_y_1, coord_z_2);
					int[] value2 = this.toIndex(coord_x_1, coord_y_2, coord_z_1);
					int[] value3 = this.toIndex(coord_x_1, coord_y_2, coord_z_2);
					int[] value4 = this.toIndex(coord_x_2, coord_y_1, coord_z_1);
					int[] value5 = this.toIndex(coord_x_2, coord_y_1, coord_z_2);
					int[] value6 = this.toIndex(coord_x_2, coord_y_2, coord_z_1);
					int[] value7 = this.toIndex(coord_x_2, coord_y_2, coord_z_2);

					if (grid_values.contains(this.grid[value0[0]][value0[1]][value0[2]])) {

						res.add(value0);
					}
					if (grid_values.contains(this.grid[value1[0]][value1[1]][value1[2]])) {

						res.add(value1);
					}
					if (grid_values.contains(this.grid[value2[0]][value2[1]][value2[2]])) {

						res.add(value2);
					}
					if (grid_values.contains(this.grid[value3[0]][value3[1]][value3[2]])) {

						res.add(value3);
					}
					if (grid_values.contains(this.grid[value4[0]][value4[1]][value4[2]])) {

						res.add(value4);
					}
					if (grid_values.contains(this.grid[value5[0]][value5[1]][value5[2]])) {

						res.add(value5);
					}
					if (grid_values.contains(this.grid[value6[0]][value6[1]][value6[2]])) {

						res.add(value6);
					}
					if (grid_values.contains(this.grid[value7[0]][value7[1]][value7[2]])) {

						res.add(value7);
					}
				}
			}	
		}
		return res;
	}


	

	
	/**
	 * Returns all cell indices around a cartesian center where the grid cells have particular values
	 * TODO Modify such that only the shell is accessible
	 * @param x
	 * @param y
	 * @param z
	 * @param residue
	 * @param atom
	 * @param grid_values
	 * @return
	 */
	public double queryAtomPercentage(
			final double x,
			final double y,
			final double z,
			final Residue residue,
			final Atom atom,
			final Set<Byte> grid_values) {

		// traverse the grid cells
		// Set grid cells occupied by that atom to true
		double radius = atom.element.vdWRadius + solvent_buffer;

		int total_points = 0;
		int enumerator = 0;
		
		for (double x_iter = 0; x_iter <= radius; x_iter += 1) {

			// Radius of the x slice
			double x_radius = Math.sin(Math.acos(x_iter/radius)) * radius;

			for (double y_iter = 0; y_iter <= x_radius; y_iter += 1) {

				double y_radius = Math.sin(Math.acos(y_iter/x_radius)) * x_radius;

				for (double z_iter = 0; z_iter <= y_radius; z_iter += 1) {

					total_points += 8;
					
					// points to update
					double coord_x_1 = x + x_iter;
					double coord_x_2 = x - x_iter;
					double coord_y_1 = y + y_iter;
					double coord_y_2 = y - y_iter;
					double coord_z_1 = z + z_iter;
					double coord_z_2 = z - z_iter;

					// Get the indices
					int[] value0 = this.toIndex(coord_x_1, coord_y_1, coord_z_1);
					int[] value1 = this.toIndex(coord_x_1, coord_y_1, coord_z_2);
					int[] value2 = this.toIndex(coord_x_1, coord_y_2, coord_z_1);
					int[] value3 = this.toIndex(coord_x_1, coord_y_2, coord_z_2);
					int[] value4 = this.toIndex(coord_x_2, coord_y_1, coord_z_1);
					int[] value5 = this.toIndex(coord_x_2, coord_y_1, coord_z_2);
					int[] value6 = this.toIndex(coord_x_2, coord_y_2, coord_z_1);
					int[] value7 = this.toIndex(coord_x_2, coord_y_2, coord_z_2);

					if (grid_values.contains(this.grid[value0[0]][value0[1]][value0[2]])) {

						enumerator++;
					}
					if (grid_values.contains(this.grid[value1[0]][value1[1]][value1[2]])) {

						enumerator++;
					}
					if (grid_values.contains(this.grid[value2[0]][value2[1]][value2[2]])) {

						enumerator++;
					}
					if (grid_values.contains(this.grid[value3[0]][value3[1]][value3[2]])) {

						enumerator++;
					}
					if (grid_values.contains(this.grid[value4[0]][value4[1]][value4[2]])) {

						enumerator++;
					}
					if (grid_values.contains(this.grid[value5[0]][value5[1]][value5[2]])) {

						enumerator++;
					}
					if (grid_values.contains(this.grid[value6[0]][value6[1]][value6[2]])) {

						enumerator++;
					}
					if (grid_values.contains(this.grid[value7[0]][value7[1]][value7[2]])) {

						enumerator++;
					}
				}
			}	
		}
		return ((double)enumerator ) /  ((double) total_points);
	}
	
	
	
	


	/**
	 * Adds a new flag to the grid. Setting the flag multiple times has further effect.
	 * 
	 * @param flag Which flag to set.
	 */
	public void setFlag(final int flag) {

		this.flags.add(flag);
	}

	/**
	 * Removes flag from the grid.
	 * 
	 * @param flag Removes flag from the set
	 */
	public void unsetFlag(final int flag) {

		this.flags.remove(flag);
	}

	/**
	 * Checks whether the grid flag is set
	 * 
	 * @param flag Which flag to check
	 * @return Whether the flag is set
	 */
	public boolean isFlagSet(final int flag) {

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
