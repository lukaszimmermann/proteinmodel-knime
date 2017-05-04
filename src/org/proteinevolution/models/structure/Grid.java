package org.proteinevolution.models.structure;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.jmol.util.Logger;
import org.proteinevolution.models.spec.pdb.Atom;
import org.proteinevolution.models.spec.pdb.Element;
import org.proteinevolution.models.spec.pdb.Residue;

/*
 *  Class which is used to calculate an orthogonal grid of a 3D structure by adding atoms successively
 *   
 *  @author: lzimmermann
 */
public final class Grid implements Serializable {

	
	
	public final class Trace {
		
		private int x_start;
		private int y_start;
		private int z_start;
		
	}
	
	

	private static final int margin = 5; // No. of columns of solvent at the edge of the grid

	
	// Possible values that grid cell can assume (the behavior of the grid for all other values is undefined)

	// 1. These values are fixed

	// Grid is blocked by at least another atom which is no donor or acceptor atom
	private static final byte OCCUPIED = 120;

	// Grid is occupied by only one acceptor atom (which means the atom is reachable)
	private static final byte DONOR = 119;

	// Grid is occupied by only one donor atom (which means the atom is reachable)
	private static final byte ACCEPTOR = 118;

	// Grid occupied by only one atom, which is either donor or acceptor (which means the atom is reachable)
	private static final byte DONOR_ACCEPTOR = 117;
	
	// Set the cells at the border of the grid to BORDER
	private static final byte BORDER = 116;
	
	// Solvent threshold; everything below this value will be considered as solvent (allows fast resetting of the grid after the BFS has been performed)
	private byte flag_threshold = 1;

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = 6931511384915737370L;

	// End of STATIC //////////////////////////////////////////////////////////////////////////////////////////
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

		this.grid = new byte[x_dim][y_dim][z_dim];

		this.donors = donors;
		this.acceptors = acceptors;
		this.flags.add(GridFlag.SASD_CALCULATION);
		
		
		// Set the border region of the grid to BORDER, to prevent 'falling off' the grid
		for (int i = 1; i < this.x_dim - 1 ; ++i) {
			
			for (int j = 1; j < this.y_dim -1; ++j) {
				
				this.grid[i][j][0] = Grid.BORDER;
				this.grid[i][j][this.z_dim - 1] = Grid.BORDER;
			}
			
			// j == 0 or j == this_ydim-1
			for (int k = 0; k < this.z_dim; ++k) {
				
				this.grid[i][0][k] = Grid.BORDER; 
				this.grid[i][this.y_dim - 1][k] = Grid.BORDER;
			}
		}
		
		// x == 0 or x == x_dim-1
		
		for (int j = 0; j <  this.y_dim - 1; ++j) {
			
			for (int k = 0; k < this.z_dim - 1; ++k) {
					
				this.grid[0][j][k] = Grid.BORDER;
				this.grid[this.x_dim - 1][j][k] = Grid.BORDER;
			}
		}
	}

	private int translateX(final double value) {

		return (int) Math.floor(value - this.x_min) + margin;
	}

	private int translateY(final double value) {

		return (int) Math.floor(value - this.y_min) + margin;
	}

	private int translateZ(final double value) {

		return (int) Math.floor(value - this.z_min) + margin;
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
	
	
	
	public void performBFS(
			final double x,  // The start coordinates
			final double y,
			final double z,
			final Element element,
			final int max_length, // Allowed maximal length
			final Set<Point3D> toFind // Coordinates of the points that are to be found
		) {
		
		int[] start = this.queryAtom(x, y, z, element);
		
		// Set initial current value of the grid search
		int current_x = start[0];
		int current_y = start[1];
		int current_z = start[2];
		
		// See if we start at donor/acceptor/donor_acceptor
		byte cellType = this.grid[current_x][current_y][current_z];
		
		if ( 	   cellType != Grid.ACCEPTOR 
				&& cellType != Grid.DONOR
				&& cellType != Grid.DONOR_ACCEPTOR) {
			
			throw new IllegalStateException("Grid cell is neither Donor nor Acceptor, so the BFS cannot be started");
		}
		// Queue used for Exploring the grid, one for each direction
		Queue<Integer> x_queue = new LinkedList<Integer>();
		Queue<Integer> y_queue = new LinkedList<Integer>();
		Queue<Integer> z_queue = new LinkedList<Integer>();
		Queue<Integer> length = new LinkedList<Integer>();
		
		// Initialization

		// Enqueue the first point
		x_queue.add(current_x);
		y_queue.add(current_y);
		z_queue.add(current_z);
		length.add(0);

		do {
			// Get next grid point and the length
			current_x = x_queue.poll();
			current_y = y_queue.poll();
			current_z = z_queue.poll();
			int current_length = length.poll();
			
			if (current_length < max_length) {
				
				// Consider all possible directions, but only walk into solvent
				
				if (this.grid[current_x-1][current_y][current_z] < this.flag_threshold) {

					x_queue.add(current_x-1);
					y_queue.add(current_y);
					z_queue.add(current_z);
					this.grid[current_x-1][current_y][current_z] = this.flag_threshold;
					length.add(current_length+1);
				}
				if (this.grid[current_x+1][current_y][current_z] < this.flag_threshold) {

					x_queue.add(current_x+1);
					y_queue.add(current_y);
					z_queue.add(current_z);
					this.grid[current_x+1][current_y][current_z] = this.flag_threshold;
					length.add(current_length+1);
				}

				if (this.grid[current_x][current_y-1][current_z] < this.flag_threshold) {

					x_queue.add(current_x);
					y_queue.add(current_y-1);
					z_queue.add(current_z);
					this.grid[current_x][current_y-1][current_z] = this.flag_threshold; 
					length.add(current_length+1);
				}

				if (this.grid[current_x][current_y+1][current_z] < this.flag_threshold) {

					x_queue.add(current_x);
					y_queue.add(current_y+1);
					z_queue.add(current_z);
					this.grid[current_x][current_y+1][current_z] = this.flag_threshold;
					
					length.add(current_length+1);
				}

				if (this.grid[current_x][current_y][current_z-1] < this.flag_threshold) {

					x_queue.add(current_x);
					y_queue.add(current_y);
					z_queue.add(current_z-1);
					this.grid[current_x][current_y][current_z-1] = this.flag_threshold;
					length.add(current_length+1);
					
				}

				if (this.grid[current_x][current_y][current_z+1] < this.flag_threshold) {

					x_queue.add(current_x);
					y_queue.add(current_y);
					z_queue.add(current_z+1);
					this.grid[current_x][current_y][current_z+1] = this.flag_threshold; 
					length.add(current_length+1);
				}
			}

		} while( ! x_queue.isEmpty());
		
		// BFS has finished. Increase the flag threshold
		this.flag_threshold++;
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

	/**
	 * Occupies (set cell value to OCCUPIED) all cells in the grid that lie within 
	 * the VDW radius of an encountered atom
	 * @param x x coordinate of the atom
	 * @param y y coordinate of the atom
	 * @param z z coordinate of the atom
	 * @param atom 
	 * @return Number of cells were occupied by this call
	 */
	private void occupyVDW(final double x, final double y, final double z, Element element) {

		// Space to block is determined by the VDW radius of the atom
		double radius = element.vdWRadius;

		for (double x_iter = 0; x_iter < radius; x_iter++) {

			int coord_x_1 = this.translateX(x + x_iter);
			int coord_x_2 = this.translateX(x - x_iter);

			double x_radius = Math.sin(Math.acos(x_iter/radius)) * radius;

			for (double y_iter = 0; y_iter < x_radius; y_iter++) {

				int coord_y_1 = this.translateY(y + y_iter);
				int coord_y_2 = this.translateY(y - y_iter);

				double y_radius = Math.sin(Math.acos(y_iter/x_radius)) * x_radius;

				for (double z_iter = 0; z_iter < y_radius; z_iter++) {

					int coord_z_1 = this.translateZ(z + z_iter);
					int coord_z_2 = this.translateZ(z - z_iter);

					this.grid[coord_x_1][coord_y_1][coord_z_1] = Grid.OCCUPIED;
					this.grid[coord_x_1][coord_y_1][coord_z_2] = Grid.OCCUPIED;
					this.grid[coord_x_1][coord_y_2][coord_z_1] = Grid.OCCUPIED;
					this.grid[coord_x_1][coord_y_2][coord_z_2] = Grid.OCCUPIED;
					this.grid[coord_x_2][coord_y_1][coord_z_1] = Grid.OCCUPIED;
					this.grid[coord_x_2][coord_y_1][coord_z_2] = Grid.OCCUPIED;
					this.grid[coord_x_2][coord_y_2][coord_z_1] = Grid.OCCUPIED;
					this.grid[coord_x_2][coord_y_2][coord_z_2] = Grid.OCCUPIED;
				}
				// Update cells or z_iter = y_radius
				int coord_z_1 = this.translateZ(z + y_radius);
				int coord_z_2 = this.translateZ(z - y_radius);

				this.grid[coord_x_1][coord_y_1][coord_z_1] = Grid.OCCUPIED;
				this.grid[coord_x_1][coord_y_1][coord_z_2] = Grid.OCCUPIED;
				this.grid[coord_x_1][coord_y_2][coord_z_1] = Grid.OCCUPIED;
				this.grid[coord_x_1][coord_y_2][coord_z_2] = Grid.OCCUPIED;
				this.grid[coord_x_2][coord_y_1][coord_z_1] = Grid.OCCUPIED;
				this.grid[coord_x_2][coord_y_1][coord_z_2] = Grid.OCCUPIED;
				this.grid[coord_x_2][coord_y_2][coord_z_1] = Grid.OCCUPIED;
				this.grid[coord_x_2][coord_y_2][coord_z_2] = Grid.OCCUPIED;
			}

			// Update for y_iter = x_radius
			// Then z_iter = 0
			int coord_y_1 = this.translateY(y + x_radius);
			int coord_y_2 = this.translateY(y - x_radius);
			int coord_z = this.translateZ(z);

			this.grid[coord_x_1][coord_y_1][coord_z] = Grid.OCCUPIED;
			this.grid[coord_x_1][coord_y_2][coord_z] = Grid.OCCUPIED;
			this.grid[coord_x_2][coord_y_1][coord_z] = Grid.OCCUPIED;
			this.grid[coord_x_2][coord_y_2][coord_z] = Grid.OCCUPIED;
		}

		// Update for x_iter = radius
		// So y_iter = 0
		// So z_iter = 0
		int coord_y = this.translateY(y);
		int coord_z = this.translateZ(z);

		this.grid[this.translateX(x + radius)][coord_y][coord_z] = Grid.OCCUPIED;
		this.grid[this.translateX(x - radius)][coord_y][coord_z] = Grid.OCCUPIED;
	}



	public void addAtom(
			final double x,
			final double y,
			final double z,
			final Residue residue,
			final Atom atom) {

		// Related to the 
		boolean residueBelongsToDonor = this.donors.containsKey(residue);
		boolean residueBelongsToAcceptor = this.acceptors.containsKey(residue);
		
		boolean isDonor = residueBelongsToDonor && this.donors.get(residue).contains(atom);
		boolean isAcceptor = residueBelongsToAcceptor && this.acceptors.get(residue).contains(atom);

		// Ignore hydrogen
		if (atom.element.equals(Element.H)) {
			return;
		}
		
		// Decide which grid value needs to be set (flagCell will then ultimately decide how to set)
		byte value_to_set = (isDonor && isAcceptor) ? Grid.DONOR_ACCEPTOR : (isDonor ? Grid.DONOR : (isAcceptor ? Grid.ACCEPTOR : Grid.OCCUPIED));

		// occupy the vdW volume of the atom
		this.occupyVDW(x, y, z, atom.element);

		if (value_to_set != OCCUPIED) {

			this.makeAccessible(x, y, z, atom.element, value_to_set);
		}
	}


	private void setIfNotOccupied(int x_index, int y_index, int z_index, byte value_to_set) {			
		if (this.grid[x_index][y_index][z_index] != OCCUPIED) {
			this.grid[x_index][y_index][z_index] = value_to_set;
		}
	}


	private void makeAccessible(double x, double y, double z, Element element, byte value_to_set) {

		double radius = element.vdWRadius + 1;

		for (double x_iter = 0; x_iter < radius; x_iter++) {

			int coord_x_1 = this.translateX(x + x_iter);
			int coord_x_2 = this.translateX(x - x_iter);

			double x_radius = Math.sin(Math.acos(x_iter/radius)) * radius;

			for (double y_iter = 0; y_iter < x_radius; y_iter++) {

				int coord_y_1 = this.translateY(y + y_iter);
				int coord_y_2 = this.translateY(y - y_iter);

				double y_radius = Math.sin(Math.acos(y_iter/x_radius)) * x_radius;

				// Update cells or z_iter = y_radius
				int coord_z_1 = this.translateZ(z + y_radius);
				int coord_z_2 = this.translateZ(z - y_radius);

				this.setIfNotOccupied(coord_x_1, coord_y_1, coord_z_1, value_to_set);
				this.setIfNotOccupied(coord_x_1, coord_y_1, coord_z_2, value_to_set);
				this.setIfNotOccupied(coord_x_1, coord_y_2, coord_z_1, value_to_set);
				this.setIfNotOccupied(coord_x_1, coord_y_2, coord_z_2, value_to_set);
				this.setIfNotOccupied(coord_x_2, coord_y_1, coord_z_1, value_to_set);
				this.setIfNotOccupied(coord_x_2, coord_y_1, coord_z_2, value_to_set);
				this.setIfNotOccupied(coord_x_2, coord_y_2, coord_z_1, value_to_set);
				this.setIfNotOccupied(coord_x_2, coord_y_2, coord_z_2, value_to_set);
			}

			// Update for y_iter = x_radius
			// Then z_iter = 0
			int coord_y_1 = this.translateY(y + x_radius);
			int coord_y_2 = this.translateY(y - x_radius);
			int coord_z = this.translateZ(z);

			this.setIfNotOccupied(coord_x_1, coord_y_1, coord_z, value_to_set);
			this.setIfNotOccupied(coord_x_1, coord_y_2, coord_z, value_to_set);
			this.setIfNotOccupied(coord_x_2, coord_y_1, coord_z, value_to_set);
			this.setIfNotOccupied(coord_x_2, coord_y_2, coord_z, value_to_set);
		}

		// Update for x_iter = radius
		// So y_iter = 0
		// So z_iter = 0
		int coord_y = this.translateY(y);
		int coord_z = this.translateZ(z);

		this.setIfNotOccupied(this.translateX(x + radius), coord_y, coord_z, value_to_set);
		this.setIfNotOccupied(this.translateX(x - radius), coord_y, coord_z, value_to_set);
	}

	private int[] queryAtom(
			final double x,
			final double y,
			final double z,
			final Element element) {

		double radius = element.vdWRadius + 1;
		
		for (double x_iter = 0; x_iter < radius; x_iter++) {

			int coord_x_1 = this.translateX(x + x_iter);
			int coord_x_2 = this.translateX(x - x_iter);

			double x_radius = Math.sin(Math.acos(x_iter/radius)) * radius;

			for (double y_iter = 0; y_iter < x_radius; y_iter++) {

				int coord_y_1 = this.translateY(y + y_iter);
				int coord_y_2 = this.translateY(y - y_iter);

				double y_radius = Math.sin(Math.acos(y_iter/x_radius)) * x_radius;

				// Update cells or z_iter = y_radius
				int coord_z_1 = this.translateZ(z + y_radius);
				int coord_z_2 = this.translateZ(z - y_radius);

				if (this.grid[coord_x_1][coord_y_1][coord_z_1] != Grid.OCCUPIED) {	
					return new int[] {coord_x_1, coord_y_1, coord_z_1  };
				}
				if (this.grid[coord_x_1][coord_y_1][coord_z_2] != Grid.OCCUPIED) {	
					return new int[]  {coord_x_1, coord_y_1, coord_z_2 };
				}
				if (this.grid[coord_x_1][coord_y_2][coord_z_1] != Grid.OCCUPIED) {	
					return new int[] {coord_x_1, coord_y_2, coord_z_1};
				}
				if (this.grid[coord_x_1][coord_y_2][coord_z_2] != Grid.OCCUPIED) {	
					return new int[] {coord_x_1, coord_y_2, coord_z_2 };
				}
				if (this.grid[coord_x_2][coord_y_1][coord_z_1] != Grid.OCCUPIED) {	
					return new int[] {coord_x_2, coord_y_1, coord_z_1};
				}
				if (this.grid[coord_x_2][coord_y_1][coord_z_2] != Grid.OCCUPIED) {	
					return new int[] {coord_x_2, coord_y_1, coord_z_2};
				}
				if (this.grid[coord_x_2][coord_y_2][coord_z_1] != Grid.OCCUPIED) {	
					return new int[] {coord_x_2, coord_y_2, coord_z_1} ;
				}
				if (this.grid[coord_x_2][coord_y_2][coord_z_2] != Grid.OCCUPIED) {	
					return new int[] {coord_x_2, coord_y_2, coord_z_2};
				}
			}

			// Update for y_iter = x_radius
			// Then z_iter = 0
			int coord_y_1 = this.translateY(y + x_radius);
			int coord_y_2 = this.translateY(y - x_radius);
			int coord_z = this.translateZ(z);

			if (this.grid[coord_x_1][coord_y_1][coord_z] != Grid.OCCUPIED) {	
				return new int[] {coord_x_1, coord_y_1, coord_z};
			}
			if (this.grid[coord_x_1][coord_y_2][coord_z] != Grid.OCCUPIED) {	
				return new int[] {coord_x_1, coord_y_2, coord_z};
			}
			if (this.grid[coord_x_2][coord_y_1][coord_z] != Grid.OCCUPIED) {	
				return new int[] {coord_x_2, coord_y_1, coord_z};
			}
			if (this.grid[coord_x_2][coord_y_2][coord_z] != Grid.OCCUPIED) {	
				return new int[] {coord_x_2, coord_y_2, coord_z};
			}
		}

		// Update for x_iter = radius
		// So y_iter = 0
		// So z_iter = 0
		
		int coord_x_1 = this.translateX(x + radius);
		int coord_x_2 = this.translateX(x - radius);
		int coord_y = this.translateY(y);
		int coord_z = this.translateZ(z);

		if (this.grid[coord_x_1][coord_y][coord_z] != Grid.OCCUPIED) {	
			return new int[] {coord_x_1, coord_y, coord_z};
		}
		if (this.grid[coord_x_2][coord_y][coord_z] != Grid.OCCUPIED) {	
			return new int[] {coord_x_2, coord_y, coord_z};
		}
		return null;
	}


	/**
	 * Returns the percentage of the surface shell that is accessible of the atom
	 * @param x
	 * @param y
	 * @param z
	 * @param residue
	 * @param atom
	 * @param grid_values
	 * @return
	 */
	public double queryAtomAccessibility(
			final double x,
			final double y,
			final double z,
			final Residue residue,
			final Element element) {

		double radius = element.vdWRadius + 1;
		
		double numerator = 0;
		double denominator = 0;

		for (double x_iter = 0; x_iter < radius; x_iter++) {

			int coord_x_1 = this.translateX(x + x_iter);
			int coord_x_2 = this.translateX(x - x_iter);

			double x_radius = Math.sin(Math.acos(x_iter/radius)) * radius;

			for (double y_iter = 0; y_iter < x_radius; y_iter++) {

				int coord_y_1 = this.translateY(y + y_iter);
				int coord_y_2 = this.translateY(y - y_iter);

				double y_radius = Math.sin(Math.acos(y_iter/x_radius)) * x_radius;

				// Update cells or z_iter = y_radius
				int coord_z_1 = this.translateZ(z + y_radius);
				int coord_z_2 = this.translateZ(z - y_radius);

				if (this.grid[coord_x_1][coord_y_1][coord_z_1] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_1][coord_y_1][coord_z_2] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_1][coord_y_2][coord_z_1] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_1][coord_y_2][coord_z_2] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_2][coord_y_1][coord_z_1] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_2][coord_y_1][coord_z_2] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_2][coord_y_2][coord_z_1] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_2][coord_y_2][coord_z_2] != Grid.OCCUPIED) {	
					numerator++;
				}
				denominator += 8;
			}

			// Update for y_iter = x_radius
			// Then z_iter = 0
			int coord_y_1 = this.translateY(y + x_radius);
			int coord_y_2 = this.translateY(y - x_radius);
			int coord_z = this.translateZ(z);

			if (this.grid[coord_x_1][coord_y_1][coord_z] != Grid.OCCUPIED) {	
				numerator++;
			}
			if (this.grid[coord_x_1][coord_y_2][coord_z] != Grid.OCCUPIED) {	
				numerator++;
			}
			if (this.grid[coord_x_2][coord_y_1][coord_z] != Grid.OCCUPIED) {	
				numerator++;
			}
			if (this.grid[coord_x_2][coord_y_2][coord_z] != Grid.OCCUPIED) {	
				numerator++;
			}
			denominator += 4;
		}

		// Update for x_iter = radius
		// So y_iter = 0
		// So z_iter = 0
		int coord_y = this.translateY(y);
		int coord_z = this.translateZ(z);

		if (this.grid[this.translateX(x + radius)][coord_y][coord_z] != Grid.OCCUPIED) {	
			numerator++;
		}
		if (this.grid[this.translateX(x - radius)][coord_y][coord_z] != Grid.OCCUPIED) {	
			numerator++;
		}
		denominator += 2;
		
		return ((double) numerator) / ((double) denominator);
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
